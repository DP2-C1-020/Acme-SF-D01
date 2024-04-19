
package acme.features.any.progressLogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Any;
import acme.entities.progress_logs.ProgressLog;

@Controller
public class AnyProgressLogsController extends AbstractController<Any, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyProgressLogsListService	listService;

	@Autowired
	protected AnyProgressLogsShowService	showService;

	// Contructors ------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
