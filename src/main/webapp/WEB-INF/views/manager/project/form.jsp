<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.project.form.label.code" path="code"/>	
	<acme:input-textbox code="manager.project.form.label.title" path="title"/>	
	<acme:input-textarea code="manager.project.form.label.abstracto" path="abstract"/>
	<acme:input-integer code="manager.project.form.label.cost" path="cost"/>
	<acme:input-url code="manager.project.form.label.link" path="link"/>
	<acme:input-checkbox code="manager.project.form.label.draftMode" path="draftMode"/>
	
	
<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="manager.project.form.button.userStories" action="/manager/userStory/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="manager.project.form.button.userStories" action="/manager/userStory/list?masterId=${id}"/>
			<acme:submit code="manager.project.form.button.update" action="/manager/userStory/update"/>
			<acme:submit code="manager.project.form.button.delete" action="/manager/userStory/delete"/>
			<acme:submit code="manager.project.form.button.publish" action="/manager/userStory/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.project.form.button.create" action="/manager/project/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>




