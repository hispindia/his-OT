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

package org.openmrs.module.OT.db;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.model.MajorOTProcedure;
import org.openmrs.module.OT.model.MinorOTProcedure;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;

/**
 *  Database methods for {@link OperationTheatreService}.
 */
public interface OperationTheatreDAO {
	
	/*
	 * Add DAO methods here
	 */
	
	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param page
	 * @param phrase 
	 * @return List<OpdTestOrder>
	 * @throws ParseException
	 */
	public List<OpdTestOrder> getSchedulesMinorOT(Date scheduleDate, List<Concept> procedures,
			List<Patient> patients, int page, String phrase) throws ParseException;
	
	/**
	 * 
	 * @param scheduleDate
	 * @param procedures
	 * @param patients
	 * @param phrase 
	 * @return Integer
	 * @throws ParseException
	 */
	public Integer countScheduleMinorOT(Date scheduleDate, List<Concept> procedures,
			List<Patient> patients, String phrase) throws ParseException;
	
	/**
	 * 
	 * @param encounterId
	 * @param concept
	 * @return Obs
	 */
	public List<Obs> getObsInstanceForDiagnosis(Encounter encounter,Concept concept,Date date);
	
	public Obs getObsInstanceForProcedure(Encounter encounter,Concept valueCoded,Date date);
	
	/**
	 * 
	 * @param orderId
	 * @return OpdTestOrder
	 */
	public OpdTestOrder getAcceptedSchedule(Integer orderId);
	
	/**
	 * 
	 * @param opdOrderId
	 * @return
	 */
	public MinorOTProcedure getMinorOTProcedure(OpdTestOrder opdOrderId);
	
	/**
	 * 
	 * @param procedure
	 * @return
	 */
	public MinorOTProcedure saveOTProcedure(MinorOTProcedure procedure);

	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param page
	 * @param phrase 
	 * @return List
	 */
	public List<MinorOTProcedure> getMinorOTSchedules(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, Integer page, String phrase) throws ParseException;

	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param phrase 
	 * @return Integer
	 */
	public Integer countMinorOTSchedule(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, String phrase) throws ParseException;

	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param page
	 * @param phrase 
	 * @return List<OpdTestOrder>
	 * @throws ParseException
	 */
	public List<OpdTestOrder> getSchedulesMajorOT(Date scheduleDate, List<Concept> procedures,
			List<Patient> patients, int page, String phrase) throws ParseException;
	
	/**
	 * 
	 * @param scheduleDate
	 * @param procedures
	 * @param patients
	 * @param phrase 
	 * @return Integer
	 * @throws ParseException
	 */
	public Integer countScheduleMajorOT(Date scheduleDate, List<Concept> procedures,
			List<Patient> patients, String phrase) throws ParseException;
	
	
	/**
	 * 
	 * @param opdOrderId
	 * @return
	 */
	public MajorOTProcedure getMajorOTProcedure(OpdTestOrder opdOrderId);
	
	/**
	 * 
	 * @param procedure
	 * @return
	 */
	public MajorOTProcedure saveOTProcedure(MajorOTProcedure procedure);

	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param page
	 * @param phrase 
	 * @return List
	 */
	public List<MajorOTProcedure> getMajorOTSchedules(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, Integer page, String phrase) throws ParseException;

	/**
	 * 
	 * @param startDate
	 * @param procedures
	 * @param patients
	 * @param phrase 
	 * @return Integer
	 */
	public Integer countMajorOTSchedule(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, String phrase) throws ParseException;}