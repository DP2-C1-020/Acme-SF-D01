
package acme.features.auditor.code_audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.features.auditor.audit_record.AuditorAuditRecordRepository;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository	repository;

	@Autowired
	protected AuditorAuditRecordRepository	auditRecordRepository;

	// AbstractService interface ----------------------------------------------


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
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("*"))
			super.state(object.isDraftMode(), "*", "validation.codeaudit.error.publish");

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		Collection<AuditRecord> auditRecords = this.auditRecordRepository.findAllAuditRecordsByCodeAuditId(object.getId());
		this.repository.deleteAll(auditRecords);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;
	}

}
