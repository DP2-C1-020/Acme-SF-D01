
package acme.features.auditor.code_audit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.CodeAuditType;
import acme.entities.audits.Mark;
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
		//		boolean status = false;
		//
		//		Principal principal = super.getRequest().getPrincipal();
		//
		//		if (principal.hasRole(Auditor.class))
		//			status = true;

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		//		Date moment;
		Auditor auditor;

		object = new CodeAudit();
		auditor = this.repository.findAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setAuditor(auditor);
		//		moment = MomentHelper.getCurrentMoment();
		//		object.setExecution(moment);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "execution", "type", "correctiveActions", "link", "project");
	}

	@Override
	public void validate(final CodeAudit object) {
		final Collection<String> allCodes = this.repository.findAllCodeAuditsCode();

		Date minimumDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (object.getExecution() != null && !super.getBuffer().getErrors().hasErrors("execution"))
			super.state(MomentHelper.isAfterOrEqual(object.getExecution(), minimumDate), "execution", "validation.auditrecord.moment.minimum-date");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allCodes.contains(object.getCode()), "code", "auditor.codeaudit.error.codeDuplicate");

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		//		Date moment;
		//
		//		moment = MomentHelper.getCurrentMoment();
		//		object.setExecution(moment);
		//		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;

		Collection<Project> allProjects;
		SelectChoices projects;
		allProjects = this.repository.findAllProjectsWithoutDraftMode();
		projects = SelectChoices.from(allProjects, "code", object.getProject());

		SelectChoices choicesType;
		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());

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
