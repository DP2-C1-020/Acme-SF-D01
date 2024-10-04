<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
    <acme:message code="client.client-dashboard.form.title"/>
</h2>

<table class="table table-sm">
	<tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogsBelow25Percent"/>
        </th>
        <td>
            <acme:print value="${progressLogByCompletenessRate == null? '-' : progressLogByCompletenessRate['25']}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogs25To50Percent"/>
        </th>
        <td>
            <acme:print value="${progressLogByCompletenessRate == null? '-': progressLogByCompletenessRate['50']}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogs50To75Percent"/>
        </th>
        <td>
            <acme:print value="${progressLogByCompletenessRate == null? '-': progressLogByCompletenessRate['75']}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogsAbove75Percent"/>
        </th>
        <td>
            <acme:print value="${progressLogByCompletenessRate == null? '-': progressLogByCompletenessRate['100']}"/>
        </td>
    </tr>  
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.averageBudget"/>
        </th>
        <td>
            <acme:print value="${averageBudget == null? '-' : averageBudget}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.deviationBudget"/>
        </th>
        <td>
            <acme:print value="${deviationBudget == null? '-' : deviationBudget}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.minBudget"/>
        </th>
        <td>
            <acme:print value="${minBudget == null? '-' : minBudget}"/>
        </td>
    </tr>    
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.maxBudget"/>
        </th>
        <td>
            <acme:print value="${maxBudget == null? '-' : maxBudget}"/>
        </td>
    </tr>
     
</table>

<div>
    <canvas id="canvas"></canvas>
</div>

<acme:return/>