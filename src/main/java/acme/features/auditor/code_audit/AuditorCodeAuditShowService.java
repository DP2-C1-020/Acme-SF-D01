
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
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


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
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;

		SelectChoices choicesType;
		choicesType = SelectChoices.from(CodeAuditType.class, object.getType());
		Project objectProject = object.getProject();

		String modeMark;
		Collection<Mark> marks = this.repository.findMarksByCodeAuditId(object.getId());
		modeMark = MarkMode.findMode(marks);

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "link", "draftMode");
		dataset.put("modeMark", modeMark);
		dataset.put("project", objectProject.getTitle());
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
