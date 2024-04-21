<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.contract.form.label.code" path="code"/>
	<acme:input-textbox code="client.contract.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="client.contract.form.label.customerName" path="customerName"/>
	<acme:input-textbox code="client.contract.form.label.goals" path="goals"/>
	<acme:input-money code="client.contract.form.label.budget" path="budget"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
			<acme:input-textbox code="client.contract.form.label.projectCode" path="projectCode" readonly="true"/>
			<acme:submit code="client.contract.form.button.update" action="/client/contract/update"/>
			<acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
			<acme:submit code="client.contract.form.button.publish" action="/client/contract/publish"/>
		</jstl:when>
	
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment"/>
			<acme:input-textbox code="client.contract.form.label.projectCode" path="projectCode"/>
			<acme:button code="client.progressLog.form.button.progressLog" action="/client/progress-log/list?contractId=${id}"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
			<acme:input-select code="client.contract.form.label.projects" path="project" choices="${projects}"/>
			<acme:submit code="client.contract.form.button.create" action="/client/contract/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>