
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
import acme.entities.audits.Mark;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditPublishService extends AbstractService<Auditor, CodeAudit> {

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

		status = object != null && principal.hasRole(auditor) && object.isDraftMode();

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
		final Collection<String> allCodes = this.repository.findAllCodeAuditsCode();
		boolean isCodeChanged = false;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !object.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "auditor.codeaudit.error.codeDuplicate");
		}

		String modeMark;
		Collection<Mark> marks = this.repository.findMarksByCodeAuditId(object.getId());
		modeMark = MarkMode.findMode(marks);

		if (!super.getBuffer().getErrors().hasErrors("modeMark"))
			if (modeMark != null) {
				boolean isAtLeastC = modeMark.equals("C") || modeMark.equals("B") || modeMark.equals("A") || modeMark.equals("A_PLUS");
				super.state(isAtLeastC, "modeMark", "validation.codeaudit.mode-less-than-c");
			} else
				super.state(false, "modeMark", "validation.codeaudit.mode-less-than-c");

		Date minimumDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (object.getExecution() != null && !super.getBuffer().getErrors().hasErrors("execution"))
			super.state(MomentHelper.isAfterOrEqual(object.getExecution(), minimumDate), "execution", "validation.auditrecord.moment.minimum-date");

		//		Collection<AuditRecord> auditRecords = this.repository.findAllAuditRecordsByCodeAuditId(object.getId());
		//		boolean hasDraftMode = auditRecords.stream().anyMatch(AuditRecord::isDraftMode);
		//		if (!super.getBuffer().getErrors().hasErrors("*"))
		//			super.state(auditRecords.stream().anyMatch(AuditRecord::isDraftMode), "*", "validation.codeaudit.publish.unpublished-audit-records");
		//		super.state(!hasDraftMode, "*", "validation.codeaudit.publish.unpublished-audit-records");

		if (!super.getBuffer().getErrors().hasErrors("*")) {
			Integer notPublishedAuditRecord = this.repository.countNotPublishedAuditRecordsOfCodeAudit(object.getId());
			super.state(notPublishedAuditRecord == 0, "*", "validation.codeaudit.form.notAllAuditRecordArePublished");
		}
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(false);
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
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("types", choicesType);
		dataset.put("modeMark", modeMark);

		super.getResponse().addData(dataset);
	}

}
