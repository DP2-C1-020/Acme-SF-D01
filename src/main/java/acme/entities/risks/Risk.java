
package acme.entities.risks;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
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
public class Risk extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "R-[0-9]{3}")
	private String				reference;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				identificationDate;

	@Positive
	@NotNull
	private double				impact;

	@Digits(integer = 3, fraction = 2)
	@Min(0)
	@Max(100)
	@NotNull
	private double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;
	
	@URL
	private String link;
	

	// Derived attributes -----------------------------------------------

	@NotNull
	@Transient
	public double				value				= this.impact * this.probability;

	// Relationships ----------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = true)
	protected Project			project;
}
