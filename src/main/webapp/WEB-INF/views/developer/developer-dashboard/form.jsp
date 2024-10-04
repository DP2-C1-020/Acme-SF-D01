<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="developer.dashboard.form.title.general-indicators" />
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.num-tm-updated" /></th>
		<td><acme:print value="${totalTrainingModulesWithUpdateMoment}" />
		</td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.num-ts-link" /></th>
		<td><acme:print value="${totalTrainingSessionWithLink}" /></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.avg-tm-time" /></th>
		<td><jstl:choose>
				<jstl:when test="${trainingModulesAverageTime != null}">
					<acme:print value="${trainingModulesAverageTime}" />
				</jstl:when>
				<jstl:when test="${trainingModulesAverageTime == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose></td>

	</tr>
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.dev-tm-time" /></th>
		<td><jstl:choose>
				<jstl:when test="${trainingModulesDeviationTime != null}">
					<acme:print value="${trainingModulesDeviationTime}" />
				</jstl:when>
				<jstl:when test="${trainingModulesDeviationTime == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.min-tm-time" /></th>
		<td><jstl:choose>
				<jstl:when test="${trainingModulesMinimumTime != null}">
					<acme:print value="${trainingModulesMinimumTime}" />
				</jstl:when>
				<jstl:when test="${trainingModulesMinimumTime == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose></td>
	</tr>
	<tr>
		<th scope="row"><acme:message
				code="developer.dashboard.form.label.max-tm-time" /></th>
		<td><jstl:choose>
				<jstl:when test="${trainingModulesMaximumTime != null}">
					<acme:print value="${trainingModulesMaximumTime}" />
				</jstl:when>
				<jstl:when test="${trainingModulesMaximumTime == null}">
					<acme:print value="N/A" />
				</jstl:when>
			</jstl:choose></td>

	</tr>
</table>
<acme:return />