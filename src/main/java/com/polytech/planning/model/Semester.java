package com.polytech.planning.model;

import java.util.Date;
import java.util.List;

public class Semester {
	
	private String name;
	
	private Date startDate;
	private Date endDate;
	
	private List<FreeDay> listFreeDays;
	private List<Holiday> listHoliday;
	
	
	
	/**
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @param listFreeDays
	 * @param listHoliday
	 */
	public Semester(String name, Date startDate, Date endDate, List<FreeDay> listFreeDays, List<Holiday> listHoliday) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.listFreeDays = listFreeDays;
		this.listHoliday = listHoliday;
	}

	/**
	 * Semester's Constructor
	 * @param name
	 */
	public Semester(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the listPublicHoliday
	 */
	public List<FreeDay> getListFreeDays() {
		return listFreeDays;
	}

	/**
	 * @param listFreeDays the listPublicHoliday to set
	 */
	public void setListFreeDays(List<FreeDay> listFreeDays) {
		this.listFreeDays = listFreeDays;
	}

	/**
	 * @return the listHoliday
	 */
	public List<Holiday> getListHoliday() {
		return listHoliday;
	}

	/**
	 * @param listHoliday the listHoliday to set
	 */
	public void setListHoliday(List<Holiday> listHoliday) {
		this.listHoliday = listHoliday;
	}
}
