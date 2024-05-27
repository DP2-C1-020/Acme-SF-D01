
package acme.features.developer.training_sessions;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionListService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {
		boolean status;
		TrainingModule trainingModule;
		int trainingModuleId;
		int developerId;
		Principal principal;

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);

		principal = super.getRequest().getPrincipal();
		developerId = trainingModule.getDeveloper().getId();

		status = principal.hasRole(Developer.class) && principal.getActiveRoleId() == developerId;

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
		dataset = super.unbind(object, "code", "startMoment", "finishMoment", "location", "instructor", "contactEmail", "link", "draftMode");
		super.getResponse().addData(dataset);

		super.getResponse().addGlobal("trainingModuleId", super.getRequest().getData("trainingModuleId", int.class));

	}

	@Override
	public void unbind(final Collection<TrainingSession> objects) {
		assert objects != null;
		int trainigModuleId;
		TrainingModule trainingModule;
		final boolean showCreate;

		trainigModuleId = super.getRequest().getData("trainingModuleId", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainigModuleId);
		showCreate = trainingModule.getDraftMode() && super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().addGlobal("trainingModuleId", trainigModuleId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
