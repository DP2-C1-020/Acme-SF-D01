
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import acme.entities.userstory.Priority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	// Numero total de historias de usuario segun prioridad

	private Map<Priority, Integer>	totalNumberUserStoriesByPriority;

	// Promedio, mínimo, máximo y desviación estándar del costo de los projectos

	private Double					avgProjectCost;
	private Integer					minProjectCost;
	private Integer					maxProjectCost;
	private Double					devProjectCost;

	// Promedio, mínimo, máximo y desviación estándar del costo de las historias de usuario

	private Double					avgUserStoryCost;
	private Integer					minUserStoryCost;
	private Integer					maxUserStoryCost;
	private Double					devUserStoryCost;

}
