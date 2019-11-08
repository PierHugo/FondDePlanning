/**
 * 
 */
package com.polytech.planning.model;

import java.util.Date;

/**
 * @author penghanyuan
 *
 */
public class FreeDay {

	private String name;
	private Date date;
	private int timeslot;
	
	
	
	/**
	 * @param name
	 * @param date
	 * @param timeslot
	 */
	public FreeDay(String name, Date date, int timeslot) {
		this.name = name;
		this.date = date;
		this.timeslot = timeslot;
	}

	public FreeDay() {
		
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the timeslot
	 */
	public int getTimeslot() {
		return timeslot;
	}

	/**
	 * @param timeslot the timeslot to set
	 */
	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}

}
