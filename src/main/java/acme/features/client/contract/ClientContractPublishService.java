
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.project.Project;
import acme.roles.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientContractRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		Principal principal;
		int contractId;

		contractId = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(contractId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.getClient().getId() == principal.getActiveRoleId() && object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Contract object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget");
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

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		Project objectProject = object.getProject();

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("projectCode", objectProject.getCode());

		super.getResponse().addData(dataset);
	}
}
