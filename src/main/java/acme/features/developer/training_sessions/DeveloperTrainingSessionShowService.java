
package acme.features.developer.training_sessions;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

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

		status = trainingSession != null && super.getRequest().getPrincipal().hasRole(Developer.class) && myTrainingModules.contains(trainingModule);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int trainingSessionId;
		TrainingSession trainingSession;

		trainingSessionId = super.getRequest().getData("id", int.class);
		trainingSession = this.repository.findTrainingSessionById(trainingSessionId);

		super.getBuffer().addData(trainingSession);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
