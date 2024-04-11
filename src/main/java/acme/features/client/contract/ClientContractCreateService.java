
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.project.Project;
import acme.roles.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientContractRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Client.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		Date moment;
		Client client;

		object = new Contract();
		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setClient(client);
		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final Contract object) {
		assert object != null;
		int id;
		Project project;

		id = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(id);

		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
		object.setProject(project);
	}

	@Override
	public void validate(final Contract object) {
		final Collection<String> allCodes = this.repository.findAllContractsCode();

		Project project = object.getProject();

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "client.contract.error.codeDuplicate");

		if (object.getBudget() == null)
			super.state(false, "budget", "client.contract.error.budget");

		if (object.getBudget() != null && object.getBudget().getAmount() > project.getCost())
			super.state(false, "budget", "client.contract.error.projectBudget");

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;

		projects = this.repository.findAllProjectsWithoutDraftMode();
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}
}
