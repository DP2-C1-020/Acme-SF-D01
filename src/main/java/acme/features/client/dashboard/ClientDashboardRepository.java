
package acme.features.client.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Client;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select c from Client c where c.userAccount.id = :id")
	Client findOneClientByUserAccountId(int id);

	@Query("select avg(c.budget.amount) from Contract c where c.client = :client")
	Double findAverageBudget(Client client);

	@Query("select stddev(c.budget.amount) from Contract c where c.client = :client")
	Double findDeviationBudget(Client client);

	@Query("select min(c.budget.amount) from Contract c where c.client = :client")
	Double findMinBudget(Client client);

	@Query("select max(c.budget.amount) from Contract c where c.client = :client ")
	Double findMaxBudget(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 25")
	Integer findNumOfProgressLogsLess25(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 50 and 25 <= pl.completeness")
	Integer findNumOfProgressLogsWithRate25to50(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 75 and 50 <= pl.completeness")
	Integer findNumOfProgressLogsWithRate50to75(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and 75 <= pl.completeness")
	Integer findNumOfProgressLogsWithRateOver75(Client client);

}
