
package acme.validators;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorMoney {

	@Autowired
	private ValidatorMoneyRepository repository;


	public Boolean moneyValidator(final String currency) {
		String acceptedCurrencies = this.repository.findAcceptedCurrencies();

		if (acceptedCurrencies == null || acceptedCurrencies.trim().isEmpty())
			throw new IllegalArgumentException("Accepted currencies are not configured properly.");

		List<String> listAcceptedCurrencies = Arrays.asList(acceptedCurrencies.split(",\\s*"));
		return listAcceptedCurrencies.stream().anyMatch(currency::equalsIgnoreCase);
	}
}
