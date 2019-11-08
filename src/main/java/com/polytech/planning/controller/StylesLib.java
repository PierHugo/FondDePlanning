package com.polytech.planning.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

public final class StylesLib {

	private static XSSFColor cmBg;
	private static XSSFColor tdBg;
	private static XSSFColor tpBg;
	private static XSSFColor asrBg;
	private static XSSFColor siBg;
	private static XSSFColor ccEtBg;
	private static XSSFColor dateBg;
	private static XSSFColor weekNumBg;
	private static XSSFColor holidaysBg;

	/**
	 * Default style for cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle baseStyle(XSSFWorkbook wb) {
		// Define color palette
		cmBg = new XSSFColor(new byte[] {(byte)33,(byte)150,(byte)243}, null);// new java.awt.Color(33, 150, 243, 0)); // #2196F3
		tdBg = new XSSFColor(new byte[] {(byte)139,(byte)195,(byte)74}, null);// new java.awt.Color(139, 195, 74, 0)); // #8BC34A
		tpBg = new XSSFColor(new byte[] {(byte)255,(byte)193,(byte)7}, null);// new java.awt.Color(255, 193, 7, 0)); // #FFC107
		ccEtBg = new XSSFColor(new byte[] {(byte)244,(byte)67,(byte)54}, null);// new java.awt.Color(244, 67, 54, 0)); // #F44336
		siBg = new XSSFColor(new byte[] {(byte)0,(byte)117,(byte)169}, null);// new java.awt.Color(0, 117, 169, 0));
		asrBg = new XSSFColor(new byte[] {(byte)50,(byte)117,(byte)108}, null);// new java.awt.Color(50, 117, 108, 0));

		dateBg = new XSSFColor(new byte[] {(byte)189,(byte)189,(byte)189}, null);// new java.awt.Color(189, 189, 189, 0)); // #BDBDBD
		weekNumBg = new XSSFColor(new byte[] {(byte)224,(byte)224,(byte)224}, null);// new java.awt.Color(224, 224, 224, 0)); // #E0E0E0
		holidaysBg = new XSSFColor(new byte[] {(byte)255,(byte)87,(byte)34}, null);// new java.awt.Color(255, 87, 34, 0)); // #FF5722

		// create font
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(false);
		font.setItalic(false);

		// Create cell style
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true); // Set wordwrap

		// Setting font to style
		cellStyle.setFont(font);

		return cellStyle;
	}

	/**
	 * set border style
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle baseBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		return cellStyle;
	}

	/**
	 * 
	 * set Border For Merged Cell
	 * @param wb
	 * @return
	 */
	public static void addBorderForMergedCell(Sheet sheet, int rowStart, int rowEnd, int colStart, int colEnd) {
		CellRangeAddress brodercell = new CellRangeAddress(rowStart, rowEnd, colStart, colEnd);
		setBorderForMergeCell(BorderStyle.THIN, brodercell, sheet);
	}
	
	/**
	 * used by addBorderForMergedCell()
	 * @param i
	 * @param cellRangeTitle
	 * @param sheet
	 */
	private static void setBorderForMergeCell(BorderStyle i, CellRangeAddress cellRangeTitle, Sheet sheet) {
		RegionUtil.setBorderBottom(i, cellRangeTitle, sheet);
		RegionUtil.setBorderLeft(i, cellRangeTitle, sheet);
		RegionUtil.setBorderRight(i, cellRangeTitle, sheet);
		RegionUtil.setBorderTop(i, cellRangeTitle, sheet);
	}

	/**
	 * Style for classroom type cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle cmStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(cmBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for supervised work cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle tdStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(tdBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for practical work cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle tpStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(tpBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for continuous control and terminal proof cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle ccStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(ccEtBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}
	
	/**
	 * Style for classroom type cells with border
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle cmBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseBorderStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(cmBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for supervised work cells with border
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle tdBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseBorderStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(tdBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for practical work cells with border
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle tpBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseBorderStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(tpBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for continuous control and terminal proof cells with border
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle ccBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseBorderStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(ccEtBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for cells of type number of week
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle weekNumStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(weekNumBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for date type cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle dateStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = dateFormatStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(dateBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for holiday cells
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle holidayStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(holidaysBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	/**
	 * Style for holiday cells with border
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle holidayBorderStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseBorderStyle(wb);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(holidaysBg);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}
	/**
	 * Makes bold the font of the style passed in parameter
	 * 
	 * @param cellStyle
	 *            style on which to put the type in bold
	 * @return
	 */
	public static XSSFCellStyle setBold(XSSFCellStyle cellStyle) {
		Font font = cellStyle.getFont();
		font.setBold(true);

		cellStyle.setFont(font);

		return cellStyle;
	}

	public static XSSFCellStyle dateFormatStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		XSSFDataFormat df = wb.createDataFormat();
		cellStyle.setDataFormat(df.getFormat("dd/m/yyyy"));
		return cellStyle;
	}

	/**
	 * set merge
	 * 
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static int setCellMerge(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		return sheet.addMergedRegion(cellRangeAddress);
	}

	/**
	 * set the gap (which is between two teaching units) style
	 * 
	 * @param wb
	 * @return
	 */
	public static XSSFCellStyle gapStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = baseStyle(wb);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		return cellStyle;
	}

	/**
	 * 
	 * @param sheet
	 * @param column
	 * @return
	 */
	public static Sheet columTitleWidth(Sheet sheet, int column) {
		sheet.setColumnWidth(column, 32 * 256);
		return sheet;
	}
}
