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

package org.openmrs.module.OT.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.db.OperationTheatreDAO;
import org.openmrs.module.OT.model.MajorOTProcedure;
import org.openmrs.module.OT.model.MinorOTProcedure;
import org.openmrs.module.OT.util.OTConstants;
import org.openmrs.module.hospitalcore.PatientDashboardService;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;

/**
 * It is a default implementation of {@link OperationTheatreService}.
 */
public class OperationTheatreServiceImpl extends BaseOpenmrsService implements OperationTheatreService {
	
	private Log logger = LogFactory.getLog(getClass());
	
	public OperationTheatreServiceImpl() {
	}
	
	private OperationTheatreDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(OperationTheatreDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public OperationTheatreDAO getDao() {
	    return dao;
    }
	
	public List<Concept> getProceduresMinorOT() {
		Concept concept = Context.getConceptService().getConcept(OTConstants.CONCEPT_CLASS_NAME_MINOR_OT);
		
		Collection<ConceptAnswer> allMinorOTProcedures = null;
		List<Concept> id = new ArrayList<Concept>();
		if( concept != null )
		{
			allMinorOTProcedures = concept.getAnswers();
			for (ConceptAnswer c: allMinorOTProcedures){
				id.add(c.getAnswerConcept());
			}
			return id;
		}
		return null;
	}

	public List<OpdTestOrder> getSchedulesMinorOT(Date startDate, String phrase,
			List<Concept> procedures, int page) throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		List<OpdTestOrder> schedules = dao.getSchedulesMinorOT(startDate, 
				procedures, patients, page, phrase);
		return schedules;
	}
	
