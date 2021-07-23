package com.softtek.assetworx_api.util;

import static com.softtek.assetworx_api.util.Constants.FILTERED_COUNT;
import static com.softtek.assetworx_api.util.Constants.LIST;
import static com.softtek.assetworx_api.util.Constants.TOTAL_COUNT;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.model.Column;
import com.softtek.assetworx_api.model.PagingRequest;
import com.softtek.assetworx_api.model.SearchOperation;

@Component
public class DBUtil {

	public static <T> Query createQuery(PagingRequest pagingRequest, Class<T> clazz, EntityManager em,
			String queryType) {
		String queryString = "";
		String entityPrefix = getEntityPrefix(clazz.getName());
		List<Column> validColumns = getValidColumns(pagingRequest.getColumns(), clazz.getDeclaredFields());
		switch (queryType) {
		case (LIST):
			if (pagingRequest.getType().equals("select")) {
				queryString = ("SELECT #COLUMNS FROM #ENTITY #PREFIX WHERE #PREFIX.isActive=1  #QUERYPARAMS ORDER BY #ORDERBY");
			} else {
				queryString = ("SELECT #PREFIX FROM #ENTITY #PREFIX WHERE #PREFIX.isActive=1  #QUERYPARAMS ORDER BY #ORDERBY");
			}
			queryString = queryString.replace("#COLUMNS", getColumnsForQuery(validColumns, entityPrefix));
			queryString = queryString.replace("#QUERYPARAMS", getQueryParams(validColumns, entityPrefix));
			queryString = queryString.replace("#ORDERBY", getOrderBy(pagingRequest, entityPrefix));
			queryString = queryString.replace("#ENTITY", clazz.getSimpleName());
			queryString = queryString.replace("#PREFIX", entityPrefix);
			System.out.println(queryString);
			return setQueryParams(validColumns, em.createQuery(queryString).setMaxResults(pagingRequest.getLength())
					.setFirstResult(pagingRequest.getStart()));
		case (FILTERED_COUNT):
			queryString = ("SELECT COUNT(#PREFIX) FROM #ENTITY #PREFIX WHERE #PREFIX.isActive=1  #QUERYPARAMS");
			queryString = queryString.replace("#QUERYPARAMS", getQueryParams(validColumns, entityPrefix));
			queryString = queryString.replace("#ENTITY", clazz.getSimpleName());
			queryString = queryString.replace("#PREFIX", entityPrefix);
			return setQueryParams(validColumns, em.createQuery(queryString));
		case (TOTAL_COUNT):
			queryString = ("SELECT COUNT(#PREFIX) FROM #ENTITY #PREFIX WHERE #PREFIX.isActive=1");
			queryString = queryString.replace("#ENTITY", clazz.getSimpleName());
			queryString = queryString.replace("#PREFIX", entityPrefix);
			return em.createQuery(queryString);
		default:
			return null;
		}
	}

	private static String getEntityPrefix(String className) {
		return String.valueOf(className.toLowerCase().charAt(0));
	}

	private static List<Column> getValidColumns(List<Column> columns, Field[] fileds) {
		return columns.stream().map(c -> validateColumns(c, fileds)).filter(c -> c != null)
				.collect(Collectors.toList());
	}

	private static Column validateColumns(Column c, Field[] fields) {
		for (Field field : fields) {
			String columnName = c.getName().contains(".") ? c.getName().split("\\.")[0] : c.getName();
			if (columnName.equals(field.getName())) {
				c.setType(field.getType().getSimpleName());
				return c;
			}
		}
		throw new GenericRestException("Could not fetch data.", HttpStatus.BAD_REQUEST);
	}

	private static String getColumnsForQuery(List<Column> validColumns, String prefix) {
		String columnsString = validColumns.stream().filter(c -> c.getVisible())
				.map(c -> prefix.concat('.' + c.getName())).collect(Collectors.joining(","));
		return columnsString;
	}

	private static String getQueryParams(List<Column> columns, String prefix) {
		String queryParamsString = columns.stream().filter(c -> !c.getSearch().getValue().isEmpty())
				.map(c -> parseQueryParam(c, prefix)).collect(Collectors.joining(" and "));
		return queryParamsString.isEmpty() ? "" : " and " + queryParamsString;
	}

