
package acme.roles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Developer extends AbstractRole {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Length(max = 76)
	private String				degree;

	@NotBlank
	@Length(max = 101)
	private String				specialisation;

	@NotBlank
	@Length(max = 101)
	private String				skills;

	@Email
	private String				email;

	@Column(nullable = true)
	@URL
	private String				link;

}
