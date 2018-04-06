package eu.kpentchev.template.security.repository;

import eu.kpentchev.template.security.domain.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository  extends PagingAndSortingRepository<Account, Long> {

	Optional<Account> findOneByEmail(String email);
	
}
