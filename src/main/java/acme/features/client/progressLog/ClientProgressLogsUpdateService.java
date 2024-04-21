
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
public class ClientProgressLogsUpdateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ProgressLog progressLog;
		Contract contract = null;
		Principal principal;
		int id;
		int contractId;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		contractId = progressLog.getContract().getId();

		if (progressLog != null)
			contract = this.repository.findContractById(contractId);

		principal = super.getRequest().getPrincipal();

		status = progressLog != null && contract.getClient().getId() == principal.getActiveRoleId() && progressLog.isDraftMode();
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
		boolean isCodeChanged = false;
		Collection<String> allCodes;
		ProgressLog progressLog;

		allCodes = this.repository.findAllProgressLogCodes();
		progressLog = this.repository.findProgressLogById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			isCodeChanged = !progressLog.getRecordId().equals(object.getRecordId());
			super.state(!isCodeChanged || !allCodes.contains(object.getRecordId()), "recordId", "client.progressLog.error.recordIdDuplicate");
		}
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.save(object);
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
