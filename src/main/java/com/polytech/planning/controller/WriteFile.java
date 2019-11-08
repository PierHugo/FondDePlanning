package com.polytech.planning.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

public class WriteFile {

	private Workbook workbook;
	private String filePath;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 */
	public WriteFile(String filePath) {
		this.filePath = filePath;

		// si file finit par xls ou xlsx
		if (filePath.substring(filePath.length() - 4).equals(".xls")) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook();
		}
	}

	/**
	 * @return the workbook
	 */
	public Workbook getWorkbook() {
		return workbook;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * 
	 * @param startCell
	 * @param endCell
	 * @param coordonates
	 * @param sheet
	 */
	public Cell writeSumFormula(int[] startCell, int[] endCell, int[] coordonates, Sheet sheet) throws InvalidValue {

		System.out.println(startCell[0] + "-" + startCell[1]);
		System.out.println(endCell[0] + "-" + endCell[1]);
		System.out.println(coordonates[0] + "-" + coordonates[1]);
		// Convert column value to letter
		String colStartString = ToolBox.getColLetter(startCell[1]);
		String colEndString = ToolBox.getColLetter(endCell[1]);

		String formula = "SUM(" + colStartString + ++startCell[0] + ":" + colEndString + ++endCell[0] + ")"; // SUM(colStartrowStart:colEndrowEnd)

		Row row = createRow(coordonates, sheet);
		Cell cell = createCell(coordonates, row, sheet);

		cell.setCellType(CellType.FORMULA);
		cell.setCellFormula(formula);

		return cell;
	}

	/**
	 * 
	 * @param formula
	 * @param rowNum
	 * @param colNum
	 * @param sheet
	 */
	public Cell writeFormula(String formula, int rowNum, int colNum, Sheet sheet) {
		int[] coordonates = { rowNum, colNum };

		Row row = createRow(coordonates, sheet);
		Cell cell = createCell(coordonates, row, sheet);

		cell.setCellType(CellType.FORMULA);
		cell.setCellFormula(formula);
		
		return cell;
	}

	/**
	 * 
	 * @param coordonates
	 * @param sheet
	 * @param content
	 * @return
	 */
	public Cell writeNumberCell(int[] coordonates, Sheet sheet, float content) {
		Row row = createRow(coordonates, sheet);
		Cell cell = createCell(coordonates, row, sheet);

		cell.setCellValue(content);

		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param sheet
	 * @param content
	 * @return
	 */
	public Cell writeNumberCell(int row, int col, Sheet sheet, float content) {
		int[] coordonates = { row, col };
		return writeNumberCell(coordonates, sheet, content);
	}

	/**
	 * 
	 * @param coordonates
	 * @param sheet
	 * @param content
	 * @return
	 */
	public Cell writeStringCell(int[] coordonates, Sheet sheet, String content) {
		Row row = createRow(coordonates, sheet);
		Cell cell = createCell(coordonates, row, sheet);

		cell.setCellValue(content);

		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param sheet
	 * @param content
	 * @return
	 */
	public Cell writeStringCell(int row, int col, Sheet sheet, String content) {
		int[] coordonates = { row, col };
		return writeStringCell(coordonates, sheet, content);
	}

	/**
	 * 
	 * @param coordonates
	 * @param sheet
	 * @return
	 */
	private Row createRow(int[] coordonates, Sheet sheet) {
		Row row = null;
		if (sheet.getRow(coordonates[0]) != null) {
			row = sheet.getRow(coordonates[0]);
		} else {
			row = sheet.createRow(coordonates[0]);
		}
		return row;
	}

	/**
	 * 
	 * @param coordonates
	 * @param row
	 * @param sheet
	 * @return
	 */
	private Cell createCell(int[] coordonates, Row row, Sheet sheet) {
		Cell cell = null;
		if (row.getCell(coordonates[1]) != null) {
			cell = row.getCell(coordonates[1]);
		} else {
			cell = row.createCell(coordonates[1]);
		}
		return cell;
	}

	/**
	 * 
	 */
	public void generateFile() throws IOException {
		FileOutputStream output;
		output = new FileOutputStream(filePath);
		workbook.write(output);
		output.close();
	}

}
