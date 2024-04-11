/*
 * ManagerProjectPublishService.java
 */

package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = project == null ? null : project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstracto", "fatalErrors", "cost", "link", "draftMode");
	}

	@Override
	public void validate(final Project object) {
		/*
		 * assert object != null;
		 * 
		 * if (!super.getBuffer().getErrors().hasErrors("deadline")) {
		 * Date minimumDeadline;
		 * 
		 * minimumDeadline = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
		 * super.state(MomentHelper.isAfter(object.getDeadline(), minimumDeadline), "deadline", "employer.job.form.error.too-close");
		 * }
		 * 
		 * if (!super.getBuffer().getErrors().hasErrors("reference")) {
		 * Job existing;
		 * 
		 * existing = this.repository.findOneJobByReference(object.getReference());
		 * super.state(existing == null || existing.equals(object), "reference", "employer.job.form.error.duplicated");
		 * }
		 * 
		 * if (!super.getBuffer().getErrors().hasErrors("salary"))
		 * super.state(object.getSalary().getAmount() > 0, "salary", "employer.job.form.error.negative-salary");
		 * 
		 * {
		 * Double workLoad;
		 * 
		 * workLoad = this.repository.computeWorkLoadByJobId(object.getId());
		 * super.state(workLoad != null && workLoad == 100.0, "*", "employer.job.form.error.bad-work-load");
		 * }
		 */
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		int managerId;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		dataset = super.unbind(object, "code", "title", "abstracto", "fatalErrors", "cost", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
