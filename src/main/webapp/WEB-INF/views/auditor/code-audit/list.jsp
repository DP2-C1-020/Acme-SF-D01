<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.codeaudit.list.label.code" path="code" width="20%"/>
	<acme:list-column code="auditor.codeaudit.list.label.execution" path="execution" width="40%"/>
	<acme:list-column code="auditor.codeaudit.list.label.type" path="type" width="40%"/>	
</acme:list>

<acme:button code="auditor.codeaudit.list.button.create" action="/auditor/code-audit/create"/>
