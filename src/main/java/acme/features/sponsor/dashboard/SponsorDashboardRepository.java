
package acme.features.sponsor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("select count(i) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.tax <= 21.00 and i.draftMode = false")
	Integer totalInvoiceHasTaxLessEqual21(int sponsorId);

	@Query("select count(sp) from Sponsorship sp where sp.sponsor.id = :sponsorId and length(sp.link) > 0 and sp.draftMode = false")
	Integer totalSponsorshipHasLink(int sponsorId);

	@Query("select avg(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false")
	Double avgAmountSponsorship(int sponsorId);

	@Query("select stddev(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false")
	Double devAmountSponsorship(int sponsorId);

	@Query("select min(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false")
	Double minAmountSponsorship(int sponsorId);

	@Query("select max(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false")
	Double maxAmountSponsorship(int sponsorId);

	@Query("select avg(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Double avgQuantityInvoice(int sponsorId);

	@Query("select stddev(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Double devQuantityInvoice(int sponsorId);

	@Query("select min(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Double minQuantityInvoice(int sponsorId);

	@Query("select max(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Double maxQuantityInvoice(int sponsorId);

}
