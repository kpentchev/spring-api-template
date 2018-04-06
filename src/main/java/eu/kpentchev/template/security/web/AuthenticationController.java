package eu.kpentchev.template.security.web;

import eu.kpentchev.template.security.domain.Credentials;
import eu.kpentchev.template.security.service.TokenAuthenticationService;
import eu.kpentchev.template.security.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @since 15.12.2015 г.
 */
@RestController
@RequestMapping(AuthenticationController.mapping)
public class AuthenticationController {
	
	public static final String mapping = "/authenticate";

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authManager;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Account login(@RequestBody Credentials user, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		final Authentication authentication = authManager.authenticate(loginToken);
		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, (UserDetails) authentication.getPrincipal());

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Account account = (Account) authentication.getPrincipal();
		
		return account;

	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handleBadCredentials(AuthenticationException e) {
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

}
