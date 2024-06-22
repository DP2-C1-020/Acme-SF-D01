
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
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
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		status = sponsorship != null && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<Invoice> invoices;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		invoices = this.repository.findManyInvoicesBySponsorshipId(sponsorshipId);

		super.getBuffer().addData(invoices);
		super.getResponse().addGlobal("sponsorshipId", sponsorshipId);
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
