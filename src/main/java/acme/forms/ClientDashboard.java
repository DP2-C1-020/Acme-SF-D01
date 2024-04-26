
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	int							totalLogsBelow25Percent;
	int							totalLogs25To50Percent;
	int							totalLogs50To75Percent;
	int							totalLogsAbove75Percent;

	Double						averageBudget;
	Double						deviationBudget;
	Double						minBudget;
	Double						maxBudget;

}
