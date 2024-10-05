
package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface AnySponsorshipRepository extends AbstractRepository {

	@Query("select sp from Sponsorship sp where sp.draftMode = false")
	Collection<Sponsorship> findAllPublishedSponsorships();

	@Query("select sp from Sponsorship sp where sp.id = :sponsorshipId")
	Sponsorship findOneSponsorshipById(int sponsorshipId);

}
