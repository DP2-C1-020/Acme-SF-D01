<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.project.list.label.title" path="title" width="20%"/>
	<acme:list-column code="manager.project.list.label.abstracto" path="abstracto" width="80%"/>
</acme:list>

<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="manager.project.list.button.create" action="/manager/project/create"/>
</jstl:if>		
	

