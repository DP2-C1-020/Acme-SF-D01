
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectShowService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(masterId);
		manager = project == null ? null : project.getManager();
		status = super.getRequest().getPrincipal().hasRole(manager) || project != null && !project.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;
		/*
		 * int employerId;
		 * Collection<Company> contractors;
		 * SelectChoices choices;
		 * Dataset dataset;
		 * 
		 * if (!object.isDraftMode())
		 * contractors = this.repository.findAllContractors();
		 * else {
		 * employerId = super.getRequest().getPrincipal().getActiveRoleId();
		 * contractors = this.repository.findManyContractorsByEmployerId(employerId);
		 * }
		 * choices = SelectChoices.from(contractors, "name", object.getContractor());
		 * 
		 * dataset = super.unbind(object, "reference", "title", "deadline", "salary", "score", "moreInfo", "description", "draftMode");
		 * dataset.put("contractor", choices.getSelected().getKey());
		 * dataset.put("contractors", choices);
		 * 
		 * super.getResponse().addData(dataset);
		 */
	}

}
