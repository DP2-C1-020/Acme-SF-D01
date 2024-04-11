<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.progressLog.list.label.recordId" path="recordId"/>
	<acme:list-column code="client.progressLog.list.label.comment" path="comment"/>
	<acme:list-column code="client.progressLog.list.label.completeness" path="completeness"/>
	<acme:list-column code="client.progressLog.list.label.draftMode" path="draftMode"/>	
</acme:list>

<acme:button code="client.progressLog.list.button.create" action="/client/progress-log/create?contractId=${contractId}"/>