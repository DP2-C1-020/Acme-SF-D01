<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.averageBudget"/>
		</th>
		<td>
			<acme:print value="${averageBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.deviationBudget"/>
		</th>
		<td>
			<acme:print value="${deviationBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.minBudget"/>
		</th>
		<td>
			<acme:print value="${minBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.maxBudget"/>
		</th>
		<td>
			<acme:print value="${maxBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsBelow25Percent"/>
		</th>
		<td>
			<acme:print value="${totalLogsBelow25Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogs25To50Percent"/>
		</th>
		<td>
			<acme:print value="${totalLogs25To50Percent}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogs50To75Percent"/>
		</th>
		<td>
			<acme:print value="${totalLogs50To75Percent}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsAbove75Percent"/>
		</th>
		<td>
			<acme:print value="${totalLogsAbove75Percent}"/>
		</td>
	</tr>	
</table>



<acme:return/>