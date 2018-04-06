package eu.kpentchev.template.security;

import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.domain.UserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {
	
	public Account getCurrentAccount() {
		return (Account) ((UserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
	}
}
