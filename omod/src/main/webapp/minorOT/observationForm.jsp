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

<style>
	.info {
		font-weight: bold;
		text-align:left;
		font-size: 16px
	}
	.ui-button {
		font-size: 12px
	}
</style>
<div class="info">Observations:</div>
<br/>

<center>
	<c:choose>
		<c:when test="${not empty procedure}">
			<textarea id="observations" rows="15" cols="81" /><br/><br/>
			<div align="right">
				<input type="button" onclick="javascript:window.parent.observationProcedure(${procedure.orderId}, $('#observations').val()); tb_remove();" 
					class="ui-button ui-widget ui-state-default ui-corner-all" value="Submit"/>
				<input type="button" onClick="tb_remove();" class="ui-button ui-widget ui-state-default ui-corner-all" value="Cancel" />
			</div>
		</c:when>
		<c:otherwise>
			Not found<br />
			<input type="button" onClick="tb_remove();" class="ui-button ui-widget ui-state-default ui-corner-all" value="Cancel" />
		</c:otherwise>
	</c:choose>
</center>