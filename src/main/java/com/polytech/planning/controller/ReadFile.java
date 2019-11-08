package com.polytech.planning.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadFile {

	private static final Logger LOG = LoggerFactory.getLogger(ReadFile.class);
	private XSSFWorkbook wb;
	private File file;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            full path of the file to read
	 */
	public ReadFile(String filePath) throws IOException {
		this.file = new File(filePath);
		File f = new File(".");
		InputStream st = new FileInputStream(this.file);
		this.wb = new XSSFWorkbook(st);
	}

	/**
	 * To get sheet name
	 * 
	 * @param sheetNum
	 *            Number of the sheet
	 * @return the name of the sheet
	 * @throws Exception
	 */
	public String getSheetName(int sheetNum) throws Exception {
		if (this.wb.getSheetAt(sheetNum) != null) {
			return this.wb.getSheetName(sheetNum);
		} else {
			throw new Exception("Cet onglet n'existe pas. (" + sheetNum + 1 + ")");
		}
	}

	/**
	 * To read a string cell
	 * 
	 * @param rowNum
	 *            Number of row to be readed
	 * @param colNum
	 *            Number of column to be readed
	 * @param sheetNum
	 *            Number of the sheet to be readed
	 */
	protected String readCell(int rowNum, int colNum, int sheetNum) {
		try {
			DataFormatter formatter = new DataFormatter();

			Sheet sheet = wb.getSheetAt(sheetNum);
			Row row = sheet.getRow(rowNum);

			Cell cell = row.getCell(colNum);
			// Cell nestCell = sheet.getRow(rowNum + 1).getCell(colNum);
			// System.out.println("row " + (rowNum + 1) + ",type: " +
			// nestCell.getCellTypeEnum().toString());
			String cellContent;
			if (cell != null) {
				cellContent = formatter.formatCellValue(cell);
			} else {
				cellContent = "";
				//System.out.println("\t<$>WARN : Problème de lecture de cellule. (f:" + sheetNum + ",col:" + colNum + ",li:" + rowNum + ") La cellule a été ignorée.");
				LOG.info("Problème de lecture de cellule. (f:" + sheetNum + ",col:" + colNum + ",li:" + rowNum + ") La cellule a été ignorée.");
				/*cellContent = null;
				Scanner sc = new Scanner(System.in);
				String answr = "";

				System.out.println("<!> Le programme a rencontré une cellule qu'il n'arrive pas à lire (feuille:" + sheetNum + ",col:" + colNum + ",ligne:" + rowNum + ").");
				while(!answr.equalsIgnoreCase("o") && !answr.equalsIgnoreCase("n")) {
					System.out.print("<?> Voulez-vous passer outre ? <O> pour Oui - <N> pour Non : ");
					answr = sc.nextLine();
				}

				if(answr.equalsIgnoreCase("n"))
					throw new NullPointerException("Impossible de lire la cellule (feuille:" + sheetNum + ",col:" + colNum + ",ligne:" + rowNum + "). Si la cellule est censée être vide, tentez d'effacer le formattage de ladite cellule.");

				if(answr.equalsIgnoreCase("o"))
					cellContent = "";*/
			}

			return cellContent;

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * To read a numeric cell
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param sheetNum
	 * @return
	 */
	protected Double readNumericCell(int rowNum, int colNum, int sheetNum) {
		try {

			Sheet sheet = wb.getSheetAt(sheetNum);
			Row row = sheet.getRow(rowNum);

			Cell cell = row.getCell(colNum);

			Double cellContent = null;
			if (cell.getCellTypeEnum() == CellType.BLANK) {
				cellContent = 0.0;
				return cellContent;
			}
			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				cellContent = cell.getNumericCellValue();
				return cellContent;
			} else {
				throw new NumberFormatException("La cellule ligne " + rowNum + " / colonne " + colNum + " de l'onglet "
						+ sheetNum + ", est de type " + getCellType(rowNum, colNum, sheetNum));
			}

		} catch (NumberFormatException e) {
			//System.out.println("\t<$>WARN : " + e.getMessage());
			LOG.warn(e.getMessage());
			// e.printStackTrace();
		}

		return null;
	}

	/**
	 * To read a cell with type date
	 * 
	 * @param rowNum
	 *            Number of row to be readed
	 * @param colNum
	 *            Number of column to be readed
	 * @param sheetNum
	 *            Number of the sheet to be readed
	 */
	protected Date readCellDate(int rowNum, int colNum, int sheetNum) {
		try {

			Sheet sheet = wb.getSheetAt(sheetNum);
			Row row = sheet.getRow(rowNum);

			Cell cell = row.getCell(colNum);

			Date cellContent = null;

			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				if (cell != null) {
					cellContent = cell.getDateCellValue();
					return cellContent;
				}
			} else {
				throw new Exception("La cellule n'est pas une date");
			}

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * To read a row
	 * 
	 * @param rowNb
	 *            Number of row to be readed
	 * @param sheetNb
	 *            Number of the sheet to be readed
	 */
	protected List<String> readRow(int rowNb, int sheetNb) {
		List<String> readingValues = new ArrayList<String>();
		int lastCell;
		int numberCell;

		// Sheet, row and cell where read the content
		Sheet sheet = wb.getSheetAt(sheetNb);
		Row row = sheet.getRow(rowNb);

		try {
			if (rowIsEmpty(rowNb, sheetNb))
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

		numberCell = row.getFirstCellNum();
		lastCell = row.getLastCellNum();

		while (numberCell <= lastCell) {
			readingValues.add(readCell(rowNb, numberCell, sheetNb));
			numberCell++;
		}

		return readingValues;
	}

	/**
	 * To know if a row id empty
	 * 
	 * @param rowNb
	 *            Number of row to be readed
	 * @param sheetNb
	 *            Number of the sheet to be readed
	 */
	protected boolean rowIsEmpty(int rowNb, int sheetNb) throws NullPointerException {
		Sheet sheet = wb.getSheetAt(sheetNb);
		Row row = sheet.getRow(rowNb);

		if (sheet.equals(null)) {
			throw new NullPointerException("L'onglet " + sheetNb + " n'existe pas");
		} else {
			if (row == null)
				return true;

			if (row.getLastCellNum() <= 0)
				return true;

			for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !cell.toString().isEmpty())
					return false;
			}

			return true;
		}
	}

	/**
	 * To know if a cell is empty
	 * 
	 * @param colNum
	 *            Number of column to be readed
	 * @param rowNum
	 *            Number of row to be readed
	 * @param sheetNum
	 *            Number of the sheet to be readed
	 */
	protected boolean cellIsEmpty(int rowNum, int colNum, int sheetNum) throws NullPointerException {
		Sheet sheet = wb.getSheetAt(sheetNum);

		if (sheet.equals(null)) {
			throw new NullPointerException("L'onglet " + sheetNum + " n'existe pas");
		} else {

			CellType type = CellType.BLANK;

			if (getCellType(rowNum, colNum, sheetNum).equals(type)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * To know if a cell is numeric type or not
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param sheetNum
	 * @return
	 * @throws NullPointerException
	 */
	protected boolean cellIsNumeric(int rowNum, int colNum, int sheetNum) throws NullPointerException {
		Sheet sheet = wb.getSheetAt(sheetNum);
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		CellType numeric = CellType.NUMERIC;

		if (cell.getCellTypeEnum().equals(numeric)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To know if a cell is string type or not
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param sheetNum
	 * @return
	 * @throws NullPointerException
	 */
	protected boolean cellIsString(int rowNum, int colNum, int sheetNum) throws NullPointerException {
		Sheet sheet = wb.getSheetAt(sheetNum);
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		CellType string = CellType.STRING;

		if (cell.getCellTypeEnum().equals(string)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To know the cell type
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param sheetNum
	 * @return
	 */
	protected CellType getCellType(int rowNum, int colNum, int sheetNum) {
		Sheet sheet = wb.getSheetAt(sheetNum);
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);

		// Verify if the cell read isn't null
		if(cell == null) {
            return CellType.BLANK;
        }
            return cell.getCellTypeEnum();
	}

	/**
	 * Get the coordonates of first cell not empty
	 * 
	 * @param sheetNb
	 *            Number of the sheet to be readed
	 * @return A tqble with two values, the first the row number and the second the
	 *         column number of the first non-empty cell
	 */
	public int[] getFirstCellNotEmpty(int sheetNb) {
		int[] coordonates = new int[2];

		Sheet sheet = wb.getSheetAt(sheetNb);
		int lineNum = 0;
		Row row = sheet.getRow(lineNum);

		if (sheet.equals(null))
			throw new NullPointerException("L'onglet " + sheetNb + " n'existe pas");

		while (row == null) {
			row = sheet.getRow(lineNum);
			lineNum++;
		}

		coordonates[0] = lineNum;
		coordonates[1] = row.getFirstCellNum();

		return coordonates;
	}

	/**
	 * Get the coordonates of cell who contain the search content
	 * 
	 * @param sheetNb
	 *            Number of the sheet to be readed
	 * @param content
	 *            the search String
	 * @return A table with two values, the first the row number and the second the
	 *         column number of the first content find
	 */
	public int[] searchContent(int sheetNb, String content, boolean startWith) throws NullPointerException, IllegalArgumentException {
		int[] coordonates = new int[2];
		String buffer;
		coordonates[0] = -1;
		coordonates[1] = -1;

		content = normalizeText(content);

		Sheet sheet = null;

		try {
			sheet = wb.getSheetAt(sheetNb);
		} catch (IllegalArgumentException e) {
			System.out.println("ERREUR : La génération du fond de planning ne peut etre effective. Il manque des feuilles dans le fichier Maquette.");

			//Exception exc = new Exception("ERREUR : La génération du fond de planning ne peut etre effective. Il manque des feuilles dans le fichier Maquette.");
		}

		if (sheet == null) {
			throw new NullPointerException("L'onglet " + sheetNb + " n'existe pas");
		}

		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellTypeEnum() == CellType.STRING) {
					buffer = cell.getRichStringCellValue().getString().trim().toUpperCase();
					buffer = normalizeText(buffer);
					if (startWith) {
						if (buffer.startsWith(content)) {
							coordonates[0] = cell.getRowIndex();
							coordonates[1] = cell.getColumnIndex();
						}
					} else {
						if (buffer.equals(content)) {
							coordonates[0] = cell.getRowIndex();
							coordonates[1] = cell.getColumnIndex();
						}
					}

				}
			}
		}
		return coordonates;
	}

	/**
	 * Put the text in lower case and without accents
	 * 
	 * @param input
	 *            string to normalize
	 * @return the string normalizes
	 */
	protected String normalizeText(String input) {
		//TODO : MODIF here ajout du if
		if(input != null) {
			input = input.toLowerCase();
			input = Normalizer.normalize(input, Normalizer.Form.NFD);
			input = input.replaceAll("[^\\p{ASCII}]", "");

		}
		return input;
	}
}
