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
			style="font-weight: bold; font-size: large;">Operation Theatre</span>
		</li>
		<openmrs:hasPrivilege privilege="Access Minor OT">
            <li id="MinorOtId" <c:if test='<%= request.getRequestURI().contains("/minorOT") %>'>class="active"</c:if>>
				<a href="minorOT.form">Minor OT Procedures</a>
            </li>
        </openmrs:hasPrivilege>
        <openmrs:hasPrivilege privilege="Access Major OT">
            <li id="MajorOtId" <c:if test='<%= request.getRequestURI().contains("/majorOT") %>'>class="active"</c:if>>
				<a href="majorOT.form">Major OT Procedures</a>
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
	if(contain(url, "minorOT.form")){
		activate("MinorOtId");
	} else if(contain(url, "majorOT.form")){
		activate("MajorOtId");
	}
	
	
	// get the context path
	function getContextPath() {
		pn = location.pathname;
		len = pn.indexOf("/", 1);
		cp = pn.substring(0, len);
		return cp;
	}
</script>
