<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.auditrecord.label.code" path="code" />
	<acme:input-moment code="any.auditrecord.label.initialMoment" path="initialMoment" />
	<acme:input-moment code="any.auditrecord.label.finalMoment" path="finalMoment" />
	<acme:input-select code="any.auditrecord.label.mark" path="mark" choices="${marks}" />
	<acme:input-url code="any.auditrecord.label.link" path="link" />
	<acme:input-textbox code="any.auditrecord.label.audit" path="codeAuditCode"/>
</acme:form>