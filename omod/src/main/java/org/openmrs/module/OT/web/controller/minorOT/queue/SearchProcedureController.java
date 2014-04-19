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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.OT.OperationTheatreService;
import org.openmrs.module.OT.util.OTConstants;
import org.openmrs.module.OT.util.PagingUtil;
import org.openmrs.module.OT.web.util.OTScheduleModel;
import org.openmrs.module.OT.web.util.OperationTheatreUtilMinor;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("MinorOTSearchProcedureController")
@RequestMapping("/module/OT/searchProcedure.form")
public class SearchProcedureController {
	@RequestMapping(method = RequestMethod.GET)
	public String searchTest(
			@RequestParam(value = "date", required = false) String dateStr,
			@RequestParam(value = "phrase", required = false) String phrase,
			@RequestParam(value = "procedure", required = false) Integer procedureId,
			@RequestParam(value = "currentPage", required = false) Integer currentPage,
			HttpServletRequest request, Model model) {
	
		OperationTheatreService ots = (OperationTheatreService) Context
				.getService(OperationTheatreService.class);
		
		List<Concept> procedures = new ArrayList<Concept>();
		Concept procedure = Context.getConceptService().getConcept(procedureId);
		if (procedure != null) {
			if (procedures.isEmpty())
				procedures.add(procedure);
			else {
				procedures.clear();
				procedures.add(procedure);
			}
		}
		else {
			if (procedures.isEmpty())
				procedures = ots.getProceduresMinorOT();
			else {
				procedures.clear();
				procedures = ots.getProceduresMinorOT();
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
			if (currentPage == null)
				currentPage = 1;	
			List<OpdTestOrder> schedules = ots.getSchedulesMinorOT(date, phrase, procedures, 
					currentPage);
			List<OTScheduleModel> otProcedures = OperationTheatreUtilMinor.generateModelsFromSchedules(
					schedules);

			int total = ots.countScheduleMinorOT(date, phrase, procedures);
			PagingUtil pagingUtil = new PagingUtil(OTConstants.PAGESIZE, currentPage,
					total);
			model.addAttribute("pagingUtil", pagingUtil);
			model.addAttribute("otProcedures", otProcedures);
			model.addAttribute("otProcedureNo", otProcedures.size());
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Error when parsing schedule date!");
			return null;
		}
		return "/module/OT/minorOT/search";
	}
}
