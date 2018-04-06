package eu.kpentchev.template.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @since 16.12.2015 г.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Credentials {
	
	private String username;
	
	private String password;


}
