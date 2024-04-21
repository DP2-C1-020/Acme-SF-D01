
package acme.entities.audits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^AU-[0-9]{4}-[0-9]{3}$")//, message = "{validation.record.code}")
	private String				code;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				initialMoment;

	//TODO: restricion, at least one hour
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				finalMoment;

	@NotNull
	private Mark				mark;

	@URL
	protected String			link;

	private boolean				draftMode;

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected CodeAudit			codeAudit;

}