	public Integer countScheduleMinorOT(Date startDate, String phrase, 
			List<Concept> procedures) throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		return dao.countScheduleMinorOT(startDate, procedures, patients, phrase);
	}

	public List<Obs> getDiagnosisOTProcedure(Encounter encounter,Concept concept,Date date) {
		//Concept concept = Context.getConceptService()
		//		.getConcept(OTConstants.CONCEPT_CLASS_NAME_DIAGNOSIS);
		return dao.getObsInstanceForDiagnosis(encounter,concept,date);
	}
	
	public OpdTestOrder getAcceptedSchedule(Integer orderId) {
		return dao.getAcceptedSchedule(orderId);
	}

	public Integer acceptProcedureMinor(OpdTestOrder schedule)
		throws ParseException {
		Encounter encounter = schedule.getEncounter();
		List<Obs> pDiagnosis;List<Obs> pFinalDiagnosis;
		MinorOTProcedure procedure = dao.getMinorOTProcedure(schedule);
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		
		if (procedure == null) {
			procedure = new MinorOTProcedure();
			procedure.setPatient(schedule.getPatient());
			procedure.setProcedure(schedule.getValueCoded());
			procedure.setEncounter(encounter);
			procedure.setOpdOrderId(schedule);
			
			pDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("PROVISIONAL DIAGNOSIS"),schedule.getCreatedOn());
			pFinalDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("FINAL DIAGNOSIS"),schedule.getCreatedOn());
			
			String pd="";
			if(pDiagnosis.size()>0){for(Obs pDiagnos:pDiagnosis){
				pd=pd+pDiagnos.getValueCoded().getName().toString()+",";
			}
			pd = pd.substring(0, pd.length()-1); }
			if(pFinalDiagnosis.size()>0){
				for(Obs pFinalDiagnos:pFinalDiagnosis){
					pd=pd+pFinalDiagnos.getValueCoded().getName().toString()+",";
				}
				pd = pd.substring(0, pd.length()-1); 
				
			}
			
			procedure.setDiagnosis(pd);
			procedure.setOtSchedule(new Date());
			procedure.setStatus(OTConstants.PROCEDURE_STATUS_ACCEPTED);
	
			MinorOTProcedure acceptedProcedure = dao.saveOTProcedure(procedure);
			logger.info(String
					.format("Accepting a procedure [procedure=%s, patient=%s, order=%s, diagnosis=%s]",
							procedure.getProcedure().getConceptId(), procedure
									.getPatient().getPatientId(), procedure
									.getOpdOrderId(), procedure
									.getDiagnosis()));
			return acceptedProcedure.getMinorOTId();
		} else {
			logger.warn(String.format("Existing test for order=%s found.",
					schedule.getOpdOrderId()));
			return OTConstants.ACCEPT_PROCEDURE_RETURN_ERROR_EXISTING_PROCEDURE;
		}
	}
	
	

	
	public String rescheduleProcedure(OpdTestOrder schedule,
			Date rescheduledDate) {
		schedule.setScheduleDate(rescheduledDate);
		Context.getService(PatientDashboardService.class).saveOrUpdateOpdOrder(schedule);
		return OTConstants.RESCHEDULE_PROCEDURE_RETURN_SUCCESS;
	}

	public MinorOTProcedure getMinorOTProcedure(OpdTestOrder opdOrderId) {
		return dao.getMinorOTProcedure(opdOrderId);
	}

	public List<MinorOTProcedure> getMinorOTSchedules(Date startDate, String phrase,
			List<Concept> procedures, Integer page) throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		List<MinorOTProcedure> schedules = dao.getMinorOTSchedules(startDate, 
				procedures, patients, page, phrase);
		return schedules;
	}

	public Integer countMinorOTSchedule(Date startDate, String phrase,
			List<Concept> procedures) throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		return dao.countMinorOTSchedule(startDate, procedures, patients, phrase);
	}

	public String observationProcedure(MinorOTProcedure schedule,
			String observations) {
		Encounter encounter = schedule.getEncounter();
		//Concept concept = Context.getConceptService()
		//		.getConcept(OTConstants.CONCEPT_CLASS_NAME_PROCEDURE);
		Obs obs =  dao.getObsInstanceForProcedure(encounter, schedule.getOpdOrderId().getValueCoded(),schedule.getOpdOrderId().getCreatedOn());
		obs.setComment(observations);
		schedule.setStatus(OTConstants.PROCEDURE_STATUS_COMPLETED);
		return OTConstants.OBSERVATION_PROCEDURE_RETURN_SUCCESS;
	}

	public List<Concept> getProceduresMajorOT() {
		Concept concept = Context.getConceptService().getConcept(OTConstants.CONCEPT_CLASS_NAME_MAJOR_OT);
		
		Collection<ConceptAnswer> allMajorOTProcedures = null;
		List<Concept> id = new ArrayList<Concept>();
		if( concept != null )
		{
			allMajorOTProcedures = concept.getAnswers();
			for (ConceptAnswer c: allMajorOTProcedures){
				id.add(c.getAnswerConcept());
			}
			return id;
		}
		return null;
	}

	public List<OpdTestOrder> getSchedulesMajorOT(Date startDate,
			String phrase, List<Concept> procedures, int page)
			throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		List<OpdTestOrder> schedules = dao.getSchedulesMajorOT(startDate, 
				procedures, patients, page, phrase);
		return schedules;
	}

	public Integer countScheduleMajorOT(Date startDate, String phrase,
			List<Concept> procedures) throws ParseException {

		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		return dao.countScheduleMajorOT(startDate, procedures, patients, phrase);
	}

	public MajorOTProcedure getMajorOTProcedure(OpdTestOrder opdOrderId) {
		return dao.getMajorOTProcedure(opdOrderId);
	}

	public List<MajorOTProcedure> getMajorOTSchedules(Date startDate,
			String phrase, List<Concept> procedures, Integer page)
			throws ParseException {
		
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		List<MajorOTProcedure> schedules = dao.getMajorOTSchedules(startDate, 
				procedures, patients, page, phrase);
		return schedules;
	}

	public Integer countMajorOTSchedule(Date startDate, String phrase,
			List<Concept> procedures) throws ParseException {
		List<Patient> patients = Context.getPatientService()
				.getPatients(phrase);
		return dao.countMajorOTSchedule(startDate, procedures, patients, phrase);
	}

	public String observationProcedure(MajorOTProcedure schedule,
			String observations) {
		Encounter encounter = schedule.getEncounter();
		//Concept concept = Context.getConceptService()
		//		.getConcept(OTConstants.CONCEPT_CLASS_NAME_PROCEDURE);
		Obs obs =  dao.getObsInstanceForProcedure(encounter, schedule.getOpdOrderId().getValueCoded(),schedule.getOpdOrderId().getCreatedOn());
		obs.setComment(observations);
		schedule.setStatus(OTConstants.PROCEDURE_STATUS_COMPLETED);
		return OTConstants.OBSERVATION_PROCEDURE_RETURN_SUCCESS;
	}
	
	public Integer acceptProcedureMajor(OpdTestOrder schedule)
			throws ParseException {
			Encounter encounter = schedule.getEncounter();
			List<Obs> pDiagnosis;List<Obs> pFinalDiagnosis;
			MajorOTProcedure procedure = dao.getMajorOTProcedure(schedule);
			OperationTheatreService ots = (OperationTheatreService) Context
					.getService(OperationTheatreService.class);
			
			if (procedure == null) {
				procedure = new MajorOTProcedure();
				procedure.setPatient(schedule.getPatient());
				procedure.setProcedure(schedule.getValueCoded());
				procedure.setEncounter(encounter);
				procedure.setOpdOrderId(schedule);
				
				pDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("PROVISIONAL DIAGNOSIS"),schedule.getCreatedOn());
				pFinalDiagnosis = ots.getDiagnosisOTProcedure(encounter,Context.getConceptService().getConcept("FINAL DIAGNOSIS"),schedule.getCreatedOn());
				
				String pd="";
				if(pDiagnosis.size()>0){for(Obs pDiagnos:pDiagnosis){
					pd=pd+pDiagnos.getValueCoded().getName().toString()+",";
				}
				pd = pd.substring(0, pd.length()-1); }
				if(pFinalDiagnosis.size()>0){
					for(Obs pFinalDiagnos:pFinalDiagnosis){
						pd=pd+pFinalDiagnos.getValueCoded().getName().toString()+",";
					}
					pd = pd.substring(0, pd.length()-1); 
					
				}
				 
				procedure.setDiagnosis(pd);
				procedure.setOtSchedule(new Date());
				procedure.setStatus(OTConstants.PROCEDURE_STATUS_ACCEPTED);
		
				MajorOTProcedure acceptedProcedure = dao.saveOTProcedure(procedure);
				logger.info(String
						.format("Accepting a procedure [procedure=%s, patient=%s, order=%s, diagnosis=%s]",
								procedure.getProcedure().getConceptId(), procedure
										.getPatient().getPatientId(), procedure
										.getOpdOrderId(), procedure
										.getDiagnosis()));
				return acceptedProcedure.getMajorOTId();
			} else {
				logger.warn(String.format("Existing test for order=%s found.",
						schedule.getOpdOrderId()));
				return OTConstants.ACCEPT_PROCEDURE_RETURN_ERROR_EXISTING_PROCEDURE;
			}
		}

}