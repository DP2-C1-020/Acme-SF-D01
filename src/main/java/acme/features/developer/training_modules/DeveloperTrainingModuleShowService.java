
package acme.features.developer.training_modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleShowService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		Boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		status = trainingModule != null && super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule trainingModule;
		int trainingModuleId;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		super.getBuffer().addData(trainingModule);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "updateMoment", "difficultyLevel", "link", "totalTime");

		super.getResponse().addData(dataset);
	}
}
