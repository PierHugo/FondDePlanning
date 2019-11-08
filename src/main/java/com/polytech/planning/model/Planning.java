package com.polytech.planning.model;

import java.util.List;

public class Planning {

	/**
	 * for example: 2017/2018
	 */
	private String year;

	private List<TeachingUnit> teachingUnits;
	private Calendar calendar;

	private float totalCM;
	private float totalTD;
	private float totalTP;
	private float totalProject;

	/**
	 * @param year
	 * @param teachingUnits
	 * @param semesters
	 */
	public Planning(String year, List<TeachingUnit> teachingUnits, Calendar calendar) {
		this.year = year;
		this.teachingUnits = teachingUnits;
		this.calendar = calendar;
		for (TeachingUnit t : teachingUnits) {
			totalCM += t.getTotalCM();
			totalTD += t.getTotalTD();
			totalTP += t.getTotalTP();
			totalProject += t.getTotalProject();
		}
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the teachingUnits
	 */
	public List<TeachingUnit> getTeachingUnits() {
		return teachingUnits;
	}

	/**
	 * @param teachingUnits
	 *            the teachingUnits to set
	 */
	public void setTeachingUnits(List<TeachingUnit> teachingUnits) {
		this.teachingUnits = teachingUnits;
		for (TeachingUnit t : teachingUnits) {
			totalCM += t.getTotalCM();
			totalTD += t.getTotalTD();
			totalTP += t.getTotalTP();
			totalProject += t.getTotalProject();
		}
	}

	/**
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * @param calendar
	 *            the calendar to set
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return the totalCM
	 */
	public float getTotalCM() {
		return totalCM;
	}

	/**
	 * @return the totalTD
	 */
	public float getTotalTD() {
		return totalTD;
	}

	/**
	 * @return the totalTP
	 */
	public float getTotalTP() {
		return totalTP;
	}

	/**
	 * @return the totalProjet
	 */
	public float getTotalProject() {
		return totalProject;
	}

}
