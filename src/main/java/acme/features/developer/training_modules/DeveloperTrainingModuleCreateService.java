
package acme.features.developer.training_modules;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.training_module.DifficultyLevel;
import acme.entities.training_module.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		Boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrainingModule trainingModule;
		Developer developer;
		int developerId;
		Date moment;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		developer = this.repository.findOneDeveloperById(developerId);

		trainingModule = new TrainingModule();
		moment = MomentHelper.getCurrentMoment();
		trainingModule.setCreationMoment(moment);
		trainingModule.setDeveloper(developer);
		trainingModule.setDraftMode(true);
		super.getBuffer().addData(trainingModule);

	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;
		int id;

		Project project;

		id = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(id);
		object.setProject(project);
		super.bind(object, "code", "details", "difficultyLevel", "link", "totalTime");

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findTrainingModuleByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-module.form.error.duplicateCode");
		}
		if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment") && !super.getBuffer().getErrors().hasErrors("creationMoment"))
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
		SelectChoices projectsChoices;

		Collection<Project> projects;
		projects = this.repository.findAllProjectsWithoutDraftMode();

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime");
		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		projectsChoices = SelectChoices.from(projects, "code", object.getProject());
		dataset.put("project", projectsChoices.getSelected().getKey());
		dataset.put("projects", projectsChoices);
		dataset.put("difficultyLevels", choices);

		super.getResponse().addData(dataset);

	}

}
