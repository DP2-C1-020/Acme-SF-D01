
package acme.entities.banners;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	private Date				instantiationMoment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				endDate;

	@NotBlank
	@URL
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@NotBlank
	@URL
	private String				link;

	// Derived attributes -----------------------------------------------


	@Transient
	@NotNull
	@Min(7)
	public Integer getDisplayPeriod() {
		//TODO implement in the future (difference between startDate and endDate)
		return 7;

	}

}
