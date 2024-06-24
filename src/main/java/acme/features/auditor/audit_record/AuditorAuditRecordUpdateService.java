
package acme.features.auditor.audit_record;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
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
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRecordRepository repository;

	// Contructors ------------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		AuditRecord auditRecord;
		CodeAudit codeAudit = null;
		Principal principal;
		int id;
		int codeAuditId;

		id = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findOneAuditRecordById(id);
		codeAuditId = auditRecord.getCodeAudit().getId();

		if (auditRecord != null)
			codeAudit = this.repository.findOneCodeAuditById(codeAuditId);

		principal = super.getRequest().getPrincipal();
		status = codeAudit.getAuditor().getId() == principal.getActiveRoleId() && auditRecord != null && auditRecord.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int auditRecordId;
		AuditRecord auditRecord;

		auditRecordId = super.getRequest().getData("id", int.class);

		auditRecord = this.repository.findOneAuditRecordById(auditRecordId);

		super.getBuffer().addData(auditRecord);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "initialMoment", "finalMoment", "mark", "link");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		boolean isCodeChanged = false;
		Collection<String> allCodes;
		AuditRecord auditRecord;

		allCodes = this.repository.findAllAuditRecordsCode();
		auditRecord = this.repository.findOneAuditRecordById(object.getId());

		Date pastMostDate = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !auditRecord.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "validation.auditrecord.error.code.duplicated");
		}

		if (object.getInitialMoment() != null)
			if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
				if (!this.isValidDateFormat(object.getInitialMoment()))
					super.state(false, "initialMoment", "validation.auditrecord.initialMoment.invalid-format");
				else
					super.state(MomentHelper.isAfterOrEqual(object.getInitialMoment(), pastMostDate), "initialMoment", "validation.auditrecord.moment.minimum-date");

		if (object.getFinalMoment() != null)
			if (!super.getBuffer().getErrors().hasErrors("finalMoment"))
				if (!this.isValidDateFormat(object.getFinalMoment()))
					super.state(false, "finalMoment", "validation.auditrecord.finalMoment.invalid-format");
				else
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

	private boolean isValidDateFormat(final Date date) {
		// Convert the Date to String and validate the format manually
		String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
		// Regular expression to validate date format "dd/MM/yyyy HH:mm"
		String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4} ([01]\\d|2[0-3]):[0-5]\\d$";
		return dateStr.matches(regex);
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

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

		super.getResponse().addData(dataset);
	}

}
