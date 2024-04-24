<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.auditrecord.form.label.codeAuditCode" path="codeAuditCode" readonly="true"/>
	<acme:input-textbox code="auditor.auditrecord.form.label.code" path="code"/>
	<acme:input-textbox code="auditor.auditrecord.form.label.initialMoment" path="initialMoment" placeholder="yyyy/MM/dd HH:mm"/>
	<acme:input-textbox code="auditor.auditrecord.form.label.finalMoment" path="finalMoment" placeholder="yyyy/MM/dd HH:mm"/>
	<acme:input-url code="auditor.auditrecord.form.label.link" path="link"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
		<acme:input-select code="auditor.auditrecord.form.label.mark" path="mark" choices="${marks}"/>
			<acme:submit code="auditor.auditrecord.form.button.update" action="/auditor/audit-record/update"/>
			<acme:submit code="auditor.auditrecord.form.button.delete" action="/auditor/audit-record/delete"/>
			<acme:submit code="auditor.auditrecord.form.button.publish" action="/auditor/audit-record/publish"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
		<acme:input-textbox code="auditor.auditrecord.form.label.mark" path="mark"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="auditor.auditrecord.form.label.mark" path="mark" choices="${marks}"/>
			<acme:submit code="auditor.auditrecord.form.button.create" action="/auditor/audit-record/create?codeAuditId=${codeAuditId}"/>
		</jstl:when>		
	</jstl:choose>
	
	
</acme:form>