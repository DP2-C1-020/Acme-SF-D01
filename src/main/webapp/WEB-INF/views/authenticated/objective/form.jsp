<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
<acme:input-textbox code="authenticated.objective.form.label.instantiationMoment" path="instantiationMoment"/>  
    <acme:input-textbox code="authenticated.objective.form.label.title" path="title"/>  
    <acme:input-textarea code="authenticated.objective.form.label.description" path="description"/>  
    <acme:input-select code="authenticated.objective.form.label.priority" path="priority" choices="${statuses}"/>  
    <acme:input-textbox code="authenticated.objective.form.label.status" path="status"/>
    <acme:input-textbox code="authenticated.objective.form.label.startDate" path="startDate"/>  
    <acme:input-textbox code="authenticated.objective.form.label.endDate" path="endDate"/>  
    <acme:input-url code="authenticated.objective.form.label.link" path="link"/>  
</acme:form>
