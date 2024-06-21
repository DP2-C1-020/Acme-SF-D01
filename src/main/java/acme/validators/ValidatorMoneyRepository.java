
package acme.validators;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ValidatorMoneyRepository extends AbstractRepository {

	@Query("select c.acceptedCurrencies from Configuration c")
	String findAcceptedCurrencies();

}
