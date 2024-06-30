
package acme.features.client.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Client;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select c from Client c where c.userAccount.id = :id")
	Client findOneClientByUserAccountId(int id);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 25 and pl.contract.draftMode = false")
	double findNumOfProgressLogsLess25(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 50 and 25 <= pl.completeness and pl.contract.draftMode = false")
	double findNumOfProgressLogsWithRate25to50(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 75 and 50 <= pl.completeness and pl.contract.draftMode = false")
	double findNumOfProgressLogsWithRate50to75(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and 75 <= pl.completeness and pl.contract.draftMode = false")
	double findNumOfProgressLogsWithRateOver75(Client client);

	@Query("select avg(c.budget.amount) from Contract c where c.client = :client and c.draftMode = false and c.budget.currency = :s")
	Double findAverageBudget(Client client, String s);

	@Query("select stddev(c.budget.amount) from Contract c where c.client = :client and c.draftMode = false and c.budget.currency = :s")
	Double findDeviationBudget(Client client, String s);

	@Query("select min(c.budget.amount) from Contract c where c.client = :client and c.draftMode = false and c.budget.currency = :s")
	Double findMinBudget(Client client, String s);

	@Query("select max(c.budget.amount) from Contract c where c.client = :client and c.draftMode = false and c.budget.currency = :s")
	Double findMaxBudget(Client client, String s);

	@Query("select sc.acceptedCurrencies from SystemConfiguration sc")
	String findAcceptedCurrencies();

}
