
package acme.features.developer.training_sessions;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionCreateService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {

		Boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;
		Principal principal;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		principal = super.getRequest().getPrincipal();

		status = trainingModule != null && trainingModule.isDraftMode() && principal.hasRole(Developer.class) && principal.getActiveRoleId() == trainingModule.getDeveloper().getId();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		TrainingSession trainingSession;
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		trainingSession = new TrainingSession();
		trainingSession.setTrainingModule(trainingModule);
		trainingSession.setDraftMode(true);
		;

		super.getBuffer().addData(trainingSession);
	}

	@Override
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession existing;

			existing = this.repository.findTrainingSessionByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-session.form.error.duplicateCode");
		}

		if (!super.getBuffer().getErrors().hasErrors("startMoment")) {
			super.state(MomentHelper.isAfter(object.getStartMoment(), object.getTrainingModule().getCreationMoment()), "startMoment", "developer.training-session.form.error.startBeforeCreate");
			super.state(MomentHelper.isAfter(object.getStartMoment(), MomentHelper.deltaFromMoment(object.getTrainingModule().getCreationMoment(), 7, ChronoUnit.DAYS)), "startMoment", "developer.training-session.form.error.oneWeekFromTrainingModule");
		}

		if (!super.getBuffer().getErrors().hasErrors("startMoment") && !super.getBuffer().getErrors().hasErrors("finishMoment")) {
			super.state(MomentHelper.isAfter(object.getFinishMoment(), object.getStartMoment()), "finishMoment", "developer.training-session.form.error.finishBeforeStart");
			super.state(MomentHelper.isAfter(object.getFinishMoment(), MomentHelper.deltaFromMoment(object.getStartMoment(), 7, ChronoUnit.DAYS)), "finishMoment", "developer.training-session.form.error.periodTooShort");
		}
	}

	@Override
	public void perform(final TrainingSession object) {

		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("trainingModuleId", super.getRequest().getData("trainingModuleId", int.class));
	}

}
