
package acme.features.client.dashboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected ClientDashboardRepository repository;

	// AbstractService Interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();
		Client client = this.repository.findClientById(id);
		status = client != null && principal.hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		int clientId = principal.getActiveRoleId();

		Collection<Double> budgets = this.repository.findBudgetAmountsByClientId(clientId);

		Collection<Double> progressLogs = this.repository.findProgressLogsCompletenessByClientId(clientId);
		Map<String, Integer> progressMap = new HashMap<>();

		progressMap.put("25", this.countProgressLogs(progressLogs, 0, 25));
		progressMap.put("50", this.countProgressLogs(progressLogs, 25, 50));
		progressMap.put("75", this.countProgressLogs(progressLogs, 50, 75));
		progressMap.put("100", this.countProgressLogs(progressLogs, 75, 100));

		Double averageBudget = this.calculateAverage(budgets);
		Double deviationBudget = this.calculateStandardDeviation(budgets);
		Double minBudget = this.calculateMinBudget(budgets);
		Double maxBudget = this.calculateMaxBudget(budgets);

		final ClientDashboard dashboard = new ClientDashboard();
		dashboard.setProgressLogByCompletenessRate(progressMap);
		dashboard.setAverageBudget(averageBudget);
		dashboard.setDeviationBudget(deviationBudget);
		dashboard.setMinBudget(minBudget);
		dashboard.setMaxBudget(maxBudget);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		String emptyMessage;
		Locale local = super.getRequest().getLocale();

		emptyMessage = local.equals(Locale.ENGLISH) ? "No Data" : "Sin Datos";

		Dataset dataset = super.unbind(object, "progressLogByCompletenessRate", "averageBudget", "deviationBudget", "minBudget", "maxBudget");

		dataset.put("emptyMessage", emptyMessage);

		super.getResponse().addData(dataset);
	}

	private Integer countProgressLogs(final Collection<Double> progressLogsCompleteness, final double lowerBound, final double upperBound) {
		long count = progressLogsCompleteness.stream().filter(log -> log >= lowerBound / 100 && log < upperBound / 100).count();
		return (int) count;
	}

	private Double calculateAverage(final Collection<Double> budgets) {
		return budgets.isEmpty() ? null : budgets.stream().collect(Collectors.averagingDouble(budget -> budget));
	}

	private Double calculateStandardDeviation(final Collection<Double> budgets) {
		if (budgets.isEmpty())
			return null;
		double mean = this.calculateAverage(budgets);
		double temp = 0;
		for (Double budget : budgets)
			temp += (budget - mean) * (budget - mean);
		return Math.sqrt(temp / budgets.size());
	}

	private Double calculateMinBudget(final Collection<Double> budgets) {
		return budgets.stream().min(Double::compare).orElse(null);
	}

	private Double calculateMaxBudget(final Collection<Double> budgets) {
		return budgets.stream().max(Double::compare).orElse(null);
	}
}
