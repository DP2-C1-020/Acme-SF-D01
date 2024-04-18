
package acme.features.manager.managerDashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.userstory.Priority;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId;
		ManagerDashboard dashboard;

		Map<Priority, Integer> totalNumberUserStoriesByPriority;
		Double avgProjectCost;
		Integer minProjectCost;
		Integer maxProjectCost;
		Double devProjectCost;
		Double avgUserStoryCost;
		Integer minUserStoryCost;
		Integer maxUserStoryCost;
		Double devUserStoryCost;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		totalNumberUserStoriesByPriority = this.repository.totalNumberUserStoriesByPriority(managerId);
		avgUserStoryCost = this.repository.avgUserStoryCost(managerId);
		devUserStoryCost = this.repository.devUserStoryCost(managerId);
		minUserStoryCost = this.repository.minUserStoryCost(managerId);
		maxUserStoryCost = this.repository.maxUserStoryCost(managerId);
		avgProjectCost = this.repository.avgProjectCost(managerId);
		devProjectCost = this.repository.devProjectCost(managerId);
		minProjectCost = this.repository.minProjectCost(managerId);
		maxProjectCost = this.repository.maxProjectCost(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setTotalNumberUserStoriesByPriority(totalNumberUserStoriesByPriority);
		dashboard.setAvgUserStoryCost(avgUserStoryCost);
		dashboard.setDevUserStoryCost(devUserStoryCost);
		dashboard.setMinUserStoryCost(minUserStoryCost);
		dashboard.setMaxUserStoryCost(maxUserStoryCost);
		dashboard.setAvgProjectCost(avgProjectCost);
		dashboard.setDevProjectCost(devProjectCost);
		dashboard.setMinProjectCost(minProjectCost);
		dashboard.setMaxProjectCost(maxProjectCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		assert object != null;

		Dataset dataset;
		Integer totalNumberUserStoriesPriorityMust;
		Integer totalNumberUserStoriesPriorityShould;
		Integer totalNumberUserStoriesPriorityCould;
		Integer totalNumberUserStoriesPriorityWont;

		totalNumberUserStoriesPriorityMust = object.getTotalNumberUserStoriesByPriority().get(Priority.MUST);
		totalNumberUserStoriesPriorityShould = object.getTotalNumberUserStoriesByPriority().get(Priority.SHOULD);
		totalNumberUserStoriesPriorityCould = object.getTotalNumberUserStoriesByPriority().get(Priority.COULD);
		totalNumberUserStoriesPriorityWont = object.getTotalNumberUserStoriesByPriority().get(Priority.WONT);

		dataset = super.unbind(object, //
			"avgUserStoryCost", "devUserStoryCost", //
			"minUserStoryCost", "maxUserStoryCost", //
			"avgProjectCost", "devProjectCost", "minProjectCost", "maxProjectCost");

		dataset.put("totalNumberUserStoriesPriorityMust", totalNumberUserStoriesPriorityMust);
		dataset.put("totalNumberUserStoriesPriorityShould", totalNumberUserStoriesPriorityShould);
		dataset.put("totalNumberUserStoriesPriorityCould", totalNumberUserStoriesPriorityCould);
		dataset.put("totalNumberUserStoriesPriorityWont", totalNumberUserStoriesPriorityWont);

		super.getResponse().addData(dataset);
	}

}
