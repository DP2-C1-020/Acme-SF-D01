
package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerUserStoryRepository extends AbstractRepository {

	@Query("select us from UserStory us where us.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("select us from UserStory us where us.manager.id = :managerId")
	Collection<UserStory> findManyUserStoriesByManagerId(int managerId);

	@Query("select p_us.userStory from ProjectUserStory p_us where p_us.project.id = :projectId")
	Collection<UserStory> findManyUserStoriesByProjectId(int projectId);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select p_us from ProjectUserStory p_us where p_us.userStory.id = :userStoryId")
	Collection<ProjectUserStory> findManyProjectUserStoryTablesByUserStoryId(int userStoryId);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

}
