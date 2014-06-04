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
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Access Major OT" otherwise="/login.htm" redirect="/module/OT/queue.form" />
<%@ include file="../page/localHeader.jsp" %>
<%@ include file="../page/majorOTHeader.jsp" %>
<script type="text/javascript">
	otProcedureNo = 0;
	currentPage = 1;
	jQuery(document).ready(function() {
		jQuery('#date').datepicker({yearRange:'c-30:c+30', dateFormat: 'dd/mm/yy', changeMonth: true, changeYear: true});
	});
	//get all tests
	function getProcedures(currentPage){
		this.currentPage = currentPage;
		var date = jQuery("#date").val();
		var phrase = jQuery("#phrase").val();
		var procedure = jQuery("#procedure").val();
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/OT/searchProcedureMajor.form",
			data : ({
				date			: date,
				phrase			: phrase,
				procedure		: procedure,
				currentPage		: currentPage
			}),
			success : function(data) {
				jQuery("#otProcedures").html(data);	
				if(otProcedureNo>0){					
					tb_init("a.thickbox"); // init to show thickbox
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});
	}
	
	// accept a OT procedure
	function acceptProcedure(orderId) {
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/OT/ajax/acceptProcedureMajor.htm",
			data : ({
				orderId : orderId,
				date	: jQuery("#date").val(),
			}),
			dataType: "json",
			success : function(data) {
				if(data.status=="success"){
					jQuery("#acceptBox_" + orderId).html("<b>Accepted</b>");
					jQuery("#rescheduleBox_" + orderId).html("Reschedule");
				} else {
					alert(data.error);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {								
				alert(thrownError);
			}
		});		
	}
	
	// reschedule a procedure
	function rescheduleProcedure(orderId, rescheduledDate, rescheduledTime) {
		validateRescheduleDate(orderId, rescheduledDate, rescheduledTime);
	}
	
	// validate reschedule date
	function validateRescheduleDate(orderId, rescheduledDate, rescheduledTime){			
		validateRescheduleDateResult = false;
		jQuery.ajax({
			type : "GET",
			url : getContextPath() + "/module/OT/ajax/validateRescheduleDate.htm",
			data : ({				
				rescheduleDate : rescheduledDate,
				rescheduledTime: rescheduledTime
			}),
			success : function(data) {
				
				if (data.indexOf('success')>=0) {						
					jQuery.ajax({
						type : "POST",
						url : getContextPath() + "/module/OT/rescheduleProcedureMajor.form",
						data : ({
							orderId : orderId,
							rescheduledDate : rescheduledDate,
							rescheduledTime : rescheduledTime
						}),
						success : function(data) {
							if (data.indexOf('success')>=0) {
								getProcedures(currentPage);
							} else {
								alert(data);
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert(thrownError);
						}
					});
					tb_remove();
				} else {
					alert('Invalid reschedule date! It must be after the current date');
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(thrownError);
			}
		});		
	}
	
	/**
	 * RESET SEARCH FORM
	 *    Set date text box to current date
	 *    Empty the patient name/identifier textbox
	 *    Set default procedure to Procedure dropdown
	 */
	function reset(){
		jQuery("#date").val("${currentDate}");
		jQuery("#phrase").val("");
		jQuery("#procedure").val(0);
	}
</script>
<div class="boxHeader">
	<strong>Get Patient List</strong>
</div>
<div class="box">
	Date:
	<input id="date" value="${currentDate}" style="text-align:right;"/>
	Patient Identifier/Name:
	<input id="phrase"/>
	Procedure:
	<span style="overflow:auto;">
		<select name="procedure" id="procedure">
			<option value="0">Select a Major OT procedure</option>
			<c:forEach var="procedure" items="${proceduresMajor}">
				<option value="${procedure.id}">${procedure.name}</option>
			</c:forEach>	
		</select>
	</span>
	<br/>
	<input type="button" value="Get patients" onClick="getProcedures(1);"/>
	<input type="button" value="Reset" onClick="reset();"/>
</div>

<div id="otProcedures">
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>