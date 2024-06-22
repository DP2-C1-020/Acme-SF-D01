
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipListService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ----------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface -----------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Sponsorship> object;
		int sponsorId;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		object = this.repository.findManySponsorshipsBySponsorId(sponsorId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		String payload;

		dataset = super.unbind(object, "code", "amount");
		dataset.put("project", object.getProject().getCode());
		dataset.put("duration", object.getDuration());
		payload = String.format(//
			"%s; %s", //
			object.getType(), //
			object.getContact());
		dataset.put("payload", payload);

		super.getResponse().addData(dataset);
	}
}
