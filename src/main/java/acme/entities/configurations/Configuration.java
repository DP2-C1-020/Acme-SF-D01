
package acme.entities.configurations;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Configuration extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@Length(min = 3, max = 3)
	@NotBlank
	@Pattern(regexp = "[A-Z]{3}", message = "{configuration.systemCurrency.error}")
	String						systemCurrency;

	@Length(min = 3)
	@NotBlank
	@Pattern(regexp = "([A-Z]{3},\\s)*([A-Z]{3})?", message = "{configuration.acceptedCurrencies.error}")
	String						acceptedCurrencies;

}
