package eu.kpentchev.template.security.service.impl;

import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

@Transactional
public class DatabaseUserDetailsService implements UserDetailsService{
	
	public static final String ADMIN_EMAIL = "admin@comeplay.eu";
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostConstruct
	public void init(){
		Optional<Account> account = accountRepository.findOneByEmail(ADMIN_EMAIL);
		if(!account.isPresent()){
			accountRepository.save(Account.of(ADMIN_EMAIL, encoder.encode("root"), Arrays.asList("ROLE_ADMIN")));
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return accountRepository.findOneByEmail(username).orElseThrow(() ->
				new UsernameNotFoundException(String.format("Cannot find username \"%s\".", username)));
	}

	

}
