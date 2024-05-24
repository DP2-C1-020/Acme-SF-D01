
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
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);
		principal = super.getRequest().getPrincipal();

		status = object != null && object.getAuditor().getId() == principal.getActiveRoleId();

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

		super.bind(object, "code", "execution", "type", "correctiveActions", "link");
	}

	@Override
	public void validate(final CodeAudit object) {
		final CodeAudit contract = this.repository.findOneCodeAuditById(object.getId());
		final Collection<String> allCodes = this.repository.findAllCodeAuditsCode();
		boolean isCodeChanged = false;

		String modeMark;
		Collection<Mark> marks = this.repository.findMarksByCodeAuditId(object.getId());
		modeMark = MarkMode.findMode(marks);

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !contract.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "auditor.codeaudit.error.codeDuplicate");
		}

		if (!super.getBuffer().getErrors().hasErrors("modeMark"))
			if (modeMark != null) {
				boolean isAtLeastC = modeMark.equals("C") || modeMark.equals("B") || modeMark.equals("A") || modeMark.equals("A_PLUS");
				super.state(isAtLeastC, "modeMark", "validation.codeaudit.mode-less-than-c");
			} else
				super.state(false, "modeMark", "validation.codeaudit.mode-less-than-c");

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
		SelectChoices choicesType;

		Project objectProject = object.getProject();

		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());

		String modeMark;
		Collection<Mark> marks = this.repository.findMarksByCodeAuditId(object.getId());
		modeMark = MarkMode.findMode(marks);

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "link", "draftMode");
		dataset.put("project", objectProject.getTitle());
		dataset.put("types", choicesType);
		dataset.put("modeMark", modeMark);

		super.getResponse().addData(dataset);
	}

}
