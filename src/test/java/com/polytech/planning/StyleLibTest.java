package com.polytech.planning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;

import com.polytech.planning.controller.StylesLib;

public class StyleLibTest {
	private static String path;
	private static XSSFWorkbook workbook;

	@BeforeClass
	public static void init() {
		path = "TestStyles.xlsx";
		workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Test de styles");
		Row row = sheet1.createRow(0);
		Cell cell = row.createCell(0);
		Cell cellB = row.createCell(2);

		Row rowCM = sheet1.createRow(1);
		Cell cellCM = rowCM.createCell(0);

		Row rowTD = sheet1.createRow(2);
		Cell cellTD = rowTD.createCell(0);

		Row rowTP = sheet1.createRow(3);
		Cell cellTP = rowTP.createCell(0);

		Row rowCC = sheet1.createRow(4);
		Cell cellCC = rowCC.createCell(0);

		Cell cellWeekNum = rowTD.createCell(2);
		Cell cellDate = rowCM.createCell(2);
		Cell cellHoliday = rowCC.createCell(2);

		sheet1.setColumnWidth(0, 24 * 200);
		sheet1.setColumnWidth(2, 24 * 200);
		row.setHeightInPoints(24);
		rowCM.setHeightInPoints(24);
		rowTD.setHeightInPoints(24);
		rowTP.setHeightInPoints(24);
		rowCC.setHeightInPoints(24);

		cell.setCellValue("Style de base");
		cell.setCellStyle(StylesLib.baseStyle(workbook));

		cellB.setCellValue("Style bold");
		cellB.setCellStyle(StylesLib.setBold(StylesLib.baseStyle(workbook)));

		cellCM.setCellValue("Style CM");
		cellCM.setCellStyle(StylesLib.cmStyle(workbook));

		cellTD.setCellValue("Style TD");
		cellTD.setCellStyle(StylesLib.tdStyle(workbook));

		cellTP.setCellValue("Style TP");
		cellTP.setCellStyle(StylesLib.tpStyle(workbook));

		cellCC.setCellValue("Style CC");
		cellCC.setCellStyle(StylesLib.ccStyle(workbook));

		cellWeekNum.setCellValue("Style Week Num");
		cellWeekNum.setCellStyle(StylesLib.weekNumStyle(workbook));

		cellDate.setCellValue("Style Date");
		cellDate.setCellStyle(StylesLib.dateStyle(workbook));

		cellHoliday.setCellValue("Style Holiday");
		cellHoliday.setCellStyle(StylesLib.holidayStyle(workbook));

		Row mergedRow = sheet1.createRow(9);
		Cell mergedCell = mergedRow.createCell(1);
		mergedCell.setCellValue("merged");
		StylesLib.setCellMerge(sheet1, 9, 10, 1, 1);

		Row gap = sheet1.createRow(11);
		gap.setRowStyle(StylesLib.gapStyle(workbook));

		FileOutputStream output;
		try {
			output = new FileOutputStream(path);
			workbook.write(output);
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
