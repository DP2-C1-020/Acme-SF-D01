
package acme.features.sponsor.invoice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invoices.Invoice;
import acme.roles.Sponsor;
import acme.validators.ValidatorMoney;

@Service
public class SponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

	// Internal state --------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository	repository;

	@Autowired
	protected ValidatorMoney			validator;

	// AsbtractService interface ---------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int invoiceId;
		Invoice invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(invoiceId);
		status = invoice != null && invoice.isDraftMode() && // 
			super.getRequest().getPrincipal().hasRole(invoice.getSponsorship().getSponsor());

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
	public void bind(final Invoice invoice) {
		assert invoice != null;

		super.bind(invoice, "code", "dueDate", "quantity", "tax", "link");
		invoice.setLink(invoice.getLink().isEmpty() ? null : invoice.getLink());
	}

	@Override
	public void validate(final Invoice invoice) {
		assert invoice != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existing;

			existing = this.repository.findOneInvoiceByCode(invoice.getCode());
			super.state(existing == null || existing.equals(invoice), "code", "sponsor.invoice.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date minimumDueDate;
			Date maximumDueDate;
			LocalDateTime maximumDueDateLDT;

			maximumDueDateLDT = LocalDateTime.of(2100, 1, 1, 0, 1);

			maximumDueDate = Date.from(maximumDueDateLDT.atZone(ZoneId.systemDefault()).toInstant());

			minimumDueDate = MomentHelper.deltaFromMoment(invoice.getRegistrationTime(), 30, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(invoice.getDueDate(), minimumDueDate), "dueDate", "sponsor.invoice.form.error.less-than-month");
			super.state(MomentHelper.isBefore(invoice.getDueDate(), maximumDueDate), "dueDate", "sponsor.invoice.form.error.maximum-due-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {

			super.state(this.validator.moneyValidator(invoice.getQuantity().getCurrency()), "quantity", "sponsor.invoice.form.error.invalid-currency");
			super.state(invoice.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.negative-quantity");
			super.state(invoice.getQuantity().getAmount() <= 1000000, "quantity", "sponsor.invoice.form.error.maximum-quantity");
		}

		super.state(invoice.getTotalAmount().getAmount() <= 1000000, "*", "sponsor.invoice.form.error.maximum-total-amount");
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
		super.getResponse().addData(dataset);
	}

}
