
package acme.features.auditor.code_audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.CodeAuditType;
import acme.entities.audits.Mark;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		CodeAudit object;
		Principal principal;
		Auditor auditor;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);
		principal = super.getRequest().getPrincipal();

		auditor = object.getAuditor();
		status = object != null && object.isDraftMode() && principal.hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		int id;
		CodeAudit object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "execution", "type", "correctiveActions", "link", "project");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit exists;
			exists = this.repository.findOneCodeAuditByCode(object.getCode());
			final CodeAudit cA2 = object.getCode().equals("") || object.getCode().equals(null) ? null : this.repository.findOneCodeAuditById(object.getId());
			super.state(exists == null || cA2.equals(exists), "code", "auditor.codeaudit.error.codeDuplicate");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;

		SelectChoices choicesType;
		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());

		SelectChoices projects;
		Collection<Project> allProjects = this.repository.findAllProjectsWithoutDraftMode();
		projects = SelectChoices.from(allProjects, "code", object.getProject());

		String modeMark;
		Collection<Mark> marks = this.repository.findMarksByCodeAuditId(object.getId());
		modeMark = MarkMode.findMode(marks);

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "link", "draftMode");
		dataset.put("modeMark", modeMark);
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
