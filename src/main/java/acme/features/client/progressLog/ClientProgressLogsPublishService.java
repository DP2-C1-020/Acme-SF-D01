
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
public class ClientProgressLogsPublishService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		Principal principal;
		ProgressLog progressLog;
		Contract contract;
		boolean status;
		int id;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		principal = super.getRequest().getPrincipal();

		if (progressLog != null && (contract = progressLog.getContract()) != null)
			status = contract.getClient().getId() == principal.getActiveRoleId() && progressLog.isDraftMode();
		else
			status = false;

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
		int progressLogId;
		progressLogId = super.getRequest().getData("id", int.class);
		Contract contract = this.repository.findContractByProgressLogId(progressLogId);

		super.bind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
		object.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		Collection<String> allCodes = this.repository.findAllProgressLogCodes();
		ProgressLog existingProgressLog = this.repository.findProgressLogById(object.getId());

		if (existingProgressLog != null && !super.getBuffer().getErrors().hasErrors("recordId")) {
			boolean isCodeChanged = !existingProgressLog.getRecordId().equals(object.getRecordId());
			boolean isDuplicate = allCodes.contains(object.getRecordId());

			super.state(!isCodeChanged || !isDuplicate, "recordId", "client.progressLog.error.recordIdDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("completeness")) {
			Contract contract = object.getContract();
			ProgressLog lastProgressLog = this.repository.findLastProgressLogByContractId(contract.getId());

			if (lastProgressLog != null) {
				boolean isCompletenessIncremental = object.getCompleteness() > lastProgressLog.getCompleteness();
				super.state(isCompletenessIncremental, "completeness", "client.progressLog.error.completenessNotIncremental");
			}
		}
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftMode");
		dataset.put("registrationMoment", object.getRegistrationMoment());
		dataset.put("contractCode", object.getContract().getCode());

		super.getResponse().addData(dataset);
	}
}
