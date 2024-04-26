
package acme.features.client.dashboard;

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

	// Contructors ------------------------------------------------------------


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
		ClientDashboard dashboard;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		client = this.repository.findOneClientByUserAccountId(userAccountId);

		int totalLogsBelow25Percent;
		int totalLogs25To50Percent;
		int totalLogs50To75Percent;
		int totalLogsAbove75Percent;
		Double averageBudget;
		Double deviationBudget;
		Double minBudget;
		Double maxBudget;

		totalLogsBelow25Percent = this.repository.findNumOfProgressLogsLess25(client);
		totalLogs25To50Percent = this.repository.findNumOfProgressLogsWithRate25to50(client);
		totalLogs50To75Percent = this.repository.findNumOfProgressLogsWithRate50to75(client);
		totalLogsAbove75Percent = this.repository.findNumOfProgressLogsWithRateOver75(client);
		averageBudget = this.repository.findAverageBudget(client);
		deviationBudget = this.repository.findDeviationBudget(client);
		minBudget = this.repository.findMinBudget(client);
		maxBudget = this.repository.findMaxBudget(client);

		dashboard = new ClientDashboard();

		dashboard.setTotalLogsBelow25Percent(totalLogsBelow25Percent);
		dashboard.setTotalLogs25To50Percent(totalLogs25To50Percent);
		dashboard.setTotalLogs50To75Percent(totalLogs50To75Percent);
		dashboard.setTotalLogsAbove75Percent(totalLogsAbove75Percent);
		dashboard.setAverageBudget(averageBudget);
		dashboard.setMinBudget(deviationBudget);
		dashboard.setMaxBudget(minBudget);
		dashboard.setDeviationBudget(maxBudget);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "totalLogsBelow25Percent", "totalLogs25To50Percent", "totalLogs50To75Percent", "totalLogsAbove75Percent", "averageBudget", "deviationBudget", "minBudget", "maxBudget");
		super.getResponse().addData(dataset);
	}

}
