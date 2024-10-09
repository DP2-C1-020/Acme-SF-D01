
package acme.features.developer.training_modules;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.training_module.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleListService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrainingModule> trainingModules;
		int developerId;

		developerId = super.getRequest().getPrincipal().getActiveRoleId();
		trainingModules = this.repository.findAllTrainingModulesByDeveloperId(developerId);

		super.getBuffer().addData(trainingModules);
		super.getResponse().addGlobal("developerId", developerId);

	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "details", "difficultyLevel", "link");

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}
