
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
import acme.validators.ValidatorMoney;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected ValidatorMoney			validator;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		Principal principal = super.getRequest().getPrincipal();
		boolean status = principal.hasRole(Client.class);
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
	public void validate(final Contract contract) {
		final Collection<String> allContractCodes = this.repository.findAllContractsCode();

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allContractCodes.contains(contract.getCode()), "code", "client.contract.form.error.duplicated");

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			double amount = contract.getBudget().getAmount();
			if (amount < 0.0)
				super.state(false, "budget", "client.contract.error.negativeBudget");
			if (amount > 1000000.0)
				super.state(false, "budget", "client.contract.error.excededBudget");
			super.state(this.validator.moneyValidator(contract.getBudget().getCurrency()), "budget", "client.contract.error.moneyValidator");
		}

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

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "draftMode");

		dataset.put("instantiationMoment", MomentHelper.getCurrentMoment());
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}
}
