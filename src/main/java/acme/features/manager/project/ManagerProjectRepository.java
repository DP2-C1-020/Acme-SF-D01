
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
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
	/*
	 * @Query("select j from Job j where j.draftMode = false and j.deadline > :currentMoment")
	 * Collection<Job> findManyJobsByAvailability(Date currentMoment);
	 * 
	 * @Query("select d from Duty d where d.job.id = :jobId")
	 * Collection<Duty> findManyDutiesByJobId(int jobId);
	 * 
	 * @Query("select sum(d.workLoad) from Duty d where d.job.id = :jobId")
	 * Double computeWorkLoadByJobId(int jobId);
	 * 
	 * @Query("select c from Company c")
	 * Collection<Company> findAllContractors();
	 * 
	 * @Query("select c from Company c where c.id = :contractorId")
	 * Company findOneContractorById(int contractorId);
	 * 
	 * @Query("select wf.contractor from WorksFor wf where wf.proxy.id = :employerId")
	 * Collection<Company> findManyContractorsByEmployerId(int employerId);
	 */
}
