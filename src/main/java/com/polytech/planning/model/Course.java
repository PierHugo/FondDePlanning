package com.polytech.planning.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
	
	private String name;
	
	private List<Teacher> listTeachers;
	
	private double totalCM;
	private double totalTD;
	private double totalTP;
	private double totalProject;
	
	private boolean cc;
	private boolean ct;
	private boolean mundus;
	private String type; // ASR or SI
	
	/**
	 * Course's Constructor
	 * @param name
	 */
	public Course(String name) {
		listTeachers = new ArrayList<Teacher>();
		this.name = name;
		this.cc = false;
		this.ct = false;
		this.mundus = false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the totalCM
	 */
	public double getTotalCM() {
		return totalCM;
	}

	/**
	 * @return the totalTD
	 */
	public double getTotalTD() {
		return totalTD;
	}

	/**
	 * @return the totalTP
	 */
	public double getTotalTP() {
		return totalTP;
	}

	/**
	 * @return the listTeachers
	 */
	public List<Teacher> getListTeachers() {
		return listTeachers;
	}

	/**
	 * @param listTeachers the listTeachers to set
	 */
	public void setListTeachers(List<Teacher> listTeachers) {
		this.listTeachers = listTeachers;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param totalCM the totalCM to set
	 */
	public void setTotalCM(double totalCM) {
		this.totalCM = totalCM;
	}

	/**
	 * @param totalTD the totalTD to set
	 */
	public void setTotalTD(double totalTD) {
		this.totalTD = totalTD;
	}

	/**
	 * @param totalTP the totalTP to set
	 */
	public void setTotalTP(double totalTP) {
		this.totalTP = totalTP;
	}

	/**
	 * @return the totalProject
	 */
	public double getTotalProject() {
		return totalProject;
	}

	/**
	 * @param totalProject the totalProject to set
	 */
	public void setTotalProject(double totalProject) {
		this.totalProject = totalProject;
	}

	/**
	 * @return the cc
	 */
	public boolean hasCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(boolean cc) {
		this.cc = cc;
	}

	/**
	 * @return the ct
	 */
	public boolean hasCt() {
		return ct;
	}

	/**
	 * @param ct the ct to set
	 */
	public void setCt(boolean ct) {
		this.ct = ct;
	}

	/**
	 * @return the mundus
	 */
	public boolean isMundus() {
		return mundus;
	}

	/**
	 * @param mundus the mundus to set
	 */
	public void setMundus(boolean mundus) {
		this.mundus = mundus;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}	
}
