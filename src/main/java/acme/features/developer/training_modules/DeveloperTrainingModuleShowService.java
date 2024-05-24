
package acme.features.developer.training_modules;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.training_module.DifficultyLevel;
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
		int developerId;
		TrainingModule trainingModule;
		Collection<TrainingModule> mytrainingModules;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		mytrainingModules = this.repository.findAllTrainingModulesByDeveloperId(developerId);

		status = trainingModule != null && super.getRequest().getPrincipal().hasRole(Developer.class) && mytrainingModules.contains(trainingModule);

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
		SelectChoices choices;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");
		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		dataset.put("difficultyLevels", choices);

		super.getResponse().addData(dataset);
	}
}
