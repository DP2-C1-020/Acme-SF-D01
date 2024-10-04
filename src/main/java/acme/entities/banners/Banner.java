
package acme.entities.banners;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "startDate, endDate")
})
public class Banner extends AbstractEntity {

	// Serialisation identifier ----------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	private Date				instantiationMoment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				endDate;

	@URL
	@NotNull
	@Length(min = 7, max = 255)
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@URL
	@NotNull
	@Length(min = 7, max = 255)
	private String				link;

	// Derived attributes -----------------------------------------------


	@Transient
	public int getDisplayPeriod() {

		Duration displayPeriod;
		displayPeriod = MomentHelper.computeDuration(this.startDate, this.endDate);
		return (int) displayPeriod.toDays();
	}

}
