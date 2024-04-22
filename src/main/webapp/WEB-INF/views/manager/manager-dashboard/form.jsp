<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityMust" path="totalNumberUserStoriesPriorityMust" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityShould" path="totalNumberUserStoriesPriorityShould" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityCould" path="totalNumberUserStoriesPriorityCould" readonly="true"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityWont" path="totalNumberUserStoriesPriorityWont" readonly="true"/>
	
	<acme:input-double code="manager.dashboard.form.label.avgUserStoryCost" path="avgUserStoryCost" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.devUserStoryCost" path="devUserStoryCost" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.minUserStoryCost" path="minUserStoryCost" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.maxUserStoryCost" path="maxUserStoryCost" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.avgProjectCost" path="avgProjectCost" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.devProjectCost" path="devProjectCost" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.minProjectCost" path="minProjectCost" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.maxProjectCost" path="maxProjectCost" readonly="true" placeholder="--"/>
</acme:form>
