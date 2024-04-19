
package acme.features.any.contract;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;

@Service
public class AnyContractShowService extends AbstractService<Any, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyContractRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		Contract contract;
		int id;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(id);

		super.getResponse().setAuthorised(!contract.isDraftMode());
	}

	@Override
	public void load() {
		Contract contract;
		int id;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(id);

		super.getResponse().addData(contract);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		List<ProgressLog> progressLogs;

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget");
		progressLogs = this.repository.findProgressLogsByContract(object.getId());
		dataset.put("hasProgressLogs", !progressLogs.isEmpty());

		super.getResponse().addData(dataset);
	}
}
