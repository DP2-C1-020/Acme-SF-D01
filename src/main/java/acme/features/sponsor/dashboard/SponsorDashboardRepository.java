
package acme.features.sponsor.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("select count(i) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.tax <= 21.00 and i.draftMode = false")
	Integer totalInvoiceHasTaxLessEqual21(int sponsorId);

	@Query("select count(sp) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.link != null and sp.draftMode = false")
	Integer totalSponsorshipHasLink(int sponsorId);

	@Query("select sp.amount.currency, avg(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false group by sp.amount.currency")
	List<Object[]> avgAmountSponsorshipByCurrency(int sponsorId);

	@Query("select sp.amount.currency, stddev(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false group by sp.amount.currency")
	List<Object[]> devAmountSponsorshipByCurrency(int sponsorId);

	@Query("select sp.amount.currency, min(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false group by sp.amount.currency")
	List<Object[]> minAmountSponsorshipByCurrency(int sponsorId);

	@Query("select sp.amount.currency, max(sp.amount.amount) from Sponsorship sp where sp.sponsor.id = :sponsorId and sp.draftMode = false group by sp.amount.currency")
	List<Object[]> maxAmountSponsorshipByCurrency(int sponsorId);

	@Query("select i.quantity.currency, avg(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false group by i.quantity.currency")
	List<Object[]> avgQuantityInvoiceByCurrency(int sponsorId);

	@Query("select i.quantity.currency, stddev(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false group by i.quantity.currency")
	List<Object[]> devQuantityInvoiceByCurrency(int sponsorId);

	@Query("select i.quantity.currency, min(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false group by i.quantity.currency")
	List<Object[]> minQuantityInvoiceByCurrency(int sponsorId);

	@Query("select i.quantity.currency, max(i.quantity.amount) from Invoice i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false group by i.quantity.currency")
	List<Object[]> maxQuantityInvoiceByCurrency(int sponsorId);

}
