
package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	protected Date				moment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				endDate;

	@URL
	protected String			linkPhoto;

	@NotBlank
	@Length(max = 75)
	protected String			slogan;

	@URL
	protected String			linkDocument;

}
