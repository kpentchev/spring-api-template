package eu.kpentchev.template.security.web;

import eu.kpentchev.template.security.domain.Credentials;
import eu.kpentchev.template.security.service.TokenAuthenticationService;
import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 
 * @author Konstantin Pentchev <konstantin.pentchev@comeplay.eu>
 *
 * @since 31.01.2016 г.
 */
@Slf4j
@RequestMapping("/register")
@RestController
public class RegistrationController {

	@Autowired
	private TokenAuthenticationService authService;

	@Autowired
	private RegistrationService service;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Account create(@RequestBody @Valid Credentials credentials, HttpServletResponse response) {
		Account saved = service.register(credentials.getUsername(), credentials.getPassword());

		addAuthHeader(response, saved);

		return saved;
	}

	private void addAuthHeader(HttpServletResponse response, Account account){
		log.debug("RegistrationInterceptor adding credentials...");
		authService.addAuthentication(response, (UserDetails) account);
	}
	
}
