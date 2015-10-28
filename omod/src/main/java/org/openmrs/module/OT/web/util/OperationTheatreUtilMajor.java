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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.model.MajorOTProcedure;
import org.openmrs.module.OT.util.OTConstants;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.openmrs.module.hospitalcore.util.PatientUtils;

public class OperationTheatreUtilMajor {

	/**
	 * Generate a list of OT Schedule Model using schedules
	 * 
	 * @param schedules
	 * @param otProcedureTreeMap
	 * @return List<OTscheduleModel>
	 */
	public static List<OTScheduleModel> generateModelsFromSchedules(
			List<OpdTestOrder> schedules) {
		
		List<OTScheduleModel> models = new ArrayList<OTScheduleModel>();
		for (OpdTestOrder schedule : schedules) {
			OTScheduleModel osm = generateModel(schedule);
			if (osm != null)
				models.add(osm);
		}
		return models;
	}
	
	/**
	 * Generate a single OT schedule model
	 * 
	 * @param schedule
	 * @return
	 */
	private static OTScheduleModel generateModel(OpdTestOrder schedule) {
	
		Encounter encounter = schedule.getEncounter();
		List<Obs> pDiagnosis=new ArrayList<Obs>();List<Obs> pFinalDiagnosis;
		OTScheduleModel osm = new OTScheduleModel();
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		MajorOTProcedure procedure = ots.getMajorOTProcedure(schedule);;
		if (encounter != null && encounter.getAllObs().size() != 0) {
			if (procedure == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				osm.setStartDate(sdf.format(schedule.getScheduleDate()));
				osm.setPatientIdentifier(schedule.getPatient().getPatientIdentifier().getIdentifier());
				osm.setPatientName(PatientUtils.getFullName(schedule.getPatient()));
				osm.setGender(schedule.getPatient().getGender());
				osm.setAge(schedule.getPatient().getAge());
				osm.setOrderId(schedule.getOpdOrderId());
				
				if (encounter != null)
					osm.setEncounterId(encounter.getEncounterId());
				osm.setProcedure(schedule.getValueCoded().getName().toString());
				osm.setStatus(null);
				
				pDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("PROVISIONAL DIAGNOSIS"),schedule.getCreatedOn());
				pFinalDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("FINAL DIAGNOSIS"),schedule.getCreatedOn());
				
				String pd="";
				if(pDiagnosis.size()>0){
				for(Obs pDiagnos:pDiagnosis){
					pd=pd+pDiagnos.getValueCoded().getName().toString()+",";
				}
				pd = pd.substring(0, pd.length()-1); 
				}
				if(pFinalDiagnosis.size()>0){
					for(Obs pFinalDiagnos:pFinalDiagnosis){
							pd=pd+pFinalDiagnos.getValueCoded().getName().toString()+",";
						}
						pd = pd.substring(0, pd.length()-1); 
						
					}
				osm.setpDiagnosis(pd);
				//osm.setpDiagnosis(schedule.getValueCoded().getName().toString());
		
			} else {
				if (!procedure.getStatus().equals(OTConstants.PROCEDURE_STATUS_COMPLETED)) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					osm.setStartDate(sdf.format(schedule.getScheduleDate()));
					osm.setPatientIdentifier(schedule.getPatient().getPatientIdentifier().getIdentifier());
					osm.setPatientName(PatientUtils.getFullName(schedule.getPatient()));
					osm.setGender(schedule.getPatient().getGender());
					osm.setAge(schedule.getPatient().getAge());
					osm.setOrderId(schedule.getOpdOrderId());
					
					if (encounter != null)
						osm.setEncounterId(encounter.getEncounterId());
					osm.setProcedure(schedule.getValueCoded().getName().toString());
					osm.setStatus(procedure.getStatus());
					
					//pDiagnosis = ots.getDiagnosisOTProcedure(encounter,schedule.getValueCoded());
					//osm.setpDiagnosis(pDiagnosis.getValueCoded().getName().toString());
					pDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("PROVISIONAL DIAGNOSIS"),schedule.getCreatedOn());
					pFinalDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("FINAL DIAGNOSIS"),schedule.getCreatedOn());
					String pd="";
					if(pDiagnosis.size()>0){
					for(Obs pDiagnos:pDiagnosis){
						pd=pd+pDiagnos.getValueCoded().getName().toString()+",";
					}
					pd = pd.substring(0, pd.length()-1); 
					}
					if(pFinalDiagnosis.size()>0){
						for(Obs pFinalDiagnos:pFinalDiagnosis){
								pd=pd+pFinalDiagnos.getValueCoded().getName().toString()+",";
							}
							pd = pd.substring(0, pd.length()-1); 
							
						}
					osm.setpDiagnosis(pd);
				} else {
					return null;
				}
			}
		}
		return osm;
	}

	public static Date parseDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm a");
		return sdf.parse(dateStr);
	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	
	public static OTScheduleModel generateRescheduleModel(OpdTestOrder schedule) {
		return generateModel(schedule);
	}

	/**
	 * Generate a list of Observation OT schedule Model
	 * 
	 * @param otSchedules
	 * @return List
	 */
	public static List<OTScheduleModel> generateObsModelsFromSchedules(
			List<MajorOTProcedure> otSchedules) {
		List<OTScheduleModel> models = new ArrayList<OTScheduleModel>();
		for (MajorOTProcedure schedule : otSchedules) {
			OTScheduleModel osm = generateObsModel(schedule);
			models.add(osm);
		}
		return models;
	}

	/**
	 * Generate a single OT schedule model
	 * 
	 * @param schedule
	 * @return 
	 */
	private static OTScheduleModel generateObsModel(MajorOTProcedure schedule) {
		Encounter encounter = schedule.getEncounter();
		OTScheduleModel osm = new OTScheduleModel();
		
		if (encounter != null && encounter.getAllObs().size() != 0) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			osm.setStartDate(sdf.format(schedule.getOtSchedule()));
			osm.setPatientIdentifier(schedule.getPatient().getPatientIdentifier().getIdentifier());
			osm.setPatientName(PatientUtils.getFullName(schedule.getPatient()));
			osm.setGender(schedule.getPatient().getGender());
			osm.setAge(schedule.getPatient().getAge());
			osm.setOrderId(schedule.getOpdOrderId().getOpdOrderId());
			osm.setProcedure(schedule.getProcedure().getName().toString());
			osm.setpDiagnosis(schedule.getDiagnosis());
			osm.setStatus(schedule.getStatus());
		}
		return osm;
	}
	
	/**
	 * Generate an Observation Form Model
	 * 
	 * @param schedule
	 * @return
	 */
	public static OTScheduleModel generateObservationFormModel(
			MajorOTProcedure schedule) {
		Encounter encounter = schedule.getEncounter();
		OTScheduleModel osm = new OTScheduleModel();
		
		if (encounter != null && encounter.getAllObs().size() != 0) {
			osm.setOrderId(schedule.getOpdOrderId().getOpdOrderId());
		}
		return osm;
	}
}
