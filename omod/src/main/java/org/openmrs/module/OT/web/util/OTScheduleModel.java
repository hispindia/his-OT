/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of OT module.
 *
 *  OT module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  OT module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OT module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.OT.web.util;

import java.util.Comparator;

public class OTScheduleModel implements Comparator<OTScheduleModel>, Comparable<OTScheduleModel> {
	
	private String startDate;
	private String patientIdentifier;
	private String patientName;
	private String gender;
	private Integer age;
	private String pDiagnosis;
	private String procedure;
	private Integer encounterId;
	private Integer orderId;
	private String status;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPatientIdentifier() {
		return patientIdentifier;
	}

	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getpDiagnosis() {
		return pDiagnosis;
	}

	public void setpDiagnosis(String pDiagnosis) {
		this.pDiagnosis = pDiagnosis;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	// Overriding the compareTo method
	public int compareTo(OTScheduleModel o) {
		return (this.patientName).compareTo(o.patientName);
	}

	public int compare(OTScheduleModel o1, OTScheduleModel o2) {
		return 0;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
