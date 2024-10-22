
package acme.features.sponsor.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	// Internal state ----------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean isSponsorshipValid;
		boolean isInvoiceValid;
		int invoiceId;
		Invoice invoice;
		Sponsorship sponsorship;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(invoiceId);
		sponsorship = this.repository.findOneSponsorshipById(invoice.getSponsorship().getId());
		isSponsorshipValid = MomentHelper.isAfterOrEqual(sponsorship.getEndDate(), MomentHelper.getCurrentMoment());
		isInvoiceValid = MomentHelper.isAfterOrEqual(invoice.getDueDate(), MomentHelper.getCurrentMoment());
		status = invoice != null && isSponsorshipValid && isInvoiceValid && super.getRequest().getPrincipal().hasRole(invoice.getSponsorship().getSponsor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice invoice;
		int invoiceId;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(invoiceId);

		super.getBuffer().addData(invoice);
	}

	@Override
	public void unbind(final Invoice invoice) {
		assert invoice != null;

		Dataset dataset;

		dataset = super.unbind(invoice, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("sponsorshipId", invoice.getSponsorship().getId());
		super.getResponse().addData(dataset);
	}
}
