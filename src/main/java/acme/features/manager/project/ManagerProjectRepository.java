
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.CodeAudit;
import acme.entities.contracts.Contract;
import acme.entities.project.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.training_module.TrainingModule;
import acme.entities.userstory.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("select m from Manager m where m.id = :id")
	Manager findOneManagerById(int id);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findManyProjectsByManagerId(int managerId);

	@Query("select p_us.userStory from ProjectUserStory p_us where p_us.project.id = :projectId")
	Collection<UserStory> findManyUserStoriesByProjectId(int projectId);

	@Query("select ca from CodeAudit ca where ca.project.id = :projectId")
	Collection<CodeAudit> findManyCodeAuditsByProjectId(int projectId);

	@Query("select c from Contract c where c.project.id = :projectId")
	Collection<Contract> findManyContractsByProjectId(int projectId);

	@Query("select s from Sponsorship s where s.project.id = :projectId")
	Collection<Sponsorship> findManySponsorshipsByProjectId(int projectId);

	@Query("select tm from TrainingModule tm where tm.project.id = :projectId")
	Collection<TrainingModule> findManyTrainingModulesByProjectId(int projectId);

}
