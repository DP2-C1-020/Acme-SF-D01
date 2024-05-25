
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banners.Banner;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBannerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id;
		Banner object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findBannerById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final Banner object) {
		assert object != null;
		super.bind(object, "startDate", "endDate", "picture", "slogan", "link");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		String dateString = "2100/01/01 00:00";
		Date maxDate = MomentHelper.parse(dateString, "yyyy/MM/dd HH:mm");

		if (object.getStartDate() != null) {

			if (!super.getBuffer().getErrors().hasErrors("startDate"))
				super.state(MomentHelper.isAfter(object.getStartDate(), object.getInstantiationMoment()), "startDate", "administrator.banner.form.error.startDate");

			if (!super.getBuffer().getErrors().hasErrors("startDate"))
				super.state(MomentHelper.isBefore(object.getStartDate(), maxDate), "startMoment", "administrator.banner.form.error.limit.date");

			if (object.getStartDate() != null) {

				if (!super.getBuffer().getErrors().hasErrors("endDate"))
					super.state(MomentHelper.isLongEnough(object.getStartDate(), object.getEndDate(), 1, ChronoUnit.WEEKS), "endDate", "administrator.banner.form.error.endDate-not-long-enough");
				if (!super.getBuffer().getErrors().hasErrors("endDate"))
					super.state(MomentHelper.isAfter(object.getEndDate(), object.getStartDate()), "endDate", "administrator.banner.form.error.endDate-not-long-enough");
			}
		}

		if (object.getStartDate() != null) {

			if (!super.getBuffer().getErrors().hasErrors("endDate"))
				super.state(MomentHelper.isAfter(object.getEndDate(), object.getInstantiationMoment()), "endDate", "administrator.banner.form.error.endDate-not-long-enough");

			if (!super.getBuffer().getErrors().hasErrors("endDate"))
				super.state(MomentHelper.isBefore(object.getEndDate(), maxDate), "endDate", "administrator.banner.form.error.limit.date");
		}

	}
	@Override
	public void perform(final Banner object) {
		assert object != null;
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "startDate", "endDate", "picture", "slogan", "link");
		super.getResponse().addData(dataset);
	}

}
