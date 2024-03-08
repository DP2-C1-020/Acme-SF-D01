
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

	private double				avgAmountSponsorship;
	private double				devAmountSponsorship;
	private double				minAmountSponsorship;
	private double				maxAmountSponsorship;

	private double				avgQuantityInvoice;
	private double				devQuantityInvoice;
	private double				minQuantityInvoice;
	private double				maxQuantityInvoice;

}
