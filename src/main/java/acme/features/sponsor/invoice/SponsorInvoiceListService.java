
package acme.features.sponsor.invoice;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceListService extends AbstractService<Sponsor, Invoice> {

	// Internal state ------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface -------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean isValid;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		isValid = MomentHelper.isAfterOrEqual(sponsorship.getEndDate(), MomentHelper.getCurrentMoment());
		status = sponsorship != null && isValid && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<Invoice> invoices;
		Date currentDate;
		Sponsorship sponsorship;
		int sponsorshipId;

		currentDate = MomentHelper.getCurrentMoment();
		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		invoices = this.repository.findManyValidInvoicesBySponsorshipId(sponsorshipId, currentDate);

		super.getBuffer().addData(invoices);
		super.getResponse().addGlobal("sponsorshipId", sponsorshipId);
		super.getResponse().addGlobal("sponsorshipInDraftMode", sponsorship.isDraftMode());
	}

	@Override
	public void unbind(final Invoice invoice) {
		assert invoice != null;

		Dataset dataset;

		dataset = super.unbind(invoice, "code", "dueDate");
		dataset.put("totalAmount", invoice.getTotalAmount());
		super.getResponse().addData(dataset);
	}
}
