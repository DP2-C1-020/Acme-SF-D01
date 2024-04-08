
package acme.features.administrator.banner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.banners.Banner;

@Controller
public class AdministratorBannerController extends AbstractController<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBannerCreateService	createService;

	@Autowired
	protected AdministratorBannerDeleteService	deleteService;

	@Autowired
	protected AdministratorBannerListService	listService;

	@Autowired
	protected AdministratorBannerUpdateService	updateService;

	@Autowired
	protected AdministratorBannerShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("show", this.showService);
	}

}
