<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="developer.training-module.list.label.code" path="code" width="20%"/>
	<acme:list-column code="developer.training-module.list.label.details" path="details" width="20%"/>
	<acme:list-column code="developer.training-module.list.label.difficultyLevel" path="difficultyLevel" width="20%"/>	
	<acme:list-column code="developer.training-module.list.label.link" path="link" width="40%"/>	
</acme:list>


<acme:button test = "${showCreate}" code="developer.training-module.list.button.create" action="/developer/training-module/create"/>