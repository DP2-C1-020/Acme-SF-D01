
package acme.features.any.claims;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claims.Claim;

@Service
public class AnyClaimPublishService extends AbstractService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Claim();
		object.setCode("");
		object.setInstantiationMoment(moment);
		object.setHeading("");
		object.setDescription("");
		object.setDepartment("");
		object.setEmail("");
		object.setLink("");
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link");
		object.setEmail(object.getEmail().isEmpty() ? null : object.getEmail());
		object.setLink(object.getLink().isEmpty() ? null : object.getLink());
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "any.claim.form.error.confirmation-needed");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Claim existing;

			existing = this.repository.findOneClaimByCode(object.getCode());
			super.state(existing == null, "code", "any.claim.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link", "draftMode");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
