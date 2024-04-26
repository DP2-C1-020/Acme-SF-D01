<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.auditrecord.label.code" path="code" width="10%"/>
	<acme:list-column code="any.auditrecord.label.mark" path="mark"/>
</acme:list>