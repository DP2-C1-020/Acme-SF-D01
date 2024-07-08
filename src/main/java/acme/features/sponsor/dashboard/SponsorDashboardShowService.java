
package acme.features.sponsor.dashboard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;
import acme.validators.ValidatorMoneyRepository;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state --------------------------------------------------

	@Autowired
	private SponsorDashboardRepository	repository;

	@Autowired
	protected ValidatorMoneyRepository	validator;

	// AbstractService interface ---------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SponsorDashboard sponsorDashboard;

		Integer sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		Integer totalInvoiceHasTaxLessEqual21 = this.repository.totalInvoiceHasTaxLessEqual21(sponsorId);
		Integer totalSponsorshipHasLink = this.repository.totalSponsorshipHasLink(sponsorId);

		List<Object[]> avgAmountSponsorshipByCurrency = this.repository.avgAmountSponsorshipByCurrency(sponsorId);
		List<Object[]> devAmountSponsorshipByCurrency = this.repository.devAmountSponsorshipByCurrency(sponsorId);
		List<Object[]> minAmountSponsorshipByCurrency = this.repository.minAmountSponsorshipByCurrency(sponsorId);
		List<Object[]> maxAmountSponsorshipByCurrency = this.repository.maxAmountSponsorshipByCurrency(sponsorId);
		List<Object[]> avgQuantityInvoiceByCurrency = this.repository.avgQuantityInvoiceByCurrency(sponsorId);
		List<Object[]> devQuantityInvoiceByCurrency = this.repository.devQuantityInvoiceByCurrency(sponsorId);
		List<Object[]> minQuantityInvoiceByCurrency = this.repository.minQuantityInvoiceByCurrency(sponsorId);
		List<Object[]> maxQuantityInvoiceByCurrency = this.repository.maxQuantityInvoiceByCurrency(sponsorId);

		Map<String, Double> avgAmountSponsorship = avgAmountSponsorshipByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> devAmountSponsorship = devAmountSponsorshipByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> minAmountSponsorship = minAmountSponsorshipByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> maxAmountSponsorship = maxAmountSponsorshipByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> avgQuantityInvoice = avgQuantityInvoiceByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> devQuantityInvoice = devQuantityInvoiceByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> minQuantityInvoice = minQuantityInvoiceByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));
		Map<String, Double> maxQuantityInvoice = maxQuantityInvoiceByCurrency.stream().collect(Collectors.toMap(object -> (String) object[0], object -> (Double) object[1]));

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
		String acceptedCurrencies = this.validator.findAcceptedCurrencies();
		String[] currencies = acceptedCurrencies.split(",\\s*");

		dataset = super.unbind(sponsorDashboard, //
			"totalInvoiceHasTaxLessEqual21", "totalSponsorshipHasLink", "avgAmountSponsorship", //
			"devAmountSponsorship", "minAmountSponsorship", "maxAmountSponsorship", //
			"avgQuantityInvoice", "devQuantityInvoice", "minQuantityInvoice", "maxQuantityInvoice");
		dataset.put("supportedCurrencies", currencies);
		super.getResponse().addData(dataset);
	}
}
