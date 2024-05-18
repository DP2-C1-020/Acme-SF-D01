<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.auditrecord.list.label.code" path="code" width="20%"/>
	<acme:list-column code="auditor.auditrecord.list.label.mark" path="mark" width="40%"/>
	<acme:list-column code="auditor.auditrecord.list.label.draftmode" path="draftMode" width="40%"/>	
</acme:list>
<jstl:if test="${codeAuditDraftMode == true}">
	<acme:button code="auditor.auditrecord.list.button.create" action="/auditor/audit-record/create?codeAuditId=${codeAuditId}"/>
</jstl:if>