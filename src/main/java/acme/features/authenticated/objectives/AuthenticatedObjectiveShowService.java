
package acme.features.authenticated.objectives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.objectives.Objective;
import acme.entities.objectives.PriorityType;

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
		Dataset dataset;

		choices = SelectChoices.from(PriorityType.class, object.getPriority());

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", "status", "startDate", "endDate", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);
	}

}
