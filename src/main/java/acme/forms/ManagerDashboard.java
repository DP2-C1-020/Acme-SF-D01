
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// Numero total de historias de usuario segun prioridad

	private Integer				totalMust;
	private Integer				totalShould;
	private Integer				totalCould;
	private Integer				totalWont;

	// Promedio, mínimo, máximo y desviación estándar del costo de los projectos

	private Double				avgProjectCost;
	private Double				minProjectCost;
	private Double				maxProjectCost;
	private Double				devProjectCost;

	// Promedio, mínimo, máximo y desviación estándar del costo de las historias de usuario

	private Double				avgUserStoryCost;
	private Double				minUserStoryCost;
	private Double				maxUserStoryCost;
	private Double				devUserStoryCost;

}
