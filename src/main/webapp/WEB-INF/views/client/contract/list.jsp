<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.contract.list.label.code" path="code" width="20%"/>
	<acme:list-column code="client.contract.list.label.goals" path="goals" width="60%"/>
	<acme:list-column code="client.contract.list.label.draftMode" path="draftMode" width="20%"/>
</acme:list>

<acme:button code="client.contract.list.button.create" action="/client/contract/create"/>