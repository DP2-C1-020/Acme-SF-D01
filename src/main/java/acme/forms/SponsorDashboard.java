
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				totalInvoiceHasTaxLessEqual21;

	private Integer				totalSponsorshipHasLink;

	private Map<String, Double>	avgAmountSponsorship;
	private Map<String, Double>	devAmountSponsorship;
	private Map<String, Double>	minAmountSponsorship;
	private Map<String, Double>	maxAmountSponsorship;

	private Map<String, Double>	avgQuantityInvoice;
	private Map<String, Double>	devQuantityInvoice;
	private Map<String, Double>	minQuantityInvoice;
	private Map<String, Double>	maxQuantityInvoice;

}
