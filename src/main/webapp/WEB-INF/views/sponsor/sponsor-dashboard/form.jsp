<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="sponsor.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.total-invoice-has-tax-less-equal-21"/>
		</th>
		<td>
			<acme:print value="${totalInvoiceHasTaxLessEqual21}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.dashboard.form.label.total-sponsorship-has-link"/>
		</th>
		<td>
			<acme:print value="${totalSponsorshipHasLink}"/>
		</td>
	</tr>
</table>

<jstl:forEach var="currency" items="${supportedCurrencies}">	

    <jstl:set var="existSponsorshipsWithCurrency" value="${avgAmountSponsorship[currency]}"/>
    <jstl:set var="existInvoicesWithCurrency" value="${avgQuantityInvoice[currency]}"/>
    
	<jstl:if test="${not empty existSponsorshipsWithCurrency}">

		<h2>
			<acme:message code="sponsor.dashboard.form.title.amount-sponsorship-indicators"/>
	        <acme:message code="${currency}"/>
		</h2>
	
		<table class="table table-sm">
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.avg-amount-sponsorship"/>
				</th>
				<td>
					<acme:print value="${avgAmountSponsorship[currency]}"/>
				</td>
			</tr>	
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.dev-amount-sponsorship"/>
				</th>
				<td>
					<acme:print value="${devAmountSponsorship[currency]}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.min-amount-sponsorship"/>
				</th>
				<td>
					<acme:print value="${minAmountSponsorship[currency]}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.max-amount-sponsorship"/>
				</th>
				<td>
					<acme:print value="${maxAmountSponsorship[currency]}"/>
				</td>
			</tr>
		</table>
	</jstl:if>
	
	<jstl:if test="${not empty existInvoicesWithCurrency}">
		<h2>
			<acme:message code="sponsor.dashboard.form.title.quantity-invoice-indicators"/>
			<acme:message code="${currency}"/>
		</h2>
		
		<table class="table table-sm">
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.avg-quantity-invoice"/>
				</th>
				<td>
					<acme:print value="${avgQuantityInvoice[currency]}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.dev-quantity-invoice"/>
				</th>
				<td>
					<acme:print value="${devQuantityInvoice[currency]}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.min-quantity-invoice"/>
				</th>
				<td>
					<acme:print value="${minQuantityInvoice[currency]}"/>
				</td>
			</tr>
			<tr>
				<th scope="row">
					<acme:message code="sponsor.dashboard.form.label.max-quantity-invoice"/>
				</th>
				<td>
					<acme:print value="${maxQuantityInvoice[currency]}"/>
				</td>
			</tr>
		</table>
	</jstl:if>
</jstl:forEach>
<acme:return/>