package eu.kpentchev.template.security.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 
 * @author Konstantin Pentchev <konstantin.pentchev@comeplay.eu>
 *
 * @since 15.12.2015 г.
 */
public class UserAuthentication implements Authentication{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4841635353089386060L;
	private final UserDetails user;
	
	private boolean isAuthenticated;
	
	public UserAuthentication(UserDetails user) {
		this.user = user;
		this.isAuthenticated = true;
	}

	public String getName() {
		return this.user.getUsername();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getAuthorities();
	}

	public Object getCredentials() {
		return this.user.getPassword();
	}

	public Object getDetails() {
		return this.user;
	}

	public Object getPrincipal() {
		return this.user;
	}

	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.isAuthenticated = isAuthenticated;
	}

}
