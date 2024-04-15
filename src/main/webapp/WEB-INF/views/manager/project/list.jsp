<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.project.list.label.code" path="code" width="10%"/>
	<acme:list-column code="manager.project.list.label.title" path="title" width="60%"/>
	<acme:list-column code="manager.project.list.label.draftMode" path="draftMode" width="15%"/>
	<acme:list-column code="manager.project.list.label.fatalErrors" path="fatalErrors" width="15%"/>
</acme:list>

<acme:button code="manager.project.list.button.create" action="/manager/project/create"/>

