<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="authenticated.risk.form.label.reference" path="reference"/>  
    <acme:input-textbox code="authenticated.risk.form.label.identificationDate" path="identificationDate"/>  
    <acme:input-textbox code="authenticated.risk.form.label.impact" path="impact"/>  
    <acme:input-textbox code="authenticated.risk.form.label.probability" path="probability"/>  
    <acme:input-textarea code="authenticated.risk.form.label.description" path="description"/>
    <acme:input-url code="authenticated.risk.form.label.link" path="link"/>
    <acme:input-textbox code="authenticated.risk.form.label.projectCode" path="projectCode"/>  
</acme:form>
