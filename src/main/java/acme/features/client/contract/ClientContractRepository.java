
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;
import acme.entities.project.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select c from Client c where c.id = :id")
	Client findClientById(int id);

	@Query("select c.code from Contract c")
	Collection<String> findAllContractsCode();

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAllProjectsWithoutDraftMode();

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select pL from ProgressLog pL where pL.id = :id")
	Collection<ProgressLog> findManyProgressLogByContractId(int id);

	@Query("select c from Contract c where c.client.id = :id")
	Collection<Contract> findContractByClientId(int id);

}
