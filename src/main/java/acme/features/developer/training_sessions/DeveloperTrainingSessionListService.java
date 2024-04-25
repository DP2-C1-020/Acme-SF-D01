
package acme.features.developer.training_sessions;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionListService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int trainigModuleId;
		Collection<TrainingSession> trainingSessions;

		trainigModuleId = super.getRequest().getData("trainingModuleId", int.class);

		trainingSessions = this.repository.findAllTrainingSessionsByTrainingModuleId(trainigModuleId);

		super.getBuffer().addData(trainingSessions);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "instructor", "location");
		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("trainingModuleId", object.getTrainingModule().getId());
	}

}
