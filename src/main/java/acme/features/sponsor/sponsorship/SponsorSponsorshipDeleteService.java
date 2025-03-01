
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invoices.Invoice;
import acme.entities.project.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

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
		sponsorship.setProject(project);
	}

	@Override
	public void validate(final Sponsorship sponsorship) {
		assert sponsorship != null;
	}

	@Override
	public void perform(final Sponsorship sponsorship) {
		assert sponsorship != null;
		Date currentDate = MomentHelper.getCurrentMoment();
		Collection<Invoice> invoices;

		invoices = this.repository.findManyValidInvoicesBySponsorshipId(sponsorship.getId(), currentDate);
		this.repository.deleteAll(invoices);
		this.repository.delete(sponsorship);
	}

}
