<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.objective.list.label.title" path="title" width="40%"/>
	<acme:list-column code="authenticated.objective.list.label.description" path="description" width="30%"/>
	<acme:list-column code="authenticated.objective.list.label.priority" path="priority" width="30%"/>	
</acme:list>

<acme:button code="authenticated.objective.list.button.create" action="/authenticated/objective/create"/>
