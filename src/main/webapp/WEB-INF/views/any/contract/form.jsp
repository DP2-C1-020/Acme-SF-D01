<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.contract.form.label.code" path="code"/>	
	<acme:input-textbox code="any.contract.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="any.contract.form.label.customerName" path="customerName"/>
	<acme:input-textbox code="any.contract.form.label.goals" path="goals"/>
	<acme:input-money code="any.contract.form.label.budget" path="budget"/>
</acme:form>

<acme:button code="any.progressLog.form.button.progressLog" action="/any/progress-log/list?contractId=${id}"/>