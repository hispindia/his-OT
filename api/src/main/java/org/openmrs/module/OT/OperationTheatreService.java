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

package org.openmrs.module.OT;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.OT.model.MajorOTProcedure;
import org.openmrs.module.OT.model.MinorOTProcedure;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(OperationTheatreService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface OperationTheatreService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */
	
	/**
	 * Return all minor OT procedures concepts
	 * 
	 * @return List<Concept>
	 **/
	public List<Concept> getProceduresMinorOT();
	
	/**
	 * Find minorOT schedules
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @param page
	 * @return List<OpdTestOrder> 
	 * @throws ParseException
	 */
	public List<OpdTestOrder> getSchedulesMinorOT(Date startDate, String phrase,
			List<Concept> procedures, int page) throws ParseException;
	
	/**
	 * Count Minor OT schedules
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @return Integer
	 * @throws ParseException
	 */
	public Integer countScheduleMinorOT(Date startDate, String phrase, 
			List<Concept> procedures) throws ParseException;

	/**
	 * Returns a diagnosis related to OT procedure
	 * 
	 * @param encounterId
	 * @return String
	 */
	public List<Obs> getDiagnosisOTProcedure(Encounter encounter,Concept concept,Date date);

	/**
	 * 
	 * @param orderId
	 * @return OpdTestOrder
	 */
	public OpdTestOrder getAcceptedSchedule(Integer orderId);
	
	/**
	 * 
	 * @param schedule
	 * @return Integer
	 */
	public Integer acceptProcedureMinor(OpdTestOrder schedule) throws ParseException;

	/**
	 * 
	 * @param schedule
	 * @param rescheduledDate
	 * @return
	 */
	public String rescheduleProcedure(OpdTestOrder schedule,
			Date rescheduledDate);

	/**
	 * 
	 * @param OrderId
	 * @return OTProcedure
	 */
	public MinorOTProcedure getMinorOTProcedure(OpdTestOrder opdOrderId);

	/**
	 * 
	 * @param date
	 * @param phrase
	 * @param procedures
	 * @param currentPage
	 * @return List
	 */
	public List<MinorOTProcedure> getMinorOTSchedules(Date startDate, String phrase,
			List<Concept> procedures, Integer page) throws ParseException;

	/**
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @return integer
	 */
	public Integer countMinorOTSchedule(Date startDate, String phrase,
			List<Concept> procedures) throws ParseException;

	/**
	 * 
	 * @param schedule
	 * @param observations
	 * @return
	 */
	public String observationProcedure(MinorOTProcedure schedule,
			String observations);
	
	/**
	 * Return all major OT procedures concepts
	 * 
	 * @return List<Concept>
	 **/
	public List<Concept> getProceduresMajorOT();
	
	/**
	 * Find MajorOT schedules
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @param page
	 * @return List<OpdTestOrder> 
	 * @throws ParseException
	 */
	public List<OpdTestOrder> getSchedulesMajorOT(Date startDate, String phrase,
			List<Concept> procedures, int page) throws ParseException;
	
	/**
	 * Count Major OT schedules
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @return Integer
	 * @throws ParseException
	 */
	public Integer countScheduleMajorOT(Date startDate, String phrase, 
			List<Concept> procedures) throws ParseException;

	

	/**
	 * 
	 * @param OrderId
	 * @return OTProcedure
	 */
	public MajorOTProcedure getMajorOTProcedure(OpdTestOrder opdOrderId);

	/**
	 * 
	 * @param date
	 * @param phrase
	 * @param procedures
	 * @param currentPage
	 * @return List
	 */
	public List<MajorOTProcedure> getMajorOTSchedules(Date startDate, String phrase,
			List<Concept> procedures, Integer page) throws ParseException;

	/**
	 * 
	 * @param startDate
	 * @param phrase
	 * @param procedures
	 * @return integer
	 */
	public Integer countMajorOTSchedule(Date startDate, String phrase,
			List<Concept> procedures) throws ParseException;

	/**
	 * 
	 * @param schedule
	 * @param observations
	 * @return
	 */
	public String observationProcedure(MajorOTProcedure schedule,
			String observations);
	
	/**
	 * 
	 * @param schedule
	 * @return Integer
	 */
	public Integer acceptProcedureMajor(OpdTestOrder schedule) throws ParseException;


}