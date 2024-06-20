
package acme.forms;

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

	private Double				avgAmountSponsorship;
	private Double				devAmountSponsorship;
	private Double				minAmountSponsorship;
	private Double				maxAmountSponsorship;

	private Double				avgQuantityInvoice;
	private Double				devQuantityInvoice;
	private Double				minQuantityInvoice;
	private Double				maxQuantityInvoice;

}
