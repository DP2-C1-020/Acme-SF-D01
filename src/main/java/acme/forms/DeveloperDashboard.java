
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Integer				totalTrainingModulesWithUpdateMoment;

	private Integer				totalTrainingSessionWithLink;

	private Double				trainingModulesAverageTime;

	private Double				trainingModulesDeviationTime;

	private Integer				trainingModulesMinimumTime;

	private Integer				trainingModulesMaximumTime;

}
