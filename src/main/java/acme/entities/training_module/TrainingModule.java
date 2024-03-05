
package acme.entities.training_module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingModule extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//	Attributes

	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@NotBlank
	@Column(unique = true)
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				creationMoment;

	@NotBlank
	@Length(max = 101)
	private String				details;

	private DifficultyLevel		difiDifficultyLevel;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				updateMoment;

	@URL
	@Column(nullable = true)
	private String				link;

	@Temporal(TemporalType.TIME)
	private Date				totalTime;

}
