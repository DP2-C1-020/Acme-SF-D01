
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
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
public class SponsorInvoiceCreateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ------------------------------------------------------

	@Autowired
	protected SponsorInvoiceRepository repository;

	// AbstractService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Invoice invoice;
		Sponsorship sponsorship;
		int sponsorshipId;
		Date moment;
		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		moment = MomentHelper.getCurrentMoment();

		invoice = new Invoice();
		invoice.setSponsorship(sponsorship);
		invoice.setRegistrationTime(moment);
		invoice.setDraftMode(true);

		super.getBuffer().addData(invoice);
	}

	@Override
	public void bind(final Invoice invoice) {
		assert invoice != null;

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		super.bind(invoice, "code", "dueDate", "quantity", "tax", "link");
		invoice.setSponsorship(sponsorship);
	}

	@Override
	public void validate(final Invoice invoice) {
		assert invoice != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existing;

			existing = this.repository.findOneInvoiceByCode(invoice.getCode());
			super.state(existing == null, "code", "sponsor.invoice.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date minimumDueDate;

			minimumDueDate = MomentHelper.deltaFromMoment(invoice.getRegistrationTime(), 1, ChronoUnit.MONTHS);
			super.state(MomentHelper.isAfter(invoice.getDueDate(), minimumDueDate), "dueDate", "sponsor.invoice.form.error.less-than-month");

		}
		if (!super.getBuffer().getErrors().hasErrors("quantity"))
			super.state(invoice.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.negative-quantity");
	}

	@Override
	public void perform(final Invoice invoice) {
		assert invoice != null;

		this.repository.save(invoice);
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