	private static String parseQueryParam(Column c, String e) {
		String returnString = "";
		if (c.getType().equals("Date") || c.getSearch().getValue().startsWith("#")) {
			return parseDateParams(c, e);
		}
		String searchOperation = c.getSearch().getSearchOperation().toString();
		String value = c.getSearch().getValue();
		String columnName = e.concat('.' + c.getName());
		if(!c.getType().equals("boolean"))
			columnName = "(cast( "+columnName+" as string))";
		
		String paramName = c.getData();
		switch (searchOperation) {
		case ("MATCH"):
			c.getSearch().setValue("%" + value + "%");
			returnString = (columnName + " LIKE :" + paramName);
			break;
		case ("MATCH_END"):
			c.getSearch().setValue("%" + value);
			returnString = (columnName + " LIKE :" + paramName);
			break;
		case ("MATCH_START"):
			c.getSearch().setValue(value + "%");
			returnString = (columnName + " LIKE :" + paramName);
			break;
		case ("EQUAL"):
			returnString = (columnName + " = :" + paramName);
			break;
		case ("NOT_EQUAL"):
			returnString = (columnName + " != :" + paramName);
			break;
		case ("IN"):
			returnString = (columnName + " IN :" + paramName);
			break;
		case ("NOT_IN"):
			returnString = (columnName + " NOT IN :" + paramName);
			break;
		case ("BETWEEN"):
			returnString = (columnName + " BETWEEN :" + paramName + "_BEGIN AND :" + paramName + "_END");
			break;
		case ("NOT_BETWEEN"):
			returnString = (columnName + " NOT BETWEEN :" + paramName + "_BEGIN AND :" + paramName + "_END");
			break;
		case ("GREATER_THAN"):
			returnString = (columnName + " > :" + paramName);
			break;
		case ("LESS_THAN"):
			returnString = (columnName + " < :" + paramName);
			break;
		case ("GREATER_THAN_EQUAL"):
			returnString = (columnName + " >= :" + paramName);
			break;
		case ("LESS_THAN_EQUAL"):
			returnString = (columnName + " <= :" + paramName);
			break;
		default:
			returnString = (columnName + " = :" + paramName);
		}
		return returnString;
	}

	private static String parseDateParams(Column c, String e) {
		String returnString = "";
		String searchOperation = c.getSearch().getSearchOperation().toString();
		String value = c.getSearch().getValue();
		String columnName = e.concat('.' + c.getName());
		columnName =   "Date(" + columnName + ")";
		switch (searchOperation) {
		case ("EQUAL"):
			returnString = (columnName+ " between " + (getDatesForSearchTerm(value)));
			break;
		case ("NOT_EQUAL"):
			returnString = (columnName+ " not between " +(getDatesForSearchTerm(value)));
			break;
		case ("MATCH"):
			value = ("'%" + value + "%'");
			returnString = (columnName + " LIKE " + value);
			break;
		case ("MATCH_END"):
			value = ("'%" + value + "'");
			returnString = (columnName + " LIKE :" + value);
			break;
		case ("MATCH_START"):
			value = ("'" + value + "%'");
			returnString = (columnName + " LIKE :" + value);
			break;
		case ("BETWEEN"):
			returnString = (columnName + " BETWEEN " + getDatesForSearchTerm(value.split(",")[0]).split("and")[0] + " AND " +  getDatesForSearchTerm(value.split(",")[1]).split("and")[1]);
			break;
		case ("NOT_BETWEEN"):
			returnString = (columnName + "NOT BETWEEN " + getDatesForSearchTerm(value.split(",")[0]).split("and")[0] + " AND " +  getDatesForSearchTerm(value.split(",")[1]).split("and")[1]);
			break;
		case ("GREATER_THAN"):
			returnString = (columnName + " > " + (getDatesForSearchTerm(value)).split("and")[1]);
			break;
		case ("LESS_THAN"):
			returnString = (columnName + " < " + (getDatesForSearchTerm(value)).split("and")[0]);
			break;
		case ("GREATER_THAN_EQUAL"):
			returnString = (columnName + " >= " + (getDatesForSearchTerm(value)).split("and")[1]);
			break;
		case ("LESS_THAN_EQUAL"):
			returnString = (columnName + " <= " + (getDatesForSearchTerm(value)).split("and")[0]);
			break;
		default:
			returnString = (columnName + " = " + value);
		}
		return returnString;
	}

