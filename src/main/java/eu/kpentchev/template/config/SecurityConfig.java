package eu.kpentchev.template.config;

import eu.kpentchev.template.security.service.DatabaseUserDetailsService;
import eu.kpentchev.template.security.service.JWTAuthenticationService;
import eu.kpentchev.template.security.service.TokenAuthenticationService;
import eu.kpentchev.template.security.web.StatelessAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * 
 * @author Konstantin Pentchev <konstantin.pentchev@comeplay.eu>
 *
 * @since 15.12.2015 г.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(userDetailsService());
	    provider.setPasswordEncoder(passwordEncoder());
		auth.authenticationProvider(provider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.httpBasic()
				.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
			.exceptionHandling()
				.accessDeniedHandler(new AccessDeniedHandlerImpl())
				.and()
			.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/**")
					.permitAll()
				.anyRequest()
					.authenticated()
				.and()
			.addFilterBefore(new StatelessAuthenticationFilter(jwtAuthenticationservice()), BasicAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public UserDetailsService userDetailsService(){
		return new DatabaseUserDetailsService();
	}
	
	@Bean
	public TokenAuthenticationService jwtAuthenticationservice(){
		return new JWTAuthenticationService();
	}

}
