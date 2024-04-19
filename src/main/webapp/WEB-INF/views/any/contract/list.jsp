<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.contract.list.label.code" path="code" width="0%"/>
	<acme:list-column code="any.contract.list.label.providerName" path="providerName" width="0%"/>
	<acme:list-column code="any.contract.list.label.budget" path="budget" width="70%"/>	
</acme:list>
