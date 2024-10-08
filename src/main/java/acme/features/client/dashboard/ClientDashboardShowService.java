
package acme.features.client.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

		int totalLogsBelow25Percent = this.repository.findNumOfProgressLogsLess25(client);
		int totalLogs25To50Percent = this.repository.findNumOfProgressLogsWithRate25to50(client);
		int totalLogs50To75Percent = this.repository.findNumOfProgressLogsWithRate50to75(client);
		int totalLogsAbove75Percent = this.repository.findNumOfProgressLogsWithRateOver75(client);

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

	private Map<String, Double> calculateAveragePerCurrency(final Client client, final List<String> currencies) {
		Map<String, Double> averagePerCurrency = new HashMap<>();
		for (String currency : currencies) {
			Double averageBudgetOfContracts = this.repository.findAverageBudget(client, currency);
			averagePerCurrency.put(currency, averageBudgetOfContracts != null ? averageBudgetOfContracts : null);
		}
		return averagePerCurrency;
	}

	private Map<String, Double> calculateDeviationBudget(final Client client, final List<String> currencies) {
		Map<String, Double> deviationPerCurrency = new HashMap<>();
		for (String currency : currencies) {
			Double deviationBudgetPerCurrency = this.repository.findDeviationBudget(client, currency);
			deviationPerCurrency.put(currency, deviationBudgetPerCurrency != null ? deviationBudgetPerCurrency : null);
		}
		return deviationPerCurrency;
	}

	private Map<String, Double> calculateMinBudget(final Client client, final List<String> currencies) {
		Map<String, Double> minimunBudgetOfContracts = new HashMap<>();
		for (String currency : currencies) {
			Double minContract = this.repository.findMinBudget(client, currency);
			minimunBudgetOfContracts.put(currency, minContract != null ? minContract : null);
		}
		return minimunBudgetOfContracts;
	}

	private Map<String, Double> calculateMaxBudget(final Client client, final List<String> currencies) {
		Map<String, Double> maximumBudgetOfContracts = new HashMap<>();
		for (String currency : currencies) {
			Double maxContract = this.repository.findMaxBudget(client, currency);
			maximumBudgetOfContracts.put(currency, maxContract != null ? maxContract : null);
		}
		return maximumBudgetOfContracts;
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;

		String nullValues;
		Locale local;

		local = super.getRequest().getLocale();
		nullValues = local.equals(Locale.ENGLISH) ? "No Data" : "Sin Datos";

		String acceptedCurrencies = this.validator.findAcceptedCurrencies();
		String[] currencies = acceptedCurrencies.split(",\\s*");

		dataset = super.unbind(object, "totalLogsBelow25Percent", "totalLogs25To50Percent", "totalLogs50To75Percent", "totalLogsAbove75Percent", "averageBudget", "deviationBudget", "minBudget", "maxBudget");
		dataset.put("nullValues", nullValues);

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("currency", currencies);
	}
}
