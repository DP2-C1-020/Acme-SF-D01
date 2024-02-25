
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private int					totalStaticAudits;
	private int					totalDynamicAudits;

	private double				averageAuditRecords;
	private double				deviationAuditRecords;
	private int					minAuditRecords;
	private int					maxAuditRecords;

	private double				averagePeriodLength;
	private double				deviationPeriodLength;
	private double				minPeriodLength;
	private double				maxPeriodLength;

}
