package com.softtek.assetworx_api.serviceImpl;

import static com.softtek.assetworx_api.util.Constants.REPORT_ERROR_MESSAGE;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softtek.assetworx_api.dao.ReportDao;
import com.softtek.assetworx_api.entity.AssetworxUser;
import com.softtek.assetworx_api.entity.Report;
import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.repository.ReportRepository;
import com.softtek.assetworx_api.service.FloorService;
import com.softtek.assetworx_api.service.ReportService;
import com.softtek.assetworx_api.service.SeatAllHistoryService;
import com.softtek.assetworx_api.service.UserService;
import com.softtek.assetworx_api.util.Constants;
import com.softtek.assetworx_api.util.DBUtil;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	ReportDao reportDao;

	@Autowired
	UserService userService;

	@Autowired
	FloorService floorService;

	@Autowired
	SeatAllHistoryService seatAllHistoryService;

	@Autowired
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> getReportData(Report report) {
		String entity = report.getEntity();
		String queryString = ("SELECT #GROUPBY, count(id) FROM #ENTITY e  #QUERYPARAMS GROUP BY #GROUPBY");
		String groupBy = report.getGroupBy();
		if (report.getType().equalsIgnoreCase("list")) {
			queryString = ("SELECT e FROM #ENTITY e  #QUERYPARAMS ");
			groupBy = "id";
		} else if (report.getType().equalsIgnoreCase("single-score")) {
			groupBy = "id";
		}
		try {
			Class<T> clazz = (Class<T>) Class.forName("com.softtek.assetworx_api.entity." + entity);
			Object obj = clazz.newInstance();
			groupBy = DBUtil.parseGroupBy(groupBy, report.getGroupByType(), obj.getClass());
			queryString = queryString.replace("#GROUPBY", groupBy);
			queryString = queryString.replace("#ENTITY", entity);
			System.out.println(reportDao.getReportData(queryString, report, obj.getClass()));
			return reportDao.getReportData(queryString, report, obj.getClass());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericRestException(REPORT_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public Report save(Report report) {
		report.setId("");
		return reportRepository.save(report);
	}

	@Override
	public Report save(Report report, String userName) {
		report.setId("");
		AssetworxUser user = userService.findByUserName(userName);
		Report savedReport = reportRepository.save(report);
		user.getUserReports().add(savedReport);
		userService.save(user);
		return savedReport;
	}

	@Override
	public Report update(Report report) {
		Report r = findById(report.getId());
		r.setName(report.getName());
		r.setEntity(report.getEntity());
		r.setGroupBy(report.getGroupBy());
		r.setGroupByType(report.getGroupByType());
		r.setQuery(report.getQuery());
		r.setType(report.getType());
		return reportRepository.save(r);
	}

	@Override
	public Report findById(String id) {
		return reportRepository.findById(id).orElseGet(null);
	}

	@Override
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	@Override
	public int shareReportToUsers(String reportId, List<String> toUserNames) {
		List<AssetworxUser> users = userService.findByUserNameIn(toUserNames);
		Report report = findById(reportId);
		if (users.size() <= 0 || report == null) {
			throw new GenericRestException("User report could not be Shared.", HttpStatus.BAD_REQUEST);
		}
		int updatedUsersCount = 0;
		for (AssetworxUser user : users) {
			System.out.println("sharing to : " + user.getUserName());
			if (user.getSharedReports().add(report)) {
				updatedUsersCount++;
			}
		}
		userService.save(users);
		return updatedUsersCount;
	}

	@Override
	public int shareReportToRoles(String reportId, List<String> roles) {
		int updatedUsersCount = 0;
		List<AssetworxUser> allusers = userService.findUsersByRoleIn(roles);
		Report sharedReport = findById(reportId);
		for (AssetworxUser user : allusers) {
			if (user.getSharedReports().add(sharedReport)) {
				System.out.println("sharing to : " + user.getUserName());
				updatedUsersCount++;
				userService.save(user);
			}
		}
		return updatedUsersCount;
	}

	@Override
	public int removeReportFromUsers(List<String> userNames, String reportId, String type) {
		List<AssetworxUser> users = userService.findByUserNameIn(userNames);
		Report report = findById(reportId);
		int updatedCount = 0;
		if (users.size() <= 0 || report == null) {
			throw new GenericRestException("User Dashboard could not be removed.", HttpStatus.BAD_REQUEST);
		}
		for (AssetworxUser user : users) {
			if (type.equals(Constants.REPORT_USER)) {
				if (user.getUserReports().remove(report)) {
					updatedCount++;
					userService.save(users);
				}
			} else if (type.equals(Constants.REPORT_SHARED)) {
				if (user.getSharedReports().remove(report)) {
					updatedCount++;
					userService.save(users);
				}
			}
		}
		return updatedCount;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public int removeReportFromRoles(List<String> roles, String dashBoardId) {
		int updatedCount = 0;
		List<AssetworxUser> users = userService.findUserWithSharedReportIdAndRoleIdIn(roles, dashBoardId);

		Report report = findById(dashBoardId);

		if (users.size() <= 0 || report == null) {
			throw new GenericRestException("User Dashboard could not be removed.", HttpStatus.BAD_REQUEST);
		}
		for (AssetworxUser user : users) {
			System.out.println("user : +++++  >  " + user);
			updatedCount++;
			user.getSharedReports().remove(report);
		}
		// userService.save(users);
		return updatedCount;
	}

	@Override
	public Map<String, Set<Report>> findAllUsersReportsByUserName(String userName) {
		AssetworxUser user = userService.findByUserName(userName);
		if (user == null) {
			throw new GenericRestException("User Dashboard could not be Found.", HttpStatus.BAD_REQUEST);
		}
		Map<String, Set<Report>> data = new HashedMap<>();
		user.getSharedReports().removeAll(user.getUserReports());
		data.put("sharedReports", user.getSharedReports());
		data.put("userReport", user.getUserReports());
		return data;
	}

	@SuppressWarnings("unchecked")
	public Collection<Object[]> getFloorReport(String date) {
		/*
		 * please check the date format here
		 * it should be yyyy-MM-dd
		 */
		Date queryDate = createDateFromString(date);
		Date currentDate = new Date();

		if (isAfter(queryDate, currentDate)) {
			System.out.println("Date after");
			return null;
		} else if (isBefore(queryDate, currentDate)) {
			System.out.println("Date before");
			/*
			 * fetch the resutl from SeatallReportHistory for the Given date and and
			 * reportType
			 */
			Collection<Object[]> floorHistoryList = seatAllHistoryService.getByReportNameAndForDate("floor-report",
					queryDate);

			if (floorHistoryList == null || floorHistoryList.size() == 0) {
				return null;
			}
			return floorHistoryList;
		}
		// Floor workstation & cabin capacity count
		else if (date == null || date.isEmpty()) {
			String qyery1 = "select a.floor.name, a.areaType.name,count(a.id) from Area a "
					+ "where a.areaType.name in ('Workstation', 'Cabin') and a.isActive=1 group by a.floor.name, a.areaType.name ";
			Map<String, Object[]> map = new HashMap<String, Object[]>();
			List<Object[]> list = (List<Object[]>) em.createQuery(qyery1).getResultList();
			for (Object[] o : list) {
				map.put((String) o[0], new Object[7]);
			}
			map = parseFloorReport(list, map, 1);
			// Floor workstation & cabin occupied count
			qyery1 = "select w.area.floor.name, w.area.areaType.name,count(w.id) from Workstation w "
					+ "where w.area.areaType.name in ('Workstation', 'Cabin') and w.isActive=1 and w.isOccupied=1 group by w.area.floor.name, w.area.areaType.name";
			// Floor workstation & cabin shared count
			map = parseFloorReport(em.createQuery(qyery1).getResultList(), map, 3);
			qyery1 = "select w.area.floor.name, w.area.areaType.name,count(w.id) from Workstation w "
					+ "where w.area.areaType.name in ('Workstation', 'Cabin') and w.isActive=1 and w.isShared=1 group by w.area.floor.name, w.area.areaType.name";
			map = parseFloorReport(em.createQuery(qyery1).getResultList(), map, 5);
			return map.values();
		}

		return null;
	}

	private Map<String, Object[]> parseFloorReport(List<Object[]> list, Map<String, Object[]> map, int index) {
		for (Object[] o : list) {
			map.get((String) o[0])[0] = o[0];
			map.get((String) o[0])[(o[1].toString().equalsIgnoreCase("WORKSTATION")) ? index : index + 1] = o[2];
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getDepartmentReport(String date) {

		System.out.println("------------------------------------");
		System.out.println("date : " + date);
		/*
		 * please check the date format here
		 */
		Date queryDate = createDateFromString(date);
		Date currentDate = new Date();
		if (isAfter(queryDate, currentDate)) {
			return null;
		} else if (isBefore(queryDate, currentDate)) {
			/*
			 * fetch the resutl from SeatallReportHistory for the Given date and and
			 * reportType
			 */
			Collection<Object[]> departmentHistoryList = seatAllHistoryService
					.getByReportNameAndForDate("department-report", queryDate);

			if (departmentHistoryList == null || departmentHistoryList.size() == 0) {
				return null;
			}
			return (List) departmentHistoryList;
		} else if (date == null || date.isEmpty()) {

			/*
			 * String qyery1 =
			 * "select w.employee.department.name, count(w.id) from WorkstationAllocation w "
			 * + "where w.isActive=1 and w.isAllocated=1 group by w.employee.department";
			 */
			String qyery1 = "select e.department.name, count(e.id) from Employee e "
					+ "where e.isActive=1 and e.employmentStatus=1 group by e.department.name";
			return em.createQuery(qyery1).getResultList();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Object[]> getCDSReport(String date) {

		/*
		 * please check the date format here
		 */
		Date queryDate = createDateFromString(date);
		Date currentDate = new Date();

		if (isAfter(queryDate, currentDate)) {
			return null;
		} else if (isBefore(queryDate, currentDate)) {
			/*
			 * fetch the resutl from SeatallReportHistory for the Given date and and
			 * reportType
			 */
			Collection<Object[]> cdsHistoryReport = seatAllHistoryService.getByReportNameAndForDate("cds-report",
					queryDate);
			if (cdsHistoryReport == null || cdsHistoryReport.size() == 0) {
				return null;
			}
			return cdsHistoryReport;
		} else if (date == null || date.isEmpty()) {
			System.out.println("In here");
			// CDS workstation & cabin capacity count
			String qyery1 = "select a.floor.name, a.parentArea.name, count(a.id) from Area a "
					+ "where a.areaType.name in ('Workstation', 'Cabin') and a.isActive=1 group by a.floor.name, a.parentArea";
			Map<String, Object[]> map = new HashMap<String, Object[]>();
			List<Object[]> list = (List<Object[]>) em.createQuery(qyery1).getResultList();
			for (Object[] o : list) {
				map.put((String) o[1], new Object[5]);
			}
			map = parseCDSReport(list, map, 2);
			// CDS workstation & cabin occupied count
			qyery1 = "select w.area.floor.name, w.area.parentArea.name,count(w.id) from Workstation w "
					+ "where w.area.areaType.name in ('Workstation', 'Cabin') and w.isActive=1 and w.isOccupied=1 group by w.area.floor.name, w.area.parentArea.name";
			// CDS workstation & cabin shared count
			map = parseCDSReport(em.createQuery(qyery1).getResultList(), map, 3);
			qyery1 = "select w.area.floor.name, w.area.parentArea.name,count(w.id) from Workstation w "
					+ "where w.area.areaType.name in ('Workstation', 'Cabin') and w.isActive=1 and w.isShared=1 group by w.area.floor.name, w.area.parentArea.name";
			map = parseCDSReport(em.createQuery(qyery1).getResultList(), map, 4);
			return map.values();
		}
		return null;
	}

	private Map<String, Object[]> parseCDSReport(List<Object[]> list, Map<String, Object[]> map, int index) {
		for (Object[] o : list) {
			map.get((String) o[1])[0] = o[0];
			map.get((String) o[1])[1] = o[1];
			map.get((String) o[1])[index] = o[2];
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUnallotedEmployeeReport() {
		String qyery1 = "select e from Employee e " + "where e.isActive=1 and e.employmentStatus=1 and e.id not in "
				+ "(select w.employee.id from WorkstationAllocation w where w.isAllocated=1 and w.isActive=1)";
		return em.createQuery(qyery1).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getEmployeeLocationHistory(String id) {
		String qyery1 = "select w from WorkstationAllocation w where w.isActive=1 and w.employee.id= :id order by w.lastUpdated desc";

		return em.createQuery(qyery1).setParameter("id", id).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getSeperationReport() {
		String qyery1 = "select e from Employee e where e.isActive=0 and e.employmentStatus=0";
		return em.createQuery(qyery1).getResultList();
	}

	@Override
	public Object getSeatallReport(String reportName, String reportParam) {
		switch (reportName) {
		case ("floor-report"):
			return getFloorReport(reportParam);
		case ("department-report"):
			return getDepartmentReport(reportParam);
		case ("cds-report"):
			return getCDSReport(reportParam);
		case ("unalloted-employees"):
			return getUnallotedEmployeeReport();
		case ("seperation-report"):
			return getSeperationReport();
		case ("employee-location-history"):
			return getEmployeeLocationHistory(reportParam);
		}
		return null;
	}

	private Date createDateFromString(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);  
		} catch(Exception e) {
			return new Date();
		}
	}

	/*
	 * compare the date in format yyyy-MM-dd without the time
	 */
	private boolean isAfter(Date compareFrom, Date compareTo) {
		if (compareFrom == null || compareTo == null) {
			return false;
		}
		LocalDate comFrom = dateToLocalDate(compareFrom);
		LocalDate comTo = dateToLocalDate(compareTo);
		System.out.println(compareFrom +"::"+compareTo+"::"+comFrom.isAfter(comTo));
		return comFrom.isAfter(comTo);
	}

	private boolean isBefore(Date compareFrom, Date compareTo) {
		if (compareFrom == null || compareTo == null) {
			return false;
		}
		LocalDate comFrom = dateToLocalDate(compareFrom);
		LocalDate comTo = dateToLocalDate(compareTo);
		System.out.println(compareFrom +"::"+compareTo+"::"+comFrom.isBefore(comTo));
		return comFrom.isBefore(comTo);
	}

//	private boolean isEqual(Date compareFrom, Date compareTo) {
//		if (compareFrom == null || compareTo == null) {
//			return false;
//		}
//		LocalDate comFrom = dateToLocalDate(compareFrom);
//		LocalDate comTo = dateToLocalDate(compareTo);
//		return comFrom.equals(comTo);
//	}

	private LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
