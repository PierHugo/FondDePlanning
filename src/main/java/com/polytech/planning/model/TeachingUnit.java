package com.polytech.planning.model;

import java.util.ArrayList;
import java.util.List;

public class TeachingUnit {

	private String name;
	private List<Course> listCourses;
	private float totalCM;
	private float totalTD;
	private float totalTP;
	private float totalProject;

	/**
	 * TeachingUnit's Constructor
	 * 
	 * @param name
	 */
	public TeachingUnit(String name) {
		this.name = name;
		listCourses = new ArrayList<Course>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the listCourses
	 */
	public List<Course> getListCourses() {
		return listCourses;
	}

	/**
	 * @param listCourses
	 *            the listCourses to set
	 */
	public void setListCourses(List<Course> listCourses) {
		this.listCourses = listCourses;
		for (Course c : listCourses) {
			if(this.name.equals("Soutien"))
				continue;
			totalCM += c.getTotalCM();
			totalTD += c.getTotalTD();
			totalTP += c.getTotalTP();
			totalProject += c.getTotalProject();
		}
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
