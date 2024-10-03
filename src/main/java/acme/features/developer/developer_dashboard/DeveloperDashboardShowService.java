
package acme.features.developer.developer_dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	@Autowired
	private DeveloperDashboardRepository repository;


	@Override
	public void authorise() {
		Boolean status;
		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		DeveloperDashboard developerDashboard;
		Integer developerId;
		Integer totalTrainingModulesWithUpdateMoment;
		Integer totalTrainingSessionWithLink;
		Double trainingModulesAverageTime;
		Double trainingModulesDeviationTime;
		Integer trainingModulesMinimumTime;
		Integer trainingModulesMaximumTime;
		Integer totalTrainingModules;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();

		totalTrainingModules = this.repository.getTotalTrainigModulesByDeveloperId(developerId);
		totalTrainingModulesWithUpdateMoment = this.repository.getTotalTrainingModulesWithUpdateMomentByDeveloperId(developerId);
		totalTrainingSessionWithLink = this.repository.getTotalTrainingSessionWithLinkByDeveloperId(developerId);
		trainingModulesAverageTime = this.repository.getTrainingModulesAverageTimeByDeveloperId(developerId).orElse(null);
		trainingModulesDeviationTime = this.repository.getTrainingModulesDeviationTimeByDeveloperId(developerId).orElse(null);
		trainingModulesMinimumTime = this.repository.getTrainingModulesMinimumTimeByDeveloperId(developerId).orElse(null);
		trainingModulesMaximumTime = this.repository.getTrainingModulesMaximumTimeByDeveloperId(developerId).orElse(null);

		developerDashboard = new DeveloperDashboard();

		if (totalTrainingModulesWithUpdateMoment != null)
			developerDashboard.setTotalTrainingModulesWithUpdateMoment(totalTrainingModulesWithUpdateMoment);

		if (totalTrainingModules > 1)
			developerDashboard.setTrainingModulesDeviationTime(trainingModulesDeviationTime);
		else
			developerDashboard.setTrainingModulesDeviationTime(null);

		developerDashboard.setTotalTrainingSessionWithLink(totalTrainingSessionWithLink);

		developerDashboard.setTrainingModulesAverageTime(trainingModulesAverageTime);

		developerDashboard.setTrainingModulesMinimumTime(trainingModulesMinimumTime);

		developerDashboard.setTrainingModulesMaximumTime(trainingModulesMaximumTime);

		super.getBuffer().addData(developerDashboard);

	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "totalTrainingModulesWithUpdateMoment", "totalTrainingSessionWithLink", "trainingModulesAverageTime", "trainingModulesDeviationTime", "trainingModulesMinimumTime", "trainingModulesMaximumTime");

		super.getResponse().addData(dataset);
	}

}
