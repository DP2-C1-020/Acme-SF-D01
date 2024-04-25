
package acme.features.auditor.code_audit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.CodeAuditType;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Auditor.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		Date moment;
		Auditor auditor;

		object = new CodeAudit();
		auditor = this.repository.findAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setAuditor(auditor);
		moment = MomentHelper.getCurrentMoment();
		object.setExecution(moment);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;
		int id;
		Project project;

		id = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(id);

		super.bind(object, "code", "execution", "type", "correctiveActions", "link");
		object.setProject(project);
	}

	@Override
	public void validate(final CodeAudit object) {
		final Collection<String> allCodes = this.repository.findAllCodeAuditsCode();

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "auditor.codeaudit.error.codeDuplicate");

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setExecution(moment);
		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;

		SelectChoices choicesType;

		projects = this.repository.findAllProjectsWithoutDraftMode();
		choices = SelectChoices.from(projects, "title", object.getProject());

		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "link", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
