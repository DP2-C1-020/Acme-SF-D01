
package acme.features.developer.training_sessions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionRepository repository;


	@Override
	public void authorise() {
		// TODO Auto-generated method stub
		super.authorise();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		super.load();
	}

	@Override
	public void unbind(final TrainingSession object) {
		// TODO Auto-generated method stub
		super.unbind(object);
	}

}
