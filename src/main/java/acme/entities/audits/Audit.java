
package acme.entities.audits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$")//,  message = "{validation.audit.code}")
	private String				code;

	@Temporal(TemporalType.DATE)
	@PastOrPresent
	@NotNull
	private Date				execution;

	@NotNull
	private AuditType			type;

	@NotBlank
	@Length(max = 100)
	private String				corrective_actions;

	@URL
	private String				link;

	//TODO: Computed mark

}
