
package acme.features.authenticated.risks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.risks.Risk;

@Repository
public interface AuthenticatedRiskRepository extends AbstractRepository {

	@Query("select r from Risk r")
	Collection<Risk> findAllRisks();

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select r from Risk r where r.id = :id")
	Risk findOneRiskById(int id);
}
