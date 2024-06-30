
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	double						totalLogsBelow25Percent;
	double						totalLogs25To50Percent;
	double						totalLogs50To75Percent;
	double						totalLogsAbove75Percent;

	Map<String, Double>			averageBudget;
	Map<String, Double>			deviationBudget;
	Map<String, Double>			minBudget;
	Map<String, Double>			maxBudget;

}
