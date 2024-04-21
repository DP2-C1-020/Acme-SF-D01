
package acme.features.client.progressLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogsListService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Contract object;
		Principal principal;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		object = this.repository.findContractById(contractId);
		principal = super.getRequest().getPrincipal();

		status = object.isDraftMode() == false && object.getClient().getId() == principal.getActiveRoleId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int contractId;

		contractId = super.getRequest().getData("contractId", int.class);
		objects = this.repository.findProgressLogByContractId(contractId);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("contractId", contractId);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "recordId", "comment", "completeness", "draftMode");
		super.getResponse().addData(dataset);

	}

}
