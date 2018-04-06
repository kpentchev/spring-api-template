package eu.kpentchev.template.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.kpentchev.template.security.web.PasswordJsonSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @since 15.12.2015 г.
 */
@EqualsAndHashCode
@Data
@Entity
public class Account implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	private String email;

	@JsonSerialize(using=PasswordJsonSerializer.class)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> grantedAuthorities = new ArrayList<>();
	
	@CreatedDate
	private Date created;
	
	@LastModifiedDate
	private Date lastModified;

	public void grantAuthority(String authority) {
		this.grantedAuthorities.add(authority);
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.grantedAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public static Account of(String email, String password, Collection<String> grantedAuthorities) {
	    final Account account = new Account();
	    account.setEmail(email);
	    account.setPassword(password);
	    account.setGrantedAuthorities(new ArrayList<>(grantedAuthorities));
	    return account;
    }

}
