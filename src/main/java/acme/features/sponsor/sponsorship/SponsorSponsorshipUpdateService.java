
package acme.features.sponsor.sponsorship;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipType;
import acme.roles.Sponsor;
import acme.validators.ValidatorMoney;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	@Autowired
	protected ValidatorMoney				validator;

	// AbstractService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean isValid;
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		isValid = MomentHelper.isAfterOrEqual(sponsorship.getEndDate(), MomentHelper.getCurrentMoment());
		status = sponsorship != null && isValid && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship sponsorship;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		super.getBuffer().addData(sponsorship);
	}

	@Override
	public void bind(final Sponsorship sponsorship) {
		assert sponsorship != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(sponsorship, "code", "startDate", "endDate", "amount", "type", "contact", "link");
		sponsorship.setContact(sponsorship.getContact().isEmpty() ? null : sponsorship.getContact());
		sponsorship.setLink(sponsorship.getLink().isEmpty() ? null : sponsorship.getLink());
		sponsorship.setProject(project);
	}

	@Override
	public void validate(final Sponsorship sponsorship) {
		assert sponsorship != null;
		Date currentDate = MomentHelper.getCurrentMoment();

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findOneSponsorshipByCode(sponsorship.getCode());
			super.state(existing == null || existing.equals(sponsorship), "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDate")) {
			Date minimumStartDate;
			Date maximumStartDate;
			LocalDateTime maximumStartDateLDT;

			maximumStartDateLDT = LocalDateTime.of(2099, 12, 2, 0, 1);

			minimumStartDate = sponsorship.getMoment();
			maximumStartDate = Date.from(maximumStartDateLDT.atZone(ZoneId.systemDefault()).toInstant());
			super.state(MomentHelper.isAfter(sponsorship.getStartDate(), minimumStartDate), "startDate", "sponsor.sponsorship.form.error.too-close");
			super.state(MomentHelper.isBefore(sponsorship.getStartDate(), maximumStartDate), "startDate", "sponsor.sponsorship.form.error.maximum-start-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("endDate")) {
			Date minimumEndDate;
			Date maximumEndDate;
			LocalDateTime maximumEndDateLDT;

			maximumEndDateLDT = LocalDateTime.of(2100, 1, 1, 0, 1);
			maximumEndDate = Date.from(maximumEndDateLDT.atZone(ZoneId.systemDefault()).toInstant());

			if (sponsorship.getStartDate() == null)
				super.state(sponsorship.getStartDate() != null, "endDate", "sponsor.sponsorship.form.error.enter-start-date");
			else {
				minimumEndDate = MomentHelper.deltaFromMoment(sponsorship.getStartDate(), 30, ChronoUnit.DAYS);
				super.state(MomentHelper.isAfterOrEqual(sponsorship.getEndDate(), minimumEndDate), "endDate", "sponsor.sponsorship.form.error.less-than-month");
				super.state(MomentHelper.isAfterOrEqual(sponsorship.getEndDate(), MomentHelper.getCurrentMoment()), "endDate", "sponsor.sponsorship.form.error.expired-date");
				super.state(MomentHelper.isBefore(sponsorship.getEndDate(), maximumEndDate), "endDate", "sponsor.sponsorship.form.error.maximum-end-date");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {

			Collection<String> currencies;
			String sponsorshipCurrency;
			boolean allCurrenciesMatch;

			currencies = this.repository.currenciesFromPublishedValidInvoicesBySponsorshipId(sponsorship.getId(), currentDate);
			sponsorshipCurrency = sponsorship.getAmount().getCurrency();
			allCurrenciesMatch = currencies.stream().allMatch(currency -> currency.equals(sponsorshipCurrency));

			super.state(this.validator.moneyValidator(sponsorship.getAmount().getCurrency()), "amount", "sponsor.sponsorship.form.error.invalid-currency");
			super.state(sponsorship.getAmount().getAmount() > 0, "amount", "sponsor.sponsorship.form.error.negative-amount");
			super.state(sponsorship.getAmount().getAmount() <= 1000000, "amount", "sponsor.sponsorship.form.error.maximum-amount");
			super.state(allCurrenciesMatch, "amount", "sponsor.sponsorship.form.error.currency-mismatch");
		}
	}

	@Override
	public void perform(final Sponsorship sponsorship) {
		assert sponsorship != null;

		this.repository.save(sponsorship);
	}

	@Override
	public void unbind(final Sponsorship sponsorship) {
		assert sponsorship != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices typeChoices;
		SelectChoices projectChoices;

		typeChoices = SelectChoices.from(SponsorshipType.class, sponsorship.getType());
		projects = this.repository.findAvailableProjects();
		projectChoices = SelectChoices.from(projects, "code", sponsorship.getProject());

		dataset = super.unbind(sponsorship, "code", "moment", "startDate", "endDate", "amount", "contact", "link", "draftMode");
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("projects", projectChoices);
		super.getResponse().addData(dataset);
	}
}
