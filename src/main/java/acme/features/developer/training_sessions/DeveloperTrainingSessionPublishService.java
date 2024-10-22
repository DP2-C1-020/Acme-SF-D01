
package acme.features.developer.training_sessions;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionPublishService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {
		Boolean status;
		int trainingSessionId;
		TrainingSession trainingSession;
		TrainingModule trainingModule;
		Collection<TrainingModule> myTrainingModules;
		int developerId;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		trainingModule = trainingSession.getTrainingModule();
		myTrainingModules = this.repository.findAllTrainingModulesByDeveloperId(developerId);

		status = trainingSession != null && trainingSession.isDraftMode() && super.getRequest().getPrincipal().hasRole(Developer.class) && myTrainingModules.contains(trainingModule);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession trainingSession;
		int trainingSessionId;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);

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
			final Collection<String> trainingSessionCodes = this.repository.findAllTrainingSessionsCodes();
			final TrainingSession trainingSession = this.repository.findTrainingSessionById(object.getId());

			boolean isCodeValid = !trainingSessionCodes.contains(object.getCode()) || trainingSession.getCode().equals(object.getCode());

			super.state(isCodeValid, "code", "developer.training-session.form.error.duplicateCode");
		}

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(object.isDraftMode(), "draftMode", "developer.training-session.form.error.draftMode");

		if (!super.getBuffer().getErrors().hasErrors("startMoment"))
			super.state(MomentHelper.isAfter(object.getStartMoment(), object.getTrainingModule().getCreationMoment()), "startMoment", "developer.training-session.form.error.startBeforeCreate");

		if (!super.getBuffer().getErrors().hasErrors("startMoment") && !super.getBuffer().getErrors().hasErrors("finishMoment")) {
			super.state(MomentHelper.isAfter(object.getFinishMoment(), object.getStartMoment()), "finishMoment", "developer.training-session.form.error.finishBeforeStart");
			super.state(MomentHelper.isAfter(object.getFinishMoment(), MomentHelper.deltaFromMoment(object.getStartMoment(), 7, ChronoUnit.DAYS)), "finishMoment", "developer.training-session.form.error.periodTooShort");
		}

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
