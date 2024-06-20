
package acme.entities.objectives;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private PriorityType		priority;

	@NotNull
	private boolean				status;

	//TODO startDate must be after the instantiationMoment
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				startDate;

	//TODO endDate must be after the startDate
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				endDate;

	@URL
	private String				link;

	// Derived attributes -----------------------------------------------


	@Transient
	public Integer getDuration() {

		Duration duration;
		duration = MomentHelper.computeDuration(this.startDate, this.endDate);
		return (int) duration.toDays();
	}

}
