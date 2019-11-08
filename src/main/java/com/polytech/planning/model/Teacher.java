package com.polytech.planning.model;

public class Teacher {

	private String name;

	private double hoursCM;
	private double hoursTD;
	private double hoursTP;
	private double hoursProjet;

	private double TDMundus;
	private double TPMundus;

	public Teacher() {
		this.TDMundus = 0;
		this.TPMundus = 0;
		this.hoursCM = 0;
		this.hoursProjet = 0;
		this.hoursTD = 0;
		this.hoursTP = 0;
	}

	/**
	 * Teacher's Constructor
	 * 
	 * @param lastName
	 * @param firstName
	 */
	public Teacher(String name) {
		this.name = name;
		this.TDMundus = 0;
		this.TPMundus = 0;
		this.hoursCM = 0;
		this.hoursProjet = 0;
		this.hoursTD = 0;
		this.hoursTP = 0;
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

	public String toString() {
		double tdMundus = TDMundus;
		double tpMundus = TPMundus;
		String buffer = "Teacher -> " + this.name + "\n";

		buffer += " CM -> " + hoursCM + " hours\n";
		buffer += " TD -> " + hoursTD + " hours " + tdMundus + "\n";
		buffer += " TP -> " + hoursTP + " hours " + tpMundus + "\n";
		buffer += " Pr -> " + hoursProjet + " hours\n";
		buffer += " TD Mundus -> " + tdMundus + " hours\n";
		buffer += " TP Mundus -> " + tpMundus + " hours\n";
		buffer += "----------------------------------\n";

		return buffer;
	}

	public boolean equals(Teacher input) {
		if (!name.equals(input.getName())) {
			return false;
		} else if (hoursCM != input.getHoursCM()) {
			return false;
		} else if (hoursTD != input.getHoursTD()) {
			return false;
		} else if (hoursTP != input.getHoursTP()) {
			return false;
		} else if (hoursProjet != input.getHoursProjet()) {
			return false;
		} else if (TDMundus != input.getTDMundus()) {
			return false;
		} else if (TPMundus != input.getTPMundus()) {
			return false;
		}

		return true;
	}

	/**
	 * @return the hoursCM
	 */
	public double getHoursCM() {
		return hoursCM;
	}

	/**
	 * @param hoursCM
	 *            the hoursCM to set
	 */
	public void setHoursCM(double hoursCM) {
		this.hoursCM = hoursCM;
	}

	/**
	 * @return the hoursTD
	 */
	public double getHoursTD() {
		return hoursTD;
	}

	/**
	 * @param hoursTD
	 *            the hoursTD to set
	 */
	public void setHoursTD(double hoursTD) {
		this.hoursTD = hoursTD;
	}

	/**
	 * @return the hoursTP
	 */
	public double getHoursTP() {
		return hoursTP;
	}

	/**
	 * @param hoursTP
	 *            the hoursTP to set
	 */
	public void setHoursTP(double hoursTP) {
		this.hoursTP = hoursTP;
	}

	/**
	 * @return the hoursProjet
	 */
	public double getHoursProjet() {
		return hoursProjet;
	}

	/**
	 * @param hoursProjet
	 *            the hoursProjet to set
	 */
	public void setHoursProjet(double hoursProjet) {
		this.hoursProjet = hoursProjet;
	}

	/**
	 * @return the tDMundus
	 */
	public double getTDMundus() {
		return TDMundus;
	}

	/**
	 * @param tDMundus
	 *            the tDMundus to set
	 */
	public void setTDMundus(double tDMundus) {
		TDMundus = tDMundus;
	}

	/**
	 * @return the tPMundus
	 */
	public double getTPMundus() {
		return TPMundus;
	}

	/**
	 * @param tPMundus
	 *            the tPMundus to set
	 */
	public void setTPMundus(double tPMundus) {
		TPMundus = tPMundus;
	}

}
