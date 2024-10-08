
package acme.features.auditor.audit_record;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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

	// Constructors ------------------------------------------------------------


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

		// Validación de código único
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !auditRecord.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allCodes.contains(object.getCode()), "code", "validation.auditrecord.error.code.duplicated");
		}

		// Obtener el idioma actual de la página
		Locale currentLocale = super.getRequest().getLocale();
		boolean isSpanish = currentLocale.getLanguage().equals("es");

		// Validar el formato de la fecha dependiendo del idioma de la página
		String expectedFormat = isSpanish ? "dd/MM/yyyy HH:mm" : "yyyy/MM/dd HH:mm";

		// Validar si el initialMoment es nulo o tiene un formato incorrecto
		String initialMomentStr = super.getRequest().getData("initialMoment", String.class);
		if (initialMomentStr == null || initialMomentStr.isEmpty())
			super.state(false, "initialMoment", "validation.auditrecord.initialMoment.invalid-format");
		else if (!this.isValidDateString(initialMomentStr, expectedFormat))
			super.state(false, "initialMoment", "validation.auditrecord.initialMoment.invalid-format");
		else {
			// Validar que la fecha sea posterior o igual a la fecha mínima
			Date initialMoment = MomentHelper.parse(initialMomentStr, expectedFormat);
			super.state(MomentHelper.isAfterOrEqual(initialMoment, pastMostDate), "initialMoment", "validation.auditrecord.moment.minimum-date");
		}

		// Validar si el finalMoment es nulo o tiene un formato incorrecto
		String finalMomentStr = super.getRequest().getData("finalMoment", String.class);
		if (finalMomentStr == null || finalMomentStr.isEmpty())
			super.state(false, "finalMoment", "validation.auditrecord.finalMoment.invalid-format");
		else if (!this.isValidDateString(finalMomentStr, expectedFormat))
			super.state(false, "finalMoment", "validation.auditrecord.finalMoment.invalid-format");
		else {
			// Validar que la fecha sea posterior o igual a la fecha mínima
			Date finalMoment = MomentHelper.parse(finalMomentStr, expectedFormat);
			super.state(MomentHelper.isAfterOrEqual(finalMoment, pastMostDate), "finalMoment", "validation.auditrecord.moment.minimum-date");
		}

		// Validar la relación entre initialMoment y finalMoment
		if (initialMomentStr != null && finalMomentStr != null && this.isValidDateString(initialMomentStr, expectedFormat) && this.isValidDateString(finalMomentStr, expectedFormat)) {
			Date initialMoment = MomentHelper.parse(initialMomentStr, expectedFormat);
			Date finalMoment = MomentHelper.parse(finalMomentStr, expectedFormat);
			if (!super.getBuffer().getErrors().hasErrors("initialMoment"))
				super.state(MomentHelper.isAfter(finalMoment, initialMoment), "initialMoment", "validation.auditrecord.moment.initial-after-final");

			if (!super.getBuffer().getErrors().hasErrors("finalMoment")) {
				Date minimumEnd = MomentHelper.deltaFromMoment(initialMoment, 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(finalMoment, minimumEnd), "finalMoment", "validation.auditrecord.moment.minimum-one-hour");
			}
		}
	}

	// Función para validar si una cadena de fecha coincide con el formato esperado
	private boolean isValidDateString(final String dateStr, final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
			return true;
		} catch (Exception e) {
			return false;
		}
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
