package com.polytech.planning.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Class for store original information of calendar
 * To be parsed by class ParserCalendar
 *
 */
public class OriginalCalendar {

	private LinkedHashMap<String, Date[]> semesters;
	private LinkedHashMap<String, Date[]> holidays;
	private LinkedHashMap<Date, String[]> freeDays;
	private SimpleDateFormat dateFormatter;

	public OriginalCalendar() {
		this.semesters = new LinkedHashMap<String, Date[]>();
		this.holidays = new LinkedHashMap<String, Date[]>();
		this.freeDays = new LinkedHashMap<Date, String[]>();
		this.dateFormatter = new SimpleDateFormat("mm/dd/yy");
	}

	/**
	 * @return the semesters
	 */
	public LinkedHashMap<String, Date[]> getSemesters() {
		return semesters;
	}

	/**
	 * @param semsters
	 *            the semesters to set
	 */
	public void addSemesters(String name, Date start, Date end) {
		Date[] dates = new Date[2];
		dates[0] = start;
		dates[1] = end;
		this.semesters.put(name, dates);
	}

	/**
	 * @param semsters
	 *            the semesters to set
	 */
	public void clearSemesters() {
		this.semesters.clear();
		this.semesters = new LinkedHashMap<String, Date[]>();
	}

	/**
	 * @param semsters
	 *            the semesters to set
	 */
	public void addSemesters(String name, String start, String end) {
		Date[] dates = new Date[2];
		Date startDate;
		Date endDate;

		try {
			startDate = this.dateFormatter.parse(start);
			endDate = this.dateFormatter.parse(start);
			dates[0] = startDate;
			dates[1] = endDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.semesters.put(name, dates);
	}

	/**
	 * @return the holidays
	 */
	public LinkedHashMap<String, Date[]> getHolidays() {
		return holidays;
	}

	/**
	 * @param holidays
	 *            the holidays to set
	 */
	public void addHolidays(String name, String start, String end) {
		Date[] dates = new Date[2];
		Date startDate;
		Date endDate;
		try {
			startDate = this.dateFormatter.parse(start);
			endDate = this.dateFormatter.parse(start);
			dates[0] = startDate;
			dates[1] = endDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.holidays.put(name, dates);
	}

	/**
	 * @param holidays
	 *            the holidays to set
	 */
	public void addHolidays(String name, Date start, Date end) {
		Date[] dates = new Date[2];
		dates[0] = start;
		dates[1] = end;

		this.holidays.put(name, dates);
	}

	/**
	 * @param name
	 * @param day
	 * @param creneaux
	 */
	public void addFreeDays(String name, Date day, String creneaux) {
		String[] content = new String[2];
		content[0] = name;
		content[1] = creneaux;

		this.freeDays.put(day, content);
	}

	/**
	 * @param semsters
	 *            the semesters to set
	 */
	public void clearHolidays() {
		this.holidays.clear();
		this.holidays = new LinkedHashMap<String, Date[]>();
	}

	/**
	 * @param semsters
	 *            the semesters to set
	 */
	public void clearFreeDays() {
		this.freeDays.clear();
		this.freeDays = new LinkedHashMap<Date, String[]>();
	}

	/**
	 * @return the freeDays
	 */
	public LinkedHashMap<Date, String[]> getFreeDays() {
		return freeDays;
	}

	/**
	 * @return
	 */
	public String semesterShow() {
		int size = this.semesters.size();
		String retour = "Size = " + size;

		for (String name : semesters.keySet()) {

			String key = name.toString();
			Date dateStart = semesters.get(name)[0];
			Date dateEnd = semesters.get(name)[1];

			retour = retour + "\n" + key + " - " + dateStart + " / " + dateEnd;
		}

		return retour;
	}

	/**
	 * @return
	 */
	public String holidayShow() {
		int size = this.holidays.size();
		String retour = "Size = " + size;

		for (String name : holidays.keySet()) {

			String key = name.toString();
			Date dateStart = holidays.get(name)[0];
			Date dateEnd = holidays.get(name)[1];

			retour = retour + "\n" + key + " - " + dateStart + " / " + dateEnd;
		}

		return retour;
	}

	/**
	 * @param input
	 * @return
	 */
	public boolean semestersAreEquals(LinkedHashMap<String, Date[]> input) {
		if (this.semesters.size() != input.size()) {
			return false;
		} else {
			return hashMapAreEquals(input, this.semesters);
		}
	}

	/**
	 * @param input
	 * @return
	 */
	public boolean holidaysAreEquals(LinkedHashMap<String, Date[]> input) {
		if (this.holidays.size() != input.size()) {
			return false;
		} else {
			return hashMapAreEquals(input, this.holidays);
		}
	}

	/**
	 * @param input
	 * @return
	 */
	public boolean freeDaysAreEquals(LinkedHashMap<Date, String[]> input) {

		Set<Date> keysInput1 = this.freeDays.keySet();
		Set<Date> keysInput2 = input.keySet();

		Iterator<Date> itInput1 = keysInput1.iterator();
		Iterator<Date> itInput2 = keysInput2.iterator();

		if (this.freeDays.size() != input.size()) {
			return false;
		} else {
			while (itInput1.hasNext()) {

				Object keyInput1 = itInput1.next();
				String[] valueInput1 = this.freeDays.get(keyInput1);

				Object keyInput2 = itInput2.next();
				String[] valueInput2 = input.get(keyInput2);

				if (!keyInput1.equals(keyInput2)) {
					return false;
				}

				if (valueInput1.length == valueInput2.length) {
					for (int i = 0; i < valueInput1.length; i++) {
						if (!valueInput1[i].equals(valueInput2[i])) {
							return false;
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param input1
	 * @param
	 * @return
	 */
	private boolean hashMapAreEquals(LinkedHashMap<String, Date[]> input1, LinkedHashMap<String, Date[]> input2) {

		Set<String> keysInput1 = input1.keySet();
		Set<String> keysInput2 = input2.keySet();

		Iterator<String> itInput1 = keysInput1.iterator();
		Iterator<String> itInput2 = keysInput2.iterator();

		while (itInput1.hasNext()) {

			Object keyInput1 = itInput1.next();
			Date[] valueInput1 = input1.get(keyInput1);

			Object keyInput2 = itInput2.next();
			Date[] valueInput2 = input2.get(keyInput2);

			if (valueInput1.length == valueInput2.length) {
				for (int i = 0; i < valueInput1.length; i++) {
					if (!valueInput1[i].equals(valueInput2[i])) {
						return false;
					}
				}
			} else {
				return false;
			}

			if (!keyInput1.equals(keyInput2) && !valueInput1.equals(valueInput2)) {
				return false;
			}
		}

		return true;
	}

}
