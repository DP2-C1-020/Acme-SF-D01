
package acme.features.developer.training_sessions;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.training_session.TrainingSession;
import acme.roles.Developer;

@Controller
public class DeveloperTrainingSessionController extends AbstractController<Developer, TrainingSession> {

	@Autowired
	private DeveloperTrainingSessionListService		listService;

	@Autowired
	private DeveloperTrainingSessionShowService		showService;

	@Autowired
	private DeveloperTrainingSessionCreateService	createService;

	@Autowired
	private DeveloperTrainingSessionDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
