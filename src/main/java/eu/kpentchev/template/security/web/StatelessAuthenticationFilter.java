package eu.kpentchev.template.security.web;

import eu.kpentchev.template.security.domain.UserAuthentication;
import eu.kpentchev.template.security.service.TokenAuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 
 * @author Konstantin Pentchev <konstantin.pentchev@comeplay.eu>
 *
 * @since 15.12.2015 г.
 */
public class StatelessAuthenticationFilter extends GenericFilterBean {

	private final TokenAuthenticationService tokenAuthenticationService;
	
	//private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;
	

	public StatelessAuthenticationFilter(TokenAuthenticationService taService) {
		this.tokenAuthenticationService = taService;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		UserDetails user = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
		if(user != null){
			SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user));
		}
		chain.doFilter(request, response); // always continue
	}
	
}
