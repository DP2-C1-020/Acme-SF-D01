
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import acme.entities.audits.CodeAuditType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	private static final long			serialVersionUID	= 1L;

	private Map<CodeAuditType, Integer>	totalCodeAuditTypes;

	private Double						averageAuditRecords;
	private Double						deviationAuditRecords;
	private Integer						minimumAuditRecords;
	private Integer						maximumAuditRecords;

	private Double						averageRecordPeriod;
	private Double						deviationRecordPeriod;
	private Double						minimumRecordPeriod;
	private Double						maximumRecordPeriod;

}
