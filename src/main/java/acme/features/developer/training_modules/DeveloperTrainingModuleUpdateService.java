
package acme.features.developer.training_modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.training_module.DifficultyLevel;
import acme.entities.training_module.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleUpdateService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		Boolean status;
		int trainingModuleId;
		Developer developer;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("id", int.class);

		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		developer = trainingModule != null ? trainingModule.getDeveloper() : null;
		status = trainingModule != null && trainingModule.getDraftMode() && super.getRequest().getPrincipal().hasRole(developer);

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
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(object.getDraftMode(), "draftMode", "developer.training-module.form.error.draftMode");

		if (!super.getBuffer().getErrors().hasErrors("updateMoment") && !super.getBuffer().getErrors().hasErrors("creationMoment") && object.getUpdateMoment() != null)
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.updateBeforeCreate");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);

	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");
		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		dataset.put("difficultyLevels", choices);

		super.getBuffer().addData(dataset);
	}

}