	public static String getDatesForSearchTerm(String searchTerm) {
		LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		switch (searchTerm) {
		case ("#TODAY"):
			return "DATE('" + localDate + " 00:00:00') and DATE('" + localDate + " 23:59:59')";
		case ("#TOMMORROW"):
			return "DATE('" + localDate.plusDays(1) + " 00:00:00') and DATE('" + localDate.plusDays(1) + " 23:59:59')";
		case ("#YESTERDAY"):
			return "DATE('" + localDate.minusDays(1) + " 00:00:00') and DATE('" + localDate.minusDays(1)
					+ " 23:59:59')";
		case ("#THIS_MONTH"):
			return "DATE('"+localDate.with(TemporalAdjusters.firstDayOfMonth())+"') and DATE('"+localDate.with(TemporalAdjusters.lastDayOfMonth())+"')";
		case ("#NEXT_MONTH"):
			return "DATE('" + localDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#NEXT_3_MONTH"):
			return "DATE('" + localDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.plusMonths(3).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#NEXT_6_MONTH"):
			return "DATE('" + localDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.plusMonths(6).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#LAST_MONTH"):
			return "DATE('" + localDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#LAST_3_MONTH"):
			return "DATE('" + localDate.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#LAST_6_MONTH"):
			return "DATE('" + localDate.minusMonths(6).with(TemporalAdjusters.firstDayOfMonth()) + "') and DATE('"
					+ localDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()) + "')";
		case ("#THIS_YEAR"):
			return "Date('" + localDate.getYear() + "-01-01') and DATE('" + localDate.getYear() + "-12-31')";
		case ("#NEXT_YEAR"):
			return "Date('" + localDate.plusYears(1).getYear() + "-01-01') and DATE('"
					+ localDate.plusYears(1).getYear() + "-12-31')";
		case ("#LAST_YEAR"):
			return "Date('" + localDate.minusYears(1).getYear() + "-01-01') and DATE('"
					+ localDate.minusYears(1).getYear() + "-12-31')";
		default:
			return "Date('"+searchTerm+"  00:00:00') and Date('"+searchTerm+"  23:59:59')";

		}
	}

	private static String getOrderBy(PagingRequest pagingRequest, String prefix) {
		return prefix.concat(
				'.' + pagingRequest.getOrder().get(0).getColumn() + ' ' + pagingRequest.getOrder().get(0).getDir());
	}

	public static Query setQueryParams(List<Column> columns, Query q) {
		columns.stream().forEach(c -> {
			if (!c.getSearch().getValue().isEmpty() && !c.getType().equals("Date") && !c.getSearch().getValue().startsWith("#")) {
				String searchOperation = c.getSearch().getSearchOperation().toString();
				String value = c.getSearch().getValue();
				if (searchOperation.equals("IN") || searchOperation.equals("NOT_IN")) {
					q.setParameter(c.getData(), Arrays.stream(value.split(",")).collect(Collectors.toList()));
				} else if (searchOperation.equals("BETWEEN") || searchOperation.equals("NOT_BETWEEN")) {
					q.setParameter(c.getData() + "_BEGIN", value.split(",")[0]);
					q.setParameter(c.getData() + "_END", value.split(",")[1]);
				} else {
					q.setParameter(c.getData(), c.getType().equals("boolean") ? Boolean.parseBoolean(value) : value);
				}
			}
		});
		return q;
	}

	public static <T> String parseGroupBy(String groupBy, String groupByType, Class<T> clazz) {
		Column col = Column.createColumn("", SearchOperation.EQUAL, groupBy.replaceAll("\\.", ""), groupBy);
		validateColumns(col, clazz.getDeclaredFields());
		switch (groupByType) {
		case ("STRING"):
			return groupBy;
		case ("NUMBER"):
			return groupBy;
		case ("FLOAT"):
			return groupBy;
		case ("DATE"):
			return "DATE(" + groupBy + ")";
		case ("WEEK"):
			return "WEEK(" + groupBy + ")";
		case ("MONTH"):
			return "DATE_FORMAT(" + groupBy + ", '%Y-%m')";
		case ("YEAR"):
			return "YEAR(" + groupBy + ")";
		default:
			return groupBy;
		}
	}

	public static <T> Query parseQueryString(String query, String queryParams, Class<T> clazz, EntityManager em) {
		List<Column> columns = new ArrayList<Column>();
		StringBuilder q = new StringBuilder("WHERE e.isActive=1 ");
		if (queryParams != null && !queryParams.isEmpty()) {
			q = new StringBuilder("WHERE e.isActive=1 AND ");
			String[] andSubQuery = queryParams.split("_:AND:_");
			for (int i = 0; i <= andSubQuery.length - 1; i++) {
				StringBuilder a1 = new StringBuilder();
				String[] orSubQuery = (andSubQuery[i]).split("_:OR:_");
				for (int j = 0; j <= orSubQuery.length - 1; j++) {
					String[] q1 = (orSubQuery[j]).split("-:-");
					if (q1.length == 3) {
						Column col = Column.createColumn(q1[2], SearchOperation.valueOf(q1[1]),
								q1[0].replaceAll("\\.", "") + j + i, q1[0]);
						validateColumns(col, clazz.getDeclaredFields());
						columns.add(col);
						String queryPart = parseQueryParam(col, "e");
						if (j < orSubQuery.length - 1)
							a1.append("  ( " + queryPart + " ) OR ");
						else
							a1.append("( " + queryPart + " )");
					}
				}
				if (i < andSubQuery.length - 1)
					q.append(" ( " + a1 + " ) AND ");
				else
					q.append("( " + a1 + " )");
			}
		}
		query = query.replaceAll("#QUERYPARAMS", q.toString());
		return setQueryParams(columns, em.createQuery(query));
	}
}