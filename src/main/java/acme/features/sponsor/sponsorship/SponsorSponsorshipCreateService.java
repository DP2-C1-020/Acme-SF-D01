
package acme.features.sponsor.sponsorship;

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

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ------------------------------------------------------

	@Autowired
	protected SponsorSponsorshipRepository repository;

	// AbstractService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Sponsorship sponsorship;
		Sponsor sponsor;
		Date moment;

		sponsor = this.repository.findOneSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		moment = MomentHelper.getCurrentMoment();

		sponsorship = new Sponsorship();
		sponsorship.setSponsor(sponsor);
		sponsorship.setMoment(moment);
		sponsorship.setDraftMode(true);

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
		sponsorship.setProject(project);
	}

	@Override
	public void validate(final Sponsorship sponsorship) {
		assert sponsorship != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findOneSponsorshipByCode(sponsorship.getCode());
			super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDate")) {
			Date minimumStartDate;

			minimumStartDate = sponsorship.getMoment();
			super.state(MomentHelper.isAfter(sponsorship.getStartDate(), minimumStartDate), "startDate", "sponsor.sponsorship.form.error.too-close");
		}

		if (!super.getBuffer().getErrors().hasErrors("endDate")) {
			Date minimumEndDate;

			if (sponsorship.getStartDate() == null)
				super.state(sponsorship.getStartDate() != null, "endDate", "sponsor.sponsorship.form.error.enter-start-date");
			else {
				minimumEndDate = MomentHelper.deltaFromMoment(sponsorship.getStartDate(), 1, ChronoUnit.MONTHS);
				super.state(MomentHelper.isAfter(sponsorship.getEndDate(), minimumEndDate), "endDate", "sponsor.sponsorship.form.error.less-than-month");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(sponsorship.getAmount().getAmount() > 0, "amount", "sponsor.sponsorship.form.error.negative-amount");
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
