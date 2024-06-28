
package acme.entities.contracts;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.entities.project.Project;
import acme.roles.Client;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "client_id"), @Index(columnList = "project_id"), @Index(columnList = "code"), @Index(columnList = "draftMode")
})

public class Contract extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$")
	protected String			code;

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			providerName;

	@NotBlank
	@Length(max = 75)
	protected String			customerName;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	@NotNull
	protected Money				budget;

	protected boolean			draftMode;

	// Relationships ----------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Project			project;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Client			client;

}
