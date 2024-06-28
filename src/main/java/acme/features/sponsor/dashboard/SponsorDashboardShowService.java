
package acme.features.sponsor.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state --------------------------------------------------

	@Autowired
	private SponsorDashboardRepository repository;

	// AbstractService interface ---------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SponsorDashboard sponsorDashboard;
		Integer totalInvoiceHasTaxLessEqual21;
		Integer totalSponsorshipHasLink;
		Double avgAmountSponsorship;
		Double devAmountSponsorship;
		Double minAmountSponsorship;
		Double maxAmountSponsorship;
		Double avgQuantityInvoice;
		Double devQuantityInvoice;
		Double minQuantityInvoice;
		Double maxQuantityInvoice;
		Integer sponsorId;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		totalInvoiceHasTaxLessEqual21 = this.repository.totalInvoiceHasTaxLessEqual21(sponsorId);
		totalSponsorshipHasLink = this.repository.totalSponsorshipHasLink(sponsorId);
		avgAmountSponsorship = this.repository.avgAmountSponsorship(sponsorId);
		devAmountSponsorship = this.repository.devAmountSponsorship(sponsorId);
		minAmountSponsorship = this.repository.minAmountSponsorship(sponsorId);
		maxAmountSponsorship = this.repository.maxAmountSponsorship(sponsorId);
		avgQuantityInvoice = this.repository.avgQuantityInvoice(sponsorId);
		devQuantityInvoice = this.repository.devQuantityInvoice(sponsorId);
		minQuantityInvoice = this.repository.minQuantityInvoice(sponsorId);
		maxQuantityInvoice = this.repository.maxQuantityInvoice(sponsorId);

		sponsorDashboard = new SponsorDashboard();
		sponsorDashboard.setTotalInvoiceHasTaxLessEqual21(totalInvoiceHasTaxLessEqual21);
		sponsorDashboard.setTotalSponsorshipHasLink(totalSponsorshipHasLink);
		sponsorDashboard.setAvgAmountSponsorship(avgAmountSponsorship);
		sponsorDashboard.setDevAmountSponsorship(devAmountSponsorship);
		sponsorDashboard.setMinAmountSponsorship(minAmountSponsorship);
		sponsorDashboard.setMaxAmountSponsorship(maxAmountSponsorship);
		sponsorDashboard.setAvgQuantityInvoice(avgQuantityInvoice);
		sponsorDashboard.setDevQuantityInvoice(devQuantityInvoice);
		sponsorDashboard.setMinQuantityInvoice(minQuantityInvoice);
		sponsorDashboard.setMaxQuantityInvoice(maxQuantityInvoice);

		super.getBuffer().addData(sponsorDashboard);
	}

	@Override
	public void unbind(final SponsorDashboard sponsorDashboard) {
		Dataset dataset;

		dataset = super.unbind(sponsorDashboard, //
			"totalInvoiceHasTaxLessEqual21", "totalSponsorshipHasLink", "avgAmountSponsorship", //
			"devAmountSponsorship", "minAmountSponsorship", "maxAmountSponsorship", //
			"avgQuantityInvoice", "devQuantityInvoice", "minQuantityInvoice", "maxQuantityInvoice");

		super.getResponse().addData(dataset);
	}
}
