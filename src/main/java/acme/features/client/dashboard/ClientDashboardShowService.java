
package acme.features.client.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;
import acme.validators.ValidatorMoneyRepository;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	@Autowired
	protected ClientDashboardRepository	repository;

	@Autowired
	protected ValidatorMoneyRepository	validator;


	@Override
	public void authorise() {
		boolean status;
		Principal principal;
		int id;
		Client client;

		principal = super.getRequest().getPrincipal();
		id = principal.getAccountId();

		client = this.repository.findOneClientByUserAccountId(id);
		status = client != null && principal.hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Client client;
		ClientDashboard dashboard = new ClientDashboard();
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		client = this.repository.findOneClientByUserAccountId(userAccountId);

		String acceptedCurrencies = this.validator.findAcceptedCurrencies();
		String[] currencies = acceptedCurrencies.split(",\\s*");
		List<String> listCurrencies = new ArrayList<>();
		for (String currency : currencies)
			listCurrencies.add(currency);

		double totalLogsBelow25Percent = this.repository.findNumOfProgressLogsLess25(client);
		double totalLogs25To50Percent = this.repository.findNumOfProgressLogsWithRate25to50(client);
		double totalLogs50To75Percent = this.repository.findNumOfProgressLogsWithRate50to75(client);
		double totalLogsAbove75Percent = this.repository.findNumOfProgressLogsWithRateOver75(client);

		Map<String, Double> averagePerCurrency = this.calculateAveragePerCurrency(client, listCurrencies);
		Map<String, Double> deviationBudget = this.calculateDeviationBudget(client, listCurrencies);
		Map<String, Double> minBudget = this.calculateMinBudget(client, listCurrencies);
		Map<String, Double> maxBudget = this.calculateMaxBudget(client, listCurrencies);

		dashboard.setTotalLogsBelow25Percent(totalLogsBelow25Percent);
		dashboard.setTotalLogs25To50Percent(totalLogs25To50Percent);
		dashboard.setTotalLogs50To75Percent(totalLogs50To75Percent);
		dashboard.setTotalLogsAbove75Percent(totalLogsAbove75Percent);

		dashboard.setAverageBudget(averagePerCurrency);
		dashboard.setDeviationBudget(deviationBudget);
		dashboard.setMinBudget(minBudget);
		dashboard.setMaxBudget(maxBudget);

		super.getBuffer().addData(dashboard);
	}

	private Map<String, Double> calculateAveragePerCurrency(final Client client, final List<String> listCurrencies) {
		return listCurrencies.stream().collect(Collectors.toMap(currency -> currency, currency -> {
			Double average = this.repository.findAverageBudget(client, currency);
			return average != null ? average : 0.0;
		}));
	}

	private Map<String, Double> calculateDeviationBudget(final Client client, final List<String> listCurrencies) {
		return listCurrencies.stream().collect(Collectors.toMap(currency -> currency, currency -> {
			Double deviationBudget = this.repository.findDeviationBudget(client, currency);
			return deviationBudget != null ? deviationBudget : 0.0;
		}));
	}

	private Map<String, Double> calculateMaxBudget(final Client client, final List<String> listCurrencies) {
		return listCurrencies.stream().collect(Collectors.toMap(currency -> currency, currency -> {
			Double maxBudget = this.repository.findMaxBudget(client, currency);
			return maxBudget != null ? maxBudget : 0.0;
		}));
	}

	private Map<String, Double> calculateMinBudget(final Client client, final List<String> listCurrencies) {
		return listCurrencies.stream().collect(Collectors.toMap(currency -> currency, currency -> {
			Double minBudget = this.repository.findMinBudget(client, currency);
			return minBudget != null ? minBudget : 0.0;
		}));
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;

		String acceptedCurrencies = this.validator.findAcceptedCurrencies();
		String[] currencies = acceptedCurrencies.split(",\\s*");

		dataset = super.unbind(object, "totalLogsBelow25Percent", "totalLogs25To50Percent", "totalLogs50To75Percent", "totalLogsAbove75Percent", "averageBudget", "deviationBudget", "minBudget", "maxBudget");
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("currency", currencies);
	}
}
