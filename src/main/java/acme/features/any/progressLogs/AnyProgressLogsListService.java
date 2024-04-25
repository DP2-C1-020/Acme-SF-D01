
package acme.features.any.progressLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;

@Service
public class AnyProgressLogsListService extends AbstractService<Any, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyProgressLogsRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		Contract object;
		int id;
		boolean status;

		id = super.getRequest().getData("contractId", int.class);
		object = this.repository.findContractById(id);

		status = object.isDraftMode() == false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ProgressLog> objects;
		int id;

		id = super.getRequest().getData("contractId", int.class);
		objects = this.repository.findProgressLogsByContractId(id);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("contractId", objects);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment");

		super.getResponse().addData(dataset);
	}

}
