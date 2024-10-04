
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<String, Integer>	progressLogByCompletenessRate;

	private Double					averageBudget;
	private Double					deviationBudget;
	private Double					minBudget;
	private Double					maxBudget;

}
