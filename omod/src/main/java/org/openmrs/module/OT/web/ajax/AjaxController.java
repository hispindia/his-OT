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

package org.openmrs.module.OT.web.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.util.OTConstants;
import org.openmrs.module.OT.web.util.OperationTheatreUtilMinor;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("OTAjaxController")
public class AjaxController {
	/**
	 * Accept a OT procedure
	 * 
	 * @param orderId
	 * @param date
	 * @param model
	 * @return id of accepted OT procedure
	 */
	@RequestMapping(value = "/module/OT/ajax/acceptProcedureMinor.htm", method = RequestMethod.GET)
	public String acceptTest(@RequestParam("orderId") Integer orderId,
			@RequestParam("date") String dateStr, Model model) {
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		OpdTestOrder schedule = ots.getAcceptedSchedule(orderId);
		if (schedule != null) {
			try {
				Integer acceptedProcedureId = ots.acceptProcedureMinor(schedule);
				model.addAttribute("acceptedProcedureId", acceptedProcedureId);
				if (acceptedProcedureId > 0) {
					model.addAttribute("status", "success");
				} else {
					model.addAttribute("status", "fail");
					if (acceptedProcedureId == OTConstants.ACCEPT_PROCEDURE_RETURN_ERROR_EXISTING_PROCEDURE) {
						model.addAttribute("error",
								"Existing accepted Procedure found");
					}
				}
			} catch (Exception e) {
				model.addAttribute("acceptedProcedureId", "0");
			}
		}
		return "/module/OT/ajax/acceptProcedure";
	}
	
	@RequestMapping(value = "/module/OT/ajax/validateRescheduleDate.htm", method = RequestMethod.GET)	
	public void validateRescheduleDate(@RequestParam("rescheduleDate") String rescheduleDateStr, 
			@RequestParam("rescheduledTime") String rescheduledTimeStr, HttpServletResponse response) throws IOException, ParseException{
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		
		Date rescheduleDate = OperationTheatreUtilMinor.parseDate(rescheduleDateStr + " " + rescheduledTimeStr);
		Date now = new Date();
		String currentDateStr = OperationTheatreUtilMinor.formatDate(now) + " 12:00 AM";
		Date currentDate = OperationTheatreUtilMinor.parseDate(currentDateStr);
		if(rescheduleDate.after(currentDate))
			writer.print("success");
		else
			writer.print("fail");
	}
	
	/**
	 * Accept a OT procedure
	 * 
	 * @param orderId
	 * @param date
	 * @param model
	 * @return id of accepted OT procedure
	 */
	@RequestMapping(value = "/module/OT/ajax/acceptProcedureMajor.htm", method = RequestMethod.GET)
	public String acceptTestMajor(@RequestParam("orderId") Integer orderId,
			@RequestParam("date") String dateStr, Model model) {
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		OpdTestOrder schedule = ots.getAcceptedSchedule(orderId);
		if (schedule != null) {
			try {
				Integer acceptedProcedureId = ots.acceptProcedureMajor(schedule);
				model.addAttribute("acceptedProcedureId", acceptedProcedureId);
				if (acceptedProcedureId > 0) {
					model.addAttribute("status", "success");
				} else {
					model.addAttribute("status", "fail");
					if (acceptedProcedureId == OTConstants.ACCEPT_PROCEDURE_RETURN_ERROR_EXISTING_PROCEDURE) {
						model.addAttribute("error",
								"Existing accepted Procedure found");
					}
				}
			} catch (Exception e) {
				model.addAttribute("acceptedProcedureId", "0");
			}
		}
		return "/module/OT/ajax/acceptProcedureMajor";
	}
}
