
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoices.Invoice;
import acme.entities.project.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select sp from Sponsorship sp where sp.id = :sponsorshipId")
	Sponsorship findOneSponsorshipById(int sponsorshipId);

	@Query("select sp from Sponsorship sp where sp.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

	@Query("select sp from Sponsorship sp where sp.sponsor.id = :sponsorId")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int sponsorId);

	@Query("select s from Sponsor s where s.id = :sponsorId")
	Sponsor findOneSponsorById(int sponsorId);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAvailableProjects();

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select i from Invoice i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select round(sum(i.quantity.amount * (1 + i.tax/100)), 2) from Invoice i where i.sponsorship.id = :sponsorshipId")
	Double computeTotalAmountBySponsorshipId(int sponsorshipId);

}
