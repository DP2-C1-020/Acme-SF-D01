
package acme.features.sponsor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("select count(i) from Invoice i where i.tax <= 21.00")
	Integer totalInvoiceHasTaxLessEqual21();

	@Query("select count(sp) from Sponsorship sp where length(sp.link) > 0")
	Integer totalSponsorshipHasLink();

	@Query("select avg(sp.amount.amount) from Sponsorship sp")
	Double avgAmountSponsorship();

	@Query("select stddev(sp.amount.amount) from Sponsorship sp")
	Double devAmountSponsorship();

	@Query("select min(sp.amount.amount) from Sponsorship sp")
	Double minAmountSponsorship();

	@Query("select max(sp.amount.amount) from Sponsorship sp")
	Double maxAmountSponsorship();

	@Query("select avg(i.quantity.amount) from Invoice i")
	Double avgQuantityInvoice();

	@Query("select stddev(i.quantity.amount) from Invoice i")
	Double devQuantityInvoice();

	@Query("select min(i.quantity.amount) from Invoice i")
	Double minQuantityInvoice();

	@Query("select max(i.quantity.amount) from Invoice i")
	Double maxQuantityInvoice();

}
