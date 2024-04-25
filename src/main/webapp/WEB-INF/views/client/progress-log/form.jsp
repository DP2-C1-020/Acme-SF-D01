<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.progressLog.form.label.recordId" path="recordId"/>	
	<acme:input-textbox code="client.progressLog.form.label.completeness" path="completeness"/>
	<acme:input-textbox code="client.progressLog.form.label.comment" path="comment"/>
	<acme:input-textbox code="client.progressLog.form.label.responsiblePerson" path="responsiblePerson"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:input-moment code="client.progressLog.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
			<acme:input-textbox code="client.progressLog.form.label.contractCode" path="contractCode" readonly="true"/>
			<acme:submit code="client.progressLog.form.button.update" action="/client/progress-log/update"/>
			<acme:submit code="client.progressLog.form.button.delete" action="/client/progress-log/delete"/>
			<acme:submit code="client.progressLog.form.button.publish" action="/client/progress-log/publish"/>
		</jstl:when>
	
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-moment code="client.progressLog.form.label.registrationMoment" path="registrationMoment"/>
			<acme:input-textbox code="client.progressLog.form.label.contractCode" path="contractCode"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-moment code="client.progressLog.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
			<acme:submit code="client.progressLog.list.button.create" action="/client/progress-log/create?contractId=${contractId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>