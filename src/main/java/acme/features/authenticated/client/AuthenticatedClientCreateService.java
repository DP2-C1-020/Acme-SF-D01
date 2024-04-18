
package acme.features.authenticated.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.roles.Client;
import acme.roles.ClientType;

@Service
public class AuthenticatedClientCreateService extends AbstractService<Authenticated, Client> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedClientRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Client client;
		UserAccount userAccount;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		client = new Client();
		client.setUserAccount(userAccount);

		super.getBuffer().addData(client);
	}

	@Override
	public void bind(final Client object) {
		assert object != null;

		super.bind(object, "identification", "companyName", "clientType", "email", "link");
	}

	@Override
	public void validate(final Client object) {
		assert object != null;
	}

	@Override
	public void perform(final Client object) {
		this.repository.save(object);
	}

	@Override
	public void unbind(final Client object) {
		assert object != null;
		Dataset dataset;
		SelectChoices choices;

		dataset = super.unbind(object, "identification", "companyName", "clientType", "email", "link");
		choices = SelectChoices.from(ClientType.class, object.getClientType());

		dataset.put("clientType", choices.getSelected().getKey());
		dataset.put("clientTypes", choices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {

		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
