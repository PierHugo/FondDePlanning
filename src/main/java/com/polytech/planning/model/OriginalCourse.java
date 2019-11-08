package com.polytech.planning.model;

/**
 * Class for store original information of course
 * To be parsed by class ParserMockUp
 *
 */
public class OriginalCourse {

	private String courseName;

	private Double hoursCM;
	private Double hoursTD;
	private Double hoursTP;
	private Double hoursProject;

	private String teachers;

	private boolean mundus;
	private boolean cc;
	private boolean ct;

	public OriginalCourse() {
		this.courseName = null;
		this.hoursCM = null;
		this.hoursTD = null;
		this.hoursTP = null;
		this.hoursProject = null;
		this.teachers = null;
		this.mundus = false;
		this.cc = false;
		this.ct = false;
	}

	public OriginalCourse(String courseName, Double hoursCM, Double hoursTD, Double hoursTP, Double hoursProject,
			String teachers, boolean mundus, boolean cc, boolean ct) {
		this.courseName = courseName.trim();
		this.hoursCM = hoursCM;
		this.hoursTD = hoursTD;
		this.hoursTP = hoursTP;
		this.hoursProject = hoursProject;
		this.teachers = teachers.trim();
		this.mundus = mundus;
		this.cc = cc;
		this.ct = ct;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName.trim();
	}

	/**
	 * @return the hoursCM
	 */
	public Double getHoursCM() {
		return hoursCM;
	}

	/**
	 * @param hoursCM
	 *            the hoursCM to set
	 */
	public void setHoursCM(Double hoursCM) {
		this.hoursCM = hoursCM;
	}

	/**
	 * @return the hoursTD
	 */
	public Double getHoursTD() {
		return hoursTD;
	}

	/**
	 * @param hoursTD
	 *            the hoursTD to set
	 */
	public void setHoursTD(Double hoursTD) {
		this.hoursTD = hoursTD;
	}

	/**
	 * @return the hoursTP
	 */
	public Double getHoursTP() {
		return hoursTP;
	}

	/**
	 * @param hoursTP
	 *            the hoursTP to set
	 */
	public void setHoursTP(Double hoursTP) {
		this.hoursTP = hoursTP;
	}

	/**
	 * @return the hoursProject
	 */
	public Double getHoursProject() {
		return hoursProject;
	}

	/**
	 * @param hoursProject
	 *            the hoursProject to set
	 */
	public void setHoursProject(Double hoursProject) {
		this.hoursProject = hoursProject;
	}

	/**
	 * @return the teachers
	 */
	public String getTeachers() {
		return teachers;
	}

	/**
	 * @param teachers
	 *            the teachers to set
	 */
	public void setTeachers(String teachers) {
		this.teachers = teachers.trim();
	}

	/**
	 * @return the mundus
	 */
	public boolean isMundus() {
		return mundus;
	}

	/**
	 * @param mundus
	 *            the mundus to set
	 */
	public void setMundus(boolean mundus) {
		this.mundus = mundus;
	}

	/**
	 * @return the cc
	 */
	public boolean isCc() {
		return cc;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public void setCc(boolean cc) {
		this.cc = cc;
	}

	/**
	 * @return the ct
	 */
	public boolean isCt() {
		return ct;
	}

	/**
	 * @param ct
	 *            the ct to set
	 */
	public void setCt(boolean ct) {
		this.ct = ct;
	}

	public String toString() {
		String output = "";

		output += "\n\tName => " + courseName;
		output += "\n\tCM = " + hoursCM + " | TP = " + hoursTP + " | TD = " + hoursTD + " | Project = " + hoursProject;

		if (this.mundus)
			output += " | Mundus";

		if (this.cc)
			output += " | CC";

		if (this.ct)
			output += " | CT";

		output += "\n\tTeachers => " + teachers;

		return output + "\n";
	}

	public boolean equals(OriginalCourse input) {
		if (!input.getCourseName().equals(courseName)) {
			System.out.println("Cours");
			return false;
		}

		if (!input.getHoursCM().equals(hoursCM)) {
			System.out.println("CM");
			return false;
		}

		if (!input.getHoursTD().equals(hoursTD)) {
			System.out.println("TD");
			return false;
		}

		if (!input.getHoursTP().equals(hoursTP)) {
			System.out.println("TP");
			return false;
		}

		if (!input.getHoursProject().equals(hoursProject)) {
			System.out.println("Projet");
			return false;
		}

		if (input.isMundus() != mundus) {
			System.out.println("Mundus");
			return false;
		}

		if (input.isCc() != cc) {
			System.out.println("CC => " + input.isCc() + " / " + cc);
			return false;
		}

		if (input.isCt() != ct) {
			System.out.println("CT => " + input.isCt() + " / " + ct);
			return false;
		}

		if (!input.getTeachers().equals(teachers)) {
			System.out.println("Teachers");
			return false;
		}

		return true;
	}
}
