
package acme.features.authenticated.objectives;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.objectives.Objective;
import acme.entities.project.Project;

@Repository
public interface AuthenticatedObjectiveRepository extends AbstractRepository {

	@Query("select o from Objective o where o.id = :id")
	Objective findOneObjectiveById(int id);

	@Query("select o from Objective o")
	Collection<Objective> findAllObjectives();

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

}
