package com.polytech.planning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

import com.polytech.planning.controller.ParserMockUp;
import com.polytech.planning.controller.ReadMockUp;
import com.polytech.planning.controller.StylesLib;
import com.polytech.planning.model.Course;
import com.polytech.planning.model.Teacher;
import com.polytech.planning.model.TeachingUnit;

/**
 * Unit test for simple App.
 */
public class ParserMockUpTest {

	@SuppressWarnings("unused")
	private static String inputTeachers;
	private static List<Teacher> expected;
	private static Teacher teacherOne;
	private static ReadMockUp readMockUp;
	private static String path;
	private static XSSFWorkbook workbook;
	private static Sheet sheet;

	@BeforeClass
	public static void init() throws IOException {
		path = "TestParser.xlsx";
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Test");
		Row header = sheet.createRow(1);
		Cell headerCell = header.createCell(1);
		headerCell.setCellValue("Teachning unit name");

		headerCell = header.createCell(2);
		headerCell.setCellValue("Course name");

		headerCell = header.createCell(3);
		headerCell.setCellValue("CM");
		headerCell = header.createCell(4);
		headerCell.setCellValue("TD");
		headerCell = header.createCell(5);
		headerCell.setCellValue("TP");
		headerCell = header.createCell(6);
		headerCell.setCellValue("Project");

		headerCell = header.createCell(7);
		headerCell.setCellValue("CC");

		headerCell = header.createCell(8);
		headerCell.setCellValue("CT");

		headerCell = header.createCell(9);
		headerCell.setCellValue("TD Mundus");
		headerCell = header.createCell(10);
		headerCell.setCellValue("TP Mundus");

		headerCell = header.createCell(11);
		headerCell.setCellValue("Teachers");

		readMockUp = new ReadMockUp("Maquette.xlsx", 1);
		inputTeachers = "H. Cardot, 8hCM, 2hTD, 0hTP*3gr + Mundus; R. Ravaux, 12hCM, 4hTD Mundus, 14hTP x3gr + 20hTP Mundus ; Mostapha Darwich, 4hTD, 6hTP x3gr";
		expected = new ArrayList<Teacher>();

		teacherOne = new Teacher("C.Tacquard");
		teacherOne.setHoursCM(16);
		teacherOne.setHoursTD(20);
		teacherOne.setHoursTP(16);
//		teacherOne.setTDMundus(true);
//		teacherOne.setTPMundus(true);

		expected.add(teacherOne);

		Teacher teacher2 = new Teacher("M. Martineau");
		teacher2.setHoursTP(12);
//		teacher2.setTPMundus(true);

		expected.add(teacher2);

	}

/*	@Test
	public void testParseTeachers() {
		ParserMockUp parser = new ParserMockUp();
		List<Teacher> output = parser.createTeachers(inputTeachers);

		Iterator<Teacher> outputIt = output.iterator();
		Iterator<Teacher> expectedIt = expected.iterator();

		//Assert.assertEquals(2, output.size());

		while (outputIt.hasNext()) {
			//Assert.assertTrue(outputIt.next().equals(expectedIt.next()));
			Teacher t = outputIt.next();
			System.out.println(t.getName());
			System.out.println("cm "+t.getHoursCM());
			System.out.println("td "+t.getHoursTD());
			System.out.println("tp "+t.getHoursTP());
			System.out.println("mundus td "+t.getTDMundus());
			System.out.println("mundus tp "+t.getTPMundus());
		}
	}
*/
	@Test
	public void testWithReadFile() {

		readMockUp.readTeachingUnits();
		ParserMockUp parser = new ParserMockUp(readMockUp.getTeachingUnits());
		List<TeachingUnit> teachingUnits = parser.createTeachingUnits();
		int teachingUnitStartRow = 2;
		int courseStartRow = 2;
		int teacherEndRow = 2;
		for (TeachingUnit teachingUnit : teachingUnits) {

			for (Course course : teachingUnit.getListCourses()) {

				for (Teacher teacher : course.getListTeachers()) {

					Row row = sheet.createRow(teacherEndRow);
					
					Cell cell = row.createCell(1);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teachingUnit.getName());
					cell = row.createCell(2);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(course.getName());
					cell = row.createCell(3);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getHoursCM());
					cell = row.createCell(4);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getHoursTD());
					cell = row.createCell(5);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getHoursTP());
					cell = row.createCell(6);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(course.getTotalProject());
					cell = row.createCell(7);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(course.hasCc() ? 1 : 0);
					cell = row.createCell(8);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(course.hasCt() ? 1 : 0);
					cell = row.createCell(9);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getTDMundus());
					cell = row.createCell(10);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getTPMundus());
					cell = row.createCell(11);
					cell.setCellStyle(StylesLib.baseStyle(workbook));
					cell.setCellValue(teacher.getName());
					teacherEndRow++;
				}
				if (courseStartRow < teacherEndRow - 1)
					StylesLib.setCellMerge(sheet, courseStartRow, teacherEndRow - 1, 2, 2);
				courseStartRow = teacherEndRow;
			}
			if (teachingUnitStartRow < courseStartRow - 1)
				StylesLib.setCellMerge(sheet, teachingUnitStartRow, courseStartRow - 1, 1, 1);
			teachingUnitStartRow = teacherEndRow;
		}
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
