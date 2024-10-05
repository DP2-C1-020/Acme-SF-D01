<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.sponsorship.form.label.code" path="code"/>
	<acme:input-moment code="any.sponsorship.form.label.moment" path="moment"/>
	<acme:input-money code="any.sponsorship.form.label.amount" path="amount"/>
	<acme:input-moment code="any.sponsorship.form.label.startDate" path="startDate"/>
	<acme:input-moment code="any.sponsorship.form.label.endDate" path="endDate"/>
	<acme:input-textbox code="any.sponsorship.form.label.type" path="type"/>
	<jstl:if test="${contact != null}">
	<acme:input-email code="any.sponsorship.form.label.contact" path="contact"/>
	</jstl:if>
	<jstl:if test="${link != null}">
	<acme:input-url code="any.sponsorship.form.label.link" path="link"/>
	</jstl:if>
	<acme:input-textbox code="any.sponsorship.form.label.project" path="project"/>
</acme:form>