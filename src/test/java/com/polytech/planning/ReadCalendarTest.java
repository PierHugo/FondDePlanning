package com.polytech.planning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.*;

import com.polytech.planning.controller.ReadCalendar;
import com.polytech.planning.model.OriginalCalendar;

public class ReadCalendarTest {

	private static OriginalCalendar calendar;
	private static ReadCalendar readCalendar;

	@BeforeClass
	public static void init() throws Exception {
		calendar = new OriginalCalendar();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");

		try {
			// Semesters
			Date startS5Date = dateFormat.parse("11/09/2017");
			Date endS5Date = dateFormat.parse("22/01/2018");
			Date endS6Date = dateFormat.parse("04/06/2018");
			Date endS10Date = dateFormat.parse("08/04/2018");

			// Holidays
			Date holidayStart1 = dateFormat.parse("30/10/2017");
			Date holidayEnd1 = dateFormat.parse("06/11/2017");

			Date holidayStart2 = dateFormat.parse("25/12/2017");
			Date holidayEnd2 = dateFormat.parse("08/01/2018");

			Date holidayStart3 = dateFormat.parse("26/02/2018");
			Date holidayEnd3 = dateFormat.parse("05/03/2018");

			Date holidayStart4 = dateFormat.parse("23/04/2018");
			Date holidayEnd4 = dateFormat.parse("07/05/2018");

			// Free days
			Date freeDay1 = dateFormat.parse("20/09/2017");
			Date freeDay2 = dateFormat.parse("16/11/2017");
			Date freeDay3 = dateFormat.parse("08/12/2017");

			// Add semesters
			calendar.addSemesters("S5", startS5Date, endS5Date);
			calendar.addSemesters("S6", endS5Date, endS6Date);
			calendar.addSemesters("S7", startS5Date, endS5Date);
			calendar.addSemesters("S8", endS5Date, endS6Date);
			calendar.addSemesters("S9", startS5Date, endS5Date);
			calendar.addSemesters("S10", endS5Date, endS10Date);

			// Add holidays
			calendar.addHolidays("Vacances de la Toussaint", holidayStart1, holidayEnd1);
			calendar.addHolidays("Vacances de Noël", holidayStart2, holidayEnd2);
			calendar.addHolidays("Vacances d'hiver", holidayStart3, holidayEnd3);
			calendar.addHolidays("Vacances de printemps", holidayStart4, holidayEnd4);

			// Add free days
			calendar.addFreeDays("WE Accueil", freeDay1, "2");
			calendar.addFreeDays("Forum entreprise SIP pas cours ce jour", freeDay2, "4");
			calendar.addFreeDays("Nuit de l'info", freeDay3, "2");

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		readCalendar = new ReadCalendar("Calendar.xlsx");
		readCalendar.readSemesters(0);
		readCalendar.readHolidays(1);
		readCalendar.readFreeDays(2);
	}

	@Test
	public void testSearchContent() throws Exception {
		int[] coordonates1 = readCalendar.searchContent(0, "S8", false);
		int[] coordonates2 = readCalendar.searchContent(2, "Nuit de l'info", false);
		int[] coordonates3 = readCalendar.searchContent(0, "Date de fin", false);
		int[] coordonates4 = readCalendar.searchContent(0, "Non présent dans le document", false);
		int[] coordonates5 = readCalendar.searchContent(0, "DATé Dè fiN", false);

		Assert.assertEquals(5, coordonates1[0]); // Ligne
		Assert.assertEquals(2, coordonates1[1]); // Colonne

		Assert.assertEquals(4, coordonates2[0]); // Ligne
		Assert.assertEquals(1, coordonates2[1]); // Colonne

		Assert.assertEquals(1, coordonates3[0]); // Ligne
		Assert.assertEquals(4, coordonates3[1]); // Colonne

		Assert.assertEquals(-1, coordonates4[0]); // Ligne
		Assert.assertEquals(-1, coordonates4[1]); // Colonne
		
		Assert.assertEquals(1, coordonates5[0]); // Ligne
		Assert.assertEquals(4, coordonates5[1]); // Colonne
	}

	@Test
	public void testReadSemester() {
		Assert.assertTrue(readCalendar.getCalendar().semestersAreEquals(calendar.getSemesters()));
	}

	@Test
	public void testGetFirstCell() {
		int[] coordonates = readCalendar.getFirstCellNotEmpty(0);

		Assert.assertEquals(2, coordonates[0]); // Ligne
		Assert.assertEquals(1, coordonates[1]); // Colonne
	}

	@Test
	public void testReadHolidays() {
		Assert.assertTrue(readCalendar.getCalendar().holidaysAreEquals(calendar.getHolidays()));
	}

	@Test
	public void testReadFreeDays() {
		Assert.assertTrue(readCalendar.getCalendar().freeDaysAreEquals(calendar.getFreeDays()));
	}
}
