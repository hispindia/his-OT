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
<%@ include file="/WEB-INF/template/include.jsp" %>
<script type="text/javascript">
otProcedureNo = ${otProcedureNo};
</script>
<table id="myTable" class="tablesorter">
	<thead>
		<tr> 
			<th>S.No</th>
			<th>Date</th>
			<th>Patient Identifier</th>
			<th>Name</th>
			<th>Age</th>
			<th>Gender</th>
			<th>Diagnosis</th>
			<th>Procedure</th>
			<th>Enter Observations</th>		
		</tr>
	</thead>
	<tbody>
		<c:forEach var="otProcedure" items="${otProcedures}" varStatus="index">
			<c:choose>
				<c:when test="${index.count mod 2 == 0}">
					<c:set var="klass" value="odd"/>
				</c:when>					
				<c:otherwise>
					<c:set var="klass" value="even"/>
				</c:otherwise>
			</c:choose>
			<tr class="${klass}">
			<c:choose>
				<c:when test="${pagingUtil.currentPage != 1}" >
				<td>${index.count + (pagingUtil.currentPage-1)*pagingUtil.pageSize} </td>
				</c:when>
				<c:otherwise>
					<td>${index.count}</td>
				</c:otherwise>
			</c:choose>
				<td>
					${otProcedure.startDate}
				</td>
				<td>
					${otProcedure.patientIdentifier}
				</td>
				<td>
					${fn:replace(otProcedure.patientName,',',' ')}
				</td>
				<td>
					${otProcedure.age}
				</td>
				<td>
					${otProcedure.gender}
				</td>
				<td>
					${otProcedure.pDiagnosis}
				</td>
				<td>
					${otProcedure.procedure}
				</td>
				<td id="observationBox_${otProcedure.orderId}">					
					<c:choose>
						<c:when test="${otProcedure.status eq 'accepted'}">
							<a href="observationProcedureMajor.form?orderId=${otProcedure.orderId}&modal=true&height=300&width=600" class="thickbox" title="Observations">
								Enter Observations 
							</a>
						</c:when>
						<c:when test="${otProcedure.status eq 'completed'}">
							<b>Observation</b>							
						</c:when>
					</c:choose>
				</td>
			</tr>	
		</c:forEach>
	</tbody>
</table>

<div id='paging'>
	<a style="text-decoration:none" href='javascript:getProcedures(1);'>&laquo;&laquo;</a>
	<a style="text-decoration:none" href="javascript:getProcedures(${pagingUtil.prev});">&laquo;</a>		
	${pagingUtil.currentPage} / <b>${pagingUtil.numberOfPages}</b>	
	<a style="text-decoration:none" href="javascript:getProcedures(${pagingUtil.next});">&raquo;</a>
	<a style="text-decoration:none" href='javascript:getProcedures(${pagingUtil.numberOfPages});'>&raquo;&raquo;</a>
</div>