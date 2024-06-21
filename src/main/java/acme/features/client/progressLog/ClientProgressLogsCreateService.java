
package acme.features.client.progressLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogsCreateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientProgressLogsRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Contract contract;
		Principal principal;
		int id;

		id = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findContractById(id);
		principal = super.getRequest().getPrincipal();
		status = contract.getClient().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int contractId;
		ProgressLog progressLog;
		Date moment;
		Contract contract;

		contractId = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findContractById(contractId);

		progressLog = new ProgressLog();
		moment = MomentHelper.getCurrentMoment();
		progressLog.setRegistrationMoment(moment);
		progressLog.setContract(contract);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		int id;
		Contract contract;

		id = super.getRequest().getData("contractId", int.class);
		contract = this.repository.findContractById(id);

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
		object.setRegistrationMoment(MomentHelper.getCurrentMoment());
		object.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog object) {

		final Collection<String> allCodes = this.repository.findAllProgressLogCodes();

		if (!super.getBuffer().getErrors().hasErrors("recordId"))
			super.state(!allCodes.contains(object.getRecordId()), "recordId", "client.progressLog.error.recordIdDuplicate");

	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		Date moment;
		moment = MomentHelper.getCurrentMoment();

		object.setRegistrationMoment(moment);
		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "responsiblePerson", "draftMode");

		dataset.put("registrationMoment", MomentHelper.getCurrentMoment());
		dataset.put("contractCode", object.getContract().getCode());
		dataset.put("contractId", object.getContract().getId());

		super.getResponse().addData(dataset);
	}
}
