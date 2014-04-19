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

package org.openmrs.module.OT.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;

public class MajorOTProcedure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4858647209395121811L;
	
	private Integer majorOTId;
	private String status;
	private Patient patient;
	private Concept procedure;
	private String diagnosis;
	private Encounter encounter;
	private Date otSchedule;
	private OpdTestOrder opdOrderId;
	
	public Integer getMajorOTId() {
		return majorOTId;
	}
	public void setMajorOTId(Integer majorOTId) {
		this.majorOTId = majorOTId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getOtSchedule() {
		return otSchedule;
	}
	public void setOtSchedule(Date otSchedule) {
		this.otSchedule = otSchedule;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Concept getProcedure() {
		return procedure;
	}
	public void setProcedure(Concept procedure) {
		this.procedure = procedure;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public Encounter getEncounter() {
		return encounter;
	}
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	public OpdTestOrder getOpdOrderId() {
		return opdOrderId;
	}
	public void setOpdOrderId(OpdTestOrder opdOrderId) {
		this.opdOrderId = opdOrderId;
	}
}
