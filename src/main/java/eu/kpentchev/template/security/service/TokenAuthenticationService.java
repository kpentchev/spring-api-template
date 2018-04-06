package eu.kpentchev.template.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author konstantin.pentchev
 *
 */
public interface TokenAuthenticationService {
	
	/**
	 * Add the authentication token to the response header.
	 * @param response
	 * @param authentication
	 */
	void addAuthentication(HttpServletResponse response, UserDetails authentication);
	
	
	/**
	 * Retrive the authentication from the request header token.
	 * @param request
	 * @return
	 */
	UserDetails getAuthentication(HttpServletRequest request);

}
