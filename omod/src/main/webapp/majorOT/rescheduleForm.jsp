<%--
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
--%>

<%@ include file="/WEB-INF/template/include.jsp"%>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#rescheduledDate').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
    });
	
	jQuery(document).ready(function () {
		jQuery('#rescheduledTime').ptTimeSelect();
	});	
</script>

<style>
	.info {
		font-weight: bold;
		text-align:right
	}
</style>

<center>
	<c:choose>
		<c:when test="${not empty procedure}">
			<table class="procedureInfo" cellspacing="15">
				<tr>
					<td class='info'>Patient Name</td>
					<td>${fn:replace(procedure.patientName,',',' ')}</td>
					<td></td>
					<td class='info'>Patient Identifier</td>
					<td>${procedure.patientIdentifier}</td>
				</tr>
				<tr>
					<td class='info'>Date</td>
					<td>${procedure.startDate}</td>
					<td width="30px"></td>
					<td class='info'>Procedure</td>
					<td>${procedure.procedure}</td>
				</tr>
				<tr>
					<td class='info'>Gender</td>
					<td>${procedure.gender}</td>
					<td></td>
					<td class='info'>Age</td>
					<td>${procedure.age}</td>
				</tr>
			</table>
			<b>Reschedule Date</b>: <input id="rescheduledDate" value="${currentDate}" style="text-align:right;"/>
			<b>&nbsp;&nbsp;Reschedule Time</b>: <input id="rescheduledTime" value="12:00 PM" style="text-align:right;"/><br/><br/>
			<input type="button" onClick="javascript:window.parent.rescheduleProcedure(${procedure.orderId}, $('#rescheduledDate').val(), $('#rescheduledTime').val()); tb_remove();" value="Reschedule" />
			<input type="button" onClick="tb_remove();" value="Cancel" />
		</c:when>
		<c:otherwise>
			Not found<br />
			<input type="button" onClick="tb_remove();" value="Cancel" />
		</c:otherwise>
	</c:choose>
</center>