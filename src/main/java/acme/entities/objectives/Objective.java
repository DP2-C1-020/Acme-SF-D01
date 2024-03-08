
package acme.entities.objectives;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.project.Project;
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


	@Positive
	@NotNull
	@Transient
	public Integer getDuration() {
		//TODO implement in the future (difference between startDate and endDate)
		return 1;
	}

	// Relationships ----------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project project;

}
