
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

		status = object != null && object.isDraftMode() && object.getClient().getId() == principal.getActiveRoleId();

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
		final Contract contract = this.repository.findContractById(object.getId());
		final Collection<String> allCodes = this.repository.findAllContractsCode();
		boolean isCodeChanged = false;

		Project project = object.getProject();

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !contract.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "client.contract.error.codeDuplicate");
		}

		if (object.getBudget() == null)
			super.state(false, "budget", "client.contract.error.budget");

		if (object.getBudget() != null && object.getBudget().getAmount() > project.getCost())
			super.state(false, "budget", "client.contract.error.projectBudget");

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
