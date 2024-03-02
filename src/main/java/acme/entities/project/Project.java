
package acme.entities.project;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

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

	@URL
	@Column(nullable = true)
	private String				link;

	private boolean				draftMode;


	// Derived attributes -----------------------------------------------------
	@Transient
	public boolean isPublished() {
		boolean result;

		result = !this.draftMode;

		return result;
	}
	@Transient
	public Integer getCosteHU() {
		Integer coste;

		coste = this.userStories.stream().mapToInt(us -> us.getEstimatedCost()).sum();

		return coste;
	}

	// Relationships ----------------------------------------------------------


	//@NotEmpty
	@OneToMany(mappedBy = "project")
	private Collection<UserStory> userStories;
}
