<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.codeaudit.label.code" path="code" width="10%"/>
	<acme:list-column code="any.codeaudit.label.execution" path="execution" width="10%"/>
	<acme:list-column code="any.codeaudit.label.type" path="type" width="10%"/>
	<acme:list-column code="any.codeaudit.label.markMode" path="markMode"/>
</acme:list>