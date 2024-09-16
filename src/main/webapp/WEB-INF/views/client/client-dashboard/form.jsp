<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogsBelow25Percent"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogsBelow25Percent}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogs25To50Percent"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogs25To50Percent}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogs50To75Percent"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogs50To75Percent}"/>
        </td>
    </tr>    
    <tr>
        <th scope="row">
            <acme:message code="client.clientDashboard.form.label.totalLogsAbove75Percent"/>
        </th>
        <td class="align-right">
            <acme:print value="${totalLogsAbove75Percent}"/>
        </td>
    </tr>
    
    <jstl:forEach var="s" items="${currency}">
        <h3>
            <acme:message code="client.client-dashboard.form.title.Contract"/>
            <acme:message code="${s}"/>
        </h3>

        <table class="table table-sm">
            <tr>
                <th scope="row">
                    <acme:message code="client.clientDashboard.form.label.averageBudget"/>
                </th>
                <td class="align-right">
                    <acme:print value="${averageBudget[s]}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:message code="client.clientDashboard.form.label.deviationBudget"/>
                </th>
                <td class="align-right">
                    <acme:print value="${deviationBudget[s]}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:message code="client.clientDashboard.form.label.minBudget"/>
                </th>
                <td class="align-right">
                    <acme:print value="${minBudget[s]}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:message code="client.clientDashboard.form.label.maxBudget"/>
                </th>
                <td class="align-right">
                    <acme:print value="${maxBudget[s]}"/>
                </td>
            </tr>
        </table>
    </jstl:forEach>
</table>

<style>
    .align-right {
        text-align: right;
    }
</style>

<acme:return/>
