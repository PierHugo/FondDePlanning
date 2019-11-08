package com.polytech.planning.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.polytech.planning.model.Calendar;
import com.polytech.planning.model.FreeDay;
import com.polytech.planning.model.Holiday;
import com.polytech.planning.model.OriginalCalendar;
import com.polytech.planning.model.Semester;

public class ParserCalendar {
	
	private OriginalCalendar originalCalendar;
	
	public ParserCalendar() {
		
	}
	
	
	/**
	 * @param originalCalendar
	 */
	public ParserCalendar(OriginalCalendar originalCalendar) {
		this.originalCalendar = originalCalendar;
	}

	/**
	 * create semesters
	 * @return list of semester
	 */
	private List<Semester> createSemester() {
		List<Semester> semesters = new ArrayList<Semester>();

		for (String key : this.originalCalendar.getSemesters().keySet()) {

			Semester semester;

			// DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
			Date sdate, edate;
			sdate = this.originalCalendar.getSemesters().get(key)[0];
			edate = this.originalCalendar.getSemesters().get(key)[1];
			// sdate = format.parse(this.originalCalendar.getSemesters().get(key)[0]);
			// edate = format.parse(this.originalCalendar.getSemesters().get(key)[1]);
			semester = new Semester(key);
			semester.setStartDate(sdate);
			semester.setEndDate(edate);

			semester.setListHoliday(this.createHolidays());
			semester.setListFreeDays(this.createFreeDays());
			semesters.add(semester);

		}
		return semesters;
	}

	/**
	 * createFreeDays
	 * @return list of freedays
	 */
	private List<FreeDay> createFreeDays() {
		List<FreeDay> freeDays = new ArrayList<FreeDay>();
		for (Date date : this.originalCalendar.getFreeDays().keySet()) {
			FreeDay freeDay;
			// DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
			String name;

			name = this.originalCalendar.getFreeDays().get(date)[0];

			freeDay = new FreeDay();
			freeDay.setName(name);
			freeDay.setDate(date);

			int timeSlot = Integer.parseInt(this.originalCalendar.getFreeDays().get(date)[1]);
			freeDay.setTimeslot(timeSlot);

			freeDays.add(freeDay);
		}
		return freeDays;
	}

	/**
	 * createHolidays
	 * @return list of holiday
	 */
	private List<Holiday> createHolidays() {
		List<Holiday> holidays = new ArrayList<Holiday>();

		for (String name : this.originalCalendar.getHolidays().keySet()) {
			Holiday holiday;

			Date sdate, edate;
			sdate = this.originalCalendar.getHolidays().get(name)[0];
			edate = this.originalCalendar.getHolidays().get(name)[1];

			holiday = new Holiday(name, sdate, edate);

			holidays.add(holiday);
		}
		return holidays;
	}
	/**
	 * 
	 * @return object calendar
	 */
	public Calendar createCalendar() {
		Calendar calendar = new Calendar();
		calendar.setListSemester(this.createSemester());
		return calendar;
	}

}
