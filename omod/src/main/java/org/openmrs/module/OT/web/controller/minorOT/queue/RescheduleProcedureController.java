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


package org.openmrs.module.OT.web.controller.minorOT.queue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.openmrs.api.context.Context;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.web.util.OTScheduleModel;
import org.openmrs.module.OT.web.util.OperationTheatreUtilMinor;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("OTRescheduleProcedureController")
@RequestMapping("/module/OT/rescheduleProcedure.form")
public class RescheduleProcedureController {
	
	@ModelAttribute("schedule")
	public OpdTestOrder getOrder(@RequestParam("orderId") Integer orderId) {
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		return ots.getAcceptedSchedule(orderId);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showRescheduleForm(Model model, 
			@RequestParam(value="type", required=false) String type,
			@ModelAttribute("schedule") OpdTestOrder schedule) {
		if (schedule != null) {
			OTScheduleModel otm = OperationTheatreUtilMinor.generateRescheduleModel(schedule);
			model.addAttribute("procedure", otm);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			model.addAttribute("currentDate", OperationTheatreUtilMinor.formatDate(c.getTime()));
		}		
		return "/module/OT/minorOT/rescheduleForm";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String rescheduleProcedure(Model model,
			@ModelAttribute("schedule") OpdTestOrder schedule, 
			@RequestParam("rescheduledDate") String rescheduledDateStr,
			@RequestParam("rescheduledTime") String rescheduledTimeStr) {
		if (schedule != null) {
			OperationTheatreService ots = (OperationTheatreService) Context
					.getService(OperationTheatreService.class);
			Date rescheduledDate;
			try {
				rescheduledDate = OperationTheatreUtilMinor.parseDate(rescheduledDateStr + " " + rescheduledTimeStr);
				String status = ots.rescheduleProcedure(schedule, rescheduledDate);
				model.addAttribute("status", status);
			} catch (ParseException e) {
				e.printStackTrace();
				model.addAttribute("status", "Invalid date!");
			}
		}
		return "/module/OT/minorOT/rescheduleResponse";
	}
}
