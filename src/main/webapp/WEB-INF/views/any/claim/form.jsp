<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="any.claim.form.label.code" path="code"/>	
	<acme:input-textbox code="any.claim.form.label.heading" path="heading"/>
	<acme:input-textbox code="any.claim.form.label.description" path="description"/>
	<acme:input-textbox code="any.claim.form.label.department" path="department"/>
	<acme:input-textbox code="any.claim.form.label.email" path="email"/>
	<acme:input-url code="any.claim.form.label.link" path="link"/>
	
	<jstl:if test="${!readonly}">
		<acme:input-checkbox code="any.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="any.claim.form.button.create" action="/any/claim/create"/>
	</jstl:if>
</acme:form>