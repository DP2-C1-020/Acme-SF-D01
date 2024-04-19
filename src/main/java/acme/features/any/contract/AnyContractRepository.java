
package acme.features.any.contract;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;

@Repository
public interface AnyContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.draftMode = false")
	Collection<Contract> findPublishedContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	List<ProgressLog> findProgressLogsByContract(int id);

}
