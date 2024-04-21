<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeStatic"/>
        </th>
        <td>
            <acme:print value="${totalNumberCodeAuditsTypeStatic}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeDynamic"/>
        </th>
        <td>
            <acme:print value="${totalNumberCodeAuditsTypeDynamic}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.averageNumberRecords"/>
        </th>
        <td>
            <acme:print value="${averageAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.deviationNumberRecords"/>
        </th>
        <td>
            <acme:print value="${deviationAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.minimumNumberRecords"/>
        </th>
        <td>
            <acme:print value="${minimumAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.maximumNumberRecords"/>
        </th>
        <td>
            <acme:print value="${maximumAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.averagePeriodLength"/>
        </th>
        <td>
            <acme:print value="${averageRecordPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.deviationPeriodLength"/>
        </th>
        <td>
            <acme:print value="${deviationRecordPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.minimumPeriodLength"/>
        </th>
        <td>
            <acme:print value="${minimumRecordPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.maximumPeriodLength"/>
        </th>
        <td>
            <acme:print value="${maximumRecordPeriod}"/>
        </td>
    </tr>
</table>

<acme:return/>
