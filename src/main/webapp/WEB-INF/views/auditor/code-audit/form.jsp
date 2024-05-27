<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.codeaudit.form.label.code" path="code"/>
	<acme:input-textbox code="auditor.codeaudit.form.label.execution" path="execution" readonly="true"/>
	
	<acme:input-textbox code="auditor.codeaudit.form.label.correctiveActions" path="correctiveActions"/>
	<acme:input-select code="auditor.codeaudit.form.label.type" path="type" choices="${types}"/>
	<acme:input-url code="auditor.codeaudit.form.label.link" path="link"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
		<acme:input-textbox code="auditor.codeaudit.form.label.modeMark" path="modeMark" placeholder="validation.codeaudit.mode.empty" readonly="true"/>
			<acme:input-textbox code="auditor.codeaudit.form.label.projects" path="project" readonly="true"/>
			<acme:submit code="auditor.codeaudit.form.button.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeaudit.form.button.delete" action="/auditor/code-audit/delete"/>
			<acme:submit code="auditor.codeaudit.form.button.publish" action="/auditor/code-audit/publish"/>
			<acme:button code="auditor.codeaudit.form.button.auditrecords" action="/auditor/audit-record/list?codeAuditId=${id}"/>
		</jstl:when>
	
		<jstl:when test="${acme:anyOf(_command, 'show|update') && draftMode == false}">
		<acme:input-textbox code="auditor.codeaudit.form.label.modeMark" path="modeMark" placeholder="validation.codeaudit.mode.empty" readonly="true"/>
			<acme:input-textbox code="auditor.codeaudit.form.label.projects" path="project" readonly="true"/>
			<acme:button code="auditor.codeaudit.form.button.auditrecords" action="/auditor/audit-record/list?codeAuditId=${id}"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="auditor.codeaudit.form.label.projects" path="project" choices="${projects}"/>
			<acme:submit code="auditor.codeaudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>		
	</jstl:choose>
	
	
	
</acme:form>