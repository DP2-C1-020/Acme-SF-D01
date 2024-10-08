
package acme.features.auditor.audit_record;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audits.AuditRecord;
import acme.entities.audits.CodeAudit;
import acme.entities.audits.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCreateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		CodeAudit codeAudit;
		Principal principal;
		int id;

		id = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(id);
		principal = super.getRequest().getPrincipal();
		status = codeAudit.getAuditor().getId() == principal.getActiveRoleId() && codeAudit.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int codeAuditId;
		AuditRecord auditRecord;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);

		auditRecord = new AuditRecord();
		auditRecord.setCodeAudit(codeAudit);

		super.getBuffer().addData(auditRecord);

	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		int id;
		CodeAudit codeAudit;

		id = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(id);

		super.bind(object, "code", "initialMoment", "finalMoment", "mark", "link");
		object.setCodeAudit(codeAudit);

	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		Date pastMostDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord isCodeUnique;
			isCodeUnique = this.repository.findAuditRecordByCode(object.getCode());
			super.state(isCodeUnique == null, "code", "validation.auditrecord.code.duplicate");
		}

		// Validar si el initialMoment es nulo o inválido
		if (object.getInitialMoment() == null)
			super.state(false, "initialMoment", "validation.auditrecord.initialMoment.invalid-format");
		else if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
			// Validar que la fecha sea posterior o igual a la fecha mínima
			super.state(MomentHelper.isAfterOrEqual(object.getInitialMoment(), pastMostDate), "initialMoment", "validation.auditrecord.moment.minimum-date");

		// Validar si el finalMoment es nulo o inválido
		if (object.getFinalMoment() == null)
			super.state(false, "finalMoment", "validation.auditrecord.finalMoment.invalid-format");
		else if (!super.getBuffer().getErrors().hasErrors("finalMoment"))
			// Validar que la fecha sea posterior o igual a la fecha mínima
			super.state(MomentHelper.isAfterOrEqual(object.getFinalMoment(), pastMostDate), "finalMoment", "validation.auditrecord.moment.minimum-date");

		if (object.getInitialMoment() != null && object.getFinalMoment() != null) {
			if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
				super.state(MomentHelper.isAfter(object.getFinalMoment(), object.getInitialMoment()), "initialMoment", "validation.auditrecord.moment.initial-after-final");

			if (!super.getBuffer().getErrors().hasErrors("finalMoment")) {
				Date minimumEnd;
				minimumEnd = MomentHelper.deltaFromMoment(object.getInitialMoment(), 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(object.getFinalMoment(), minimumEnd), "finalMoment", "validation.auditrecord.moment.minimum-one-hour");
			}
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(Mark.class, object.getMark());

		CodeAudit codeAudit = object.getCodeAudit();

		dataset = super.unbind(object, "code", "initialMoment", "finalMoment", "mark", "link", "draftMode");
		dataset.put("codeAuditCode", codeAudit.getCode());
		dataset.put("marks", choices);
		dataset.put("codeAuditId", codeAudit.getId());

		super.getResponse().addData(dataset);
	}
}
