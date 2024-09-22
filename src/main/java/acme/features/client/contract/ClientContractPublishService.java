
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
import acme.validators.ValidatorMoney;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientContractRepository	repository;

	@Autowired
	protected ValidatorMoney			validator;

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
	public void validate(final Contract object) {
		assert object != null;
		final Collection<String> allContractCodes = this.repository.findAllContractsCode();
		final Contract contract = this.repository.findContractById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean isCodeValid = !allContractCodes.contains(object.getCode()) || contract.getCode().equals(object.getCode());
			super.state(isCodeValid, "code", "client.contract.error.codeDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget")) {
			super.state(object.getBudget().getAmount() >= 0, "budget", "client.contract.error.negativeBudget");
			super.state(object.getBudget().getAmount() <= 1000000, "budget", "client.contract.error.excededBudget");
			super.state(this.checkBudgetLessThanProjectCost(object), "budget", "client.contract.error.excededProjectBudget", object.getProject().getCost());
			super.state(this.validator.moneyValidator(object.getBudget().getCurrency()), "budget", "client.contract.error.moneyValidator");
		}
	}

	private boolean checkBudgetLessThanProjectCost(final Contract object) {
		assert object != null;

		if (object.getProject() != null) {
			Collection<Contract> allContracts = this.repository.findAllContractsWithProject(object.getProject().getId());

			double budgetTotal = 0.0;
			for (Contract contract : allContracts)
				if (!contract.isDraftMode())
					budgetTotal += contract.getBudget().getAmount();

			int projectCost = object.getProject().getCost() * 20;
			return projectCost >= budgetTotal + object.getBudget().getAmount();
		}
		return true;
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
