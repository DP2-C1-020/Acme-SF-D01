
package acme.features.client.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.progress_logs.ProgressLog;

@Repository
public interface ClientProgressLogsRepository extends AbstractRepository {

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findProgressLogByContractId(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogById(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl.recordId from ProgressLog pl")
	Collection<String> findAllProgressLogCodes();

	@Query("select pl.contract from ProgressLog pl where pl.id = :id")
	Contract findContractByProgressLogId(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId and pl.registrationMoment = (select max(pl2.registrationMoment) from ProgressLog pl2 where pl2.contract.id = :contractId)")
	ProgressLog findLastProgressLogByContractId(@Param("contractId") int contractId);

}
