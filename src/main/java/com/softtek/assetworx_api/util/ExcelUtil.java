package com.softtek.assetworx_api.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {

	public static CellStyle getCellStyle(Workbook workbook,String color) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(colorIndex(color));
		Font font = workbook.createFont();
		font.setColor(colorIndex("WHITE"));
		cellStyle.setFont(font);
		return cellStyle;
	}

	private static short colorIndex(String color) {
		switch(color) {
		case("GREEN"):
			return IndexedColors.GREEN.getIndex();
		case("RED"):
			return IndexedColors.RED.getIndex();
		case("WHITE"):
			return IndexedColors.WHITE.getIndex();
		case("BROWN"):
			return IndexedColors.BROWN.getIndex();
		default:
			return IndexedColors.BLACK.getIndex();
		}

	}

}
