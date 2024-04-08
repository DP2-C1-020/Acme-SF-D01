
package acme.features.authenticated.objectives;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.objectives.Objective;
import acme.entities.objectives.PriorityType;
import acme.entities.project.Project;

@Service
public class AuthenticatedObjectiveShowService extends AbstractService<Authenticated, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedObjectiveRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Objective object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneObjectiveById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		SelectChoices choices;
		Collection<Project> projects;
		SelectChoices projectsChoices;
		Dataset dataset;

		projects = this.repository.findAllProjects();
		projectsChoices = SelectChoices.from(projects, "title", object.getProject());

		choices = SelectChoices.from(PriorityType.class, object.getPriority());

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", "status", "startDate", "endDate", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		dataset.put("statuses", choices);
		dataset.put("projects", projectsChoices);

		super.getResponse().addData(dataset);
	}

}
