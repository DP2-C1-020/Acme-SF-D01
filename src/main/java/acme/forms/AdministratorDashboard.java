
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// Numero total de directivos con cada rol

	private Integer				totalAuditors;
	private Integer				totalClients;
	private Integer				totalConsumers;
	private Integer				totalDevelopers;
	private Integer				totalManagers;
	private Integer				totalProviders;

	//proporción de avisos con una dirección de correo electrónico y un enlace

	private Double				ratioLinkAndEmailNotices;

	//proporción de objetivos criticos y no criticos
	private Double				ratioCriticalObjetives;
	private Double				retioNonCriticalObjetives;

	// Promedio, mínimo, máximo y desviación estándar del valor de los riesgos

	private Double				avgRiskValue;
	private Double				minRiskValue;
	private Double				maxRiskValue;
	private Double				devRiskValue;

	// Promedio, mínimo, máximo y desviación estándar del numero de reclamaciones publicadas durante las ultimas 10 semanas

	private Double				avgLast10WeeksPostedClaims;
	private Double				minLast10WeeksPostedClaims;
	private Double				maxLast10WeeksPostedClaims;
	private Double				devLast10WeeksPostedClaims;

}
