
package acme.features.developer.training_modules;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.training_module.DifficultyLevel;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		boolean status;
		TrainingModule trainingModule;
		Principal principal;
		int trainingModuleId;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		principal = super.getRequest().getPrincipal();

		status = trainingModule != null && trainingModule.getDraftMode() && trainingModule.getDeveloper().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrainingModule trainingModule;

		id = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(id);

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
		int trainingModuleId;
		Collection<TrainingSession> trainingSessions;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingSessions = this.repository.findTrainingSessionsByTrainingModuleId(trainingModuleId);
		if (trainingSessions.isEmpty())
			super.state(false, "sessions", "developer.training-module.form.error.training-session");

	}

	@Override
	public void perform(final TrainingModule object) {

		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);

	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");

		dataset.put("difficultyLevels", choices);

		super.getResponse().addData(dataset);
	}

}
