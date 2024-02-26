
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private int					totalLogsBelow25Percent;
	private int					totalLogs25To50Percent;
	private int					totalLogs50To75Percent;
	private int					totalLogsAbove75Percent;

	private double				averageBudget;
	private double				deviationBudget;
	private double				minBudget;
	private double				maxBudget;

}
