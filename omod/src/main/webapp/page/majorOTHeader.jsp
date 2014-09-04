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
<div
	style="border-bottom: 1px solid black; padding-bottom: 5px; margin-bottom: 10px;">
	<ul id="menu">
		<li class="first"><span
			style="font-weight: bold; font-size: medium;">Major OT Procedure</span>
		</li>
		<openmrs:hasPrivilege privilege="Access Major OT">
            <li id="QueueId" <c:if test='<%= request.getRequestURI().contains("/majorQueue") %>'>class="active"</c:if>>
				<a href="majorQueue.form">Queue</a>
            </li>
        </openmrs:hasPrivilege>
        <openmrs:hasPrivilege privilege="Access Major OT">
            <li id="ObservationId" <c:if test='<%= request.getRequestURI().contains("/majorObservation") %>'>class="active"</c:if>>
				<a href="majorObservation.form">Enter Observations</a>
            </li>
        </openmrs:hasPrivilege>
	</ul>
</div>

<script type="text/javascript">
	
	// activate the <li> with @id by adding the css class named "active"
	function activate(id){
		jQuery("#" + id).addClass("active");		
	}
	
	// return true whether the @str contains @searchText, otherwise return false
	function contain(str, searchText){
		return str.indexOf(searchText)>-1;
	}
	
	// choose which <li> will be activated using @url
	var url = location.href;
	if(contain(url, "majorQueue.form")){
		activate("QueueId");
	} else if(contain(url, "majorObservation.form")){
		activate("ObservationId");
	}
</script>
