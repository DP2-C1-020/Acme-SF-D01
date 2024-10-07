<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.claim.form.label.code" path="code" placeholder="any.claim.form.placeholder.code"/>	
	<acme:input-textbox code="any.claim.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
	<acme:input-textbox code="any.claim.form.label.heading" path="heading"/>
	<acme:input-textbox code="any.claim.form.label.description" path="description"/>
	<acme:input-textbox code="any.claim.form.label.department" path="department"/>
	<jstl:if test="${draftMode == true || email != null}">
	<acme:input-email code="any.claim.form.label.email" path="email"/>
	</jstl:if>
	<jstl:if test="${draftMode == true || link != null}">
	<acme:input-url code="any.claim.form.label.link" path="link"/>
	</jstl:if>
	
	<jstl:if test="${draftMode == true}">
		<acme:input-checkbox code="any.claim.form.label.confirmation" path="confirmation"/>
		<acme:submit code="any.claim.form.button.publish" action="/any/claim/publish"/>
	</jstl:if>
</acme:form>