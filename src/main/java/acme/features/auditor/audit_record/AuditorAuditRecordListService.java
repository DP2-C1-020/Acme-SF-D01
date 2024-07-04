
package acme.features.auditor.audit_record;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		int codeAuditId;
		Auditor auditor;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);

		auditor = codeAudit == null ? null : codeAudit.getAuditor();
		status = codeAudit != null && super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditRecord> objects;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		objects = this.repository.findAllAuditRecordsByCodeAuditId(codeAuditId);
		CodeAudit codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		boolean codeAuditDraftMode = codeAudit.isDraftMode();

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("codeAuditId", codeAuditId);
		super.getResponse().addGlobal("codeAuditDraftMode", codeAuditDraftMode);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "mark");
		dataset.put("draftMode", object.isDraftMode() ? "✔" : "❌");
		super.getResponse().addData(dataset);
	}

}
