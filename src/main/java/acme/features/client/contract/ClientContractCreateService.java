
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
		assert contract != null;
		boolean isValid = true;

		// Validate Project
		Project project = contract.getProject();
		if (project == null) {
			super.state(false, "project", "client.contract.error.project");
			isValid = false;
		}

		// Validate Budget
		if (isValid) {
			double totalBudget = 0.0;
			Collection<Contract> allContractsByProject = this.repository.findAllContractsWithProject(project.getId());

			for (Contract c : allContractsByProject)
				if (c.getBudget() != null && c.getBudget().getAmount() != null)
					totalBudget += c.getBudget().getAmount();

			if (contract.getBudget() != null && contract.getBudget().getAmount() != null)
				totalBudget += contract.getBudget().getAmount();

			if (contract.getBudget() == null || contract.getBudget().getAmount() == null) {
				super.state(false, "budget", "client.contract.error.budget");
				isValid = false;
			} else {
				double budgetAmount = contract.getBudget().getAmount();
				if (budgetAmount < 0.0) {
					super.state(false, "budget", "client.contract.error.negativeBudget");
					isValid = false;
				} else if (budgetAmount == 0.0) {
					super.state(false, "budget", "client.contract.error.zeroBudget");
					isValid = false;
				} else if (totalBudget > project.getCost().getAmount()) {
					super.state(false, "budget", "client.contract.error.projectBudgetTotal");
					isValid = false;
				}
			}
		}

		// Validate Code Uniqueness
		if (isValid) {
			Collection<String> allCodes = this.repository.findAllContractsCode();
			boolean isCodeUnique = !allCodes.contains(contract.getCode());
			super.state(isCodeUnique, "code", "client.contract.error.codeDuplicate");
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

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}
}
