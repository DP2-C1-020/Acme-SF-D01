
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
		//Integer sponsorId;

		totalInvoiceHasTaxLessEqual21 = this.repository.totalInvoiceHasTaxLessEqual21();
		totalSponsorshipHasLink = this.repository.totalSponsorshipHasLink();
		avgAmountSponsorship = this.repository.avgAmountSponsorship();
		devAmountSponsorship = this.repository.devAmountSponsorship();
		minAmountSponsorship = this.repository.minAmountSponsorship();
		maxAmountSponsorship = this.repository.maxAmountSponsorship();
		avgQuantityInvoice = this.repository.avgQuantityInvoice();
		devQuantityInvoice = this.repository.devQuantityInvoice();
		minQuantityInvoice = this.repository.minQuantityInvoice();
		maxQuantityInvoice = this.repository.maxQuantityInvoice();

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
