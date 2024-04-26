
package acme.features.developer.training_sessions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionDeleteService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {
		Boolean status;
		int trainingSessionId;
		TrainingSession trainingSession;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);
		status = trainingSession != null && trainingSession.getDraftMode() && super.getRequest().getPrincipal().hasRole(Developer.class);
		super.getResponse().setAuthorised(status);
		System.out.println(status);
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

		super.bind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");
	}
	@Override
	public void validate(final TrainingSession object) {
		assert object != null;
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		this.repository.delete(object);

	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
