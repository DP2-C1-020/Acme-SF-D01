
package acme.features.developer.training_modules;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleDeleteService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {

		Boolean status;
		int trainingModuleId;
		Developer developer;
		TrainingModule trainingModule;
		int developerId;
		Collection<TrainingModule> mytrainingModules;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		mytrainingModules = this.repository.findAllTrainingModulesByDeveloperId(developerId);

		trainingModuleId = super.getRequest().getData("id", int.class);

		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		developer = trainingModule != null ? trainingModule.getDeveloper() : null;
		status = trainingModule != null && trainingModule.getDraftMode() && super.getRequest().getPrincipal().hasRole(developer) && mytrainingModules.contains(trainingModule);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		super.getBuffer().addData(trainingModule);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		int trainingModuleId;
		Collection<TrainingSession> trainingSessions;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingSessions = this.repository.findTrainingSessionsByTrainingModuleId(trainingModuleId);

		this.repository.deleteAll(trainingSessions);
		this.repository.delete(object);

	}

}
