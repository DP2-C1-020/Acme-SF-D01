
package acme.features.client.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogsDeleteService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ProgressLog progressLog;
		Contract contract;
		Principal principal;
		int id;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		contract = progressLog.getContract();
		principal = super.getRequest().getPrincipal();
		status = contract.getClient().getId() == principal.getActiveRoleId() && progressLog.isDraftMode() && progressLog != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int contractId;
		ProgressLog progressLog;

		contractId = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(contractId);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;
		Contract objectContract = object.getContract();

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftMode");
		dataset.put("contractCode", objectContract.getCode());

		super.getResponse().addData(dataset);
	}
}
