
package acme.features.manager.managerDashboard;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.userstory.Priority;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select us.priority from UserStory us where us.manager.id = :managerId")
	Collection<Priority> findUserStoriesAsPriorities(int managerId);

	default Map<Priority, Integer> totalNumberUserStoriesByPriority(final int managerId) {
		Map<Priority, Integer> sol;
		Collection<Priority> prioritiesQuery;

		sol = new EnumMap<Priority, Integer>(Priority.class);
		prioritiesQuery = this.findUserStoriesAsPriorities(managerId);

		for (Priority priority : Priority.values())
			sol.put(priority, 0);

		for (Priority priority : prioritiesQuery)
			sol.put(priority, sol.get(priority) + 1);

		return sol;
	}

	@Query("select avg(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	Double avgUserStoryCost(int managerId);

	@Query("select stddev(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	Double devUserStoryCost(int managerId);

	@Query("select min(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	Integer minUserStoryCost(int managerId);

	@Query("select max(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	Integer maxUserStoryCost(int managerId);

	@Query("select avg(p.cost) from Project p where p.manager.id = :managerId")
	Double avgProjectCost(int managerId);

	@Query("select stddev(p.cost) from Project p where p.manager.id = :managerId")
	Double devProjectCost(int managerId);

	@Query("select min(p.cost) from Project p where p.manager.id = :managerId")
	Integer minProjectCost(int managerId);

	@Query("select max(p.cost) from Project p where p.manager.id = :managerId")
	Integer maxProjectCost(int managerId);

}
