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

package org.openmrs.module.OT.util;

public class OTConstants {
	private static final String MODULE_ID = "OT";
	public static final Integer PAGESIZE = 20;
	public static final String CONCEPT_CLASS_NAME_DIAGNOSIS = "PROVISIONAL DIAGNOSIS";
	public static final String CONCEPT_CLASS_NAME_MINOR_OT = "MINOR OPERATION";
	public static final Integer ACCEPT_PROCEDURE_RETURN_ERROR_EXISTING_PROCEDURE = -1;
	public static final String PROCEDURE_STATUS_ACCEPTED = "accepted";
	public static final String RESCHEDULE_PROCEDURE_RETURN_SUCCESS = "success";
	public static final String CONCEPT_CLASS_NAME_PROCEDURE = "POST FOR PROCEDURE";
	public static final String OBSERVATION_PROCEDURE_RETURN_SUCCESS = "success";
	public static final String PROCEDURE_STATUS_COMPLETED = "completed";
	public static final String CONCEPT_CLASS_NAME_MAJOR_OT = "MAJOR OPERATION";
}
