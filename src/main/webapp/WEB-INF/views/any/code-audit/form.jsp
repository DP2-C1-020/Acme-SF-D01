<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.codeaudit.label.code" path="code" />
	<acme:input-moment code="any.codeaudit.label.execution" path="execution" />
	<acme:input-textbox code="any.codeaudit.label.modeMark" path="modeMark" placeholder="validation.codeaudit.mode.empty" />
	<acme:input-textbox code="any.codeaudit.label.correctiveActions" path="correctiveActions" />
	<acme:input-select code="any.codeaudit.label.type" path="type" choices="${types}" />
	<acme:input-url code="any.codeaudit.label.link" path="link" />
	<acme:input-select code="any.codeaudit.label.project" path="project" choices="${projects}"/>
</acme:form>

	<acme:button code="any.codeaudit.form.button.auditrecords" action="/any/audit-record/list?codeAuditId=${id}"/>