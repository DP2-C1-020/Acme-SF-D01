
package acme.entities.project;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.entities.userstory.UserStory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Project extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Pattern(regexp = "[A-Z]{3}-[0-9]{4}")
	@NotBlank
	@Column(unique = true)
	private String				code;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				abstracto;

	private Boolean				fatalErrors;

	@Min(0)
	@Column(nullable = true)
	private Integer				cost;

	@Column(nullable = true)
	private String				link;


	@AssertTrue(message = "Projects containing fatal errors must be rejected")
	public boolean isFatalErrorsValid() {
		return this.fatalErrors == null || !this.fatalErrors;
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------


	//@NotEmpty
	@OneToMany(mappedBy = "project")
	private Collection<UserStory> userStories;
}
