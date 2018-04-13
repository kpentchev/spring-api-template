package eu.kpentchev.template.security.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.kpentchev.template.security.domain.Account;
import eu.kpentchev.template.security.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 
 * @author Konstantin Pentchev <konstantin.pentchev@comeplay.eu>
 *
 * @since 15.12.2015 г.
 */
//@Service
public class JWTAuthenticationService implements TokenAuthenticationService {

	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";

	private Mac hmac;

	@Autowired
	private MappingJackson2HttpMessageConverter mapper;

	@Value("${security.encrypt.jwt.password}")
	private String secretKey;

	@PostConstruct
	public void init(){
		try {
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey.getBytes("UTF-8"), HMAC_ALGO));
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		}
	}

	public void addAuthentication(HttpServletResponse response, UserDetails authentication) {
		response.addHeader(AUTH_HEADER_NAME, createTokenForUser(authentication));
	}

	public UserDetails getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final UserDetails user = parseUserFromToken(token);
			if (user != null) {
				return user;
			}
		}
		return null;
	}

	private UserDetails parseUserFromToken(String token) {
		final String[] parts = token.split(SEPARATOR_SPLITTER);
		if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
			try {
				final byte[] userBytes = fromBase64(parts[0]);
				final byte[] hash = fromBase64(parts[1]);

				boolean validHash = Arrays.equals(createHmac(userBytes), hash);
				if (validHash) {
					final UserDetails user = fromJSON(userBytes);
					return user;
				}
			} catch (IllegalArgumentException e) {
				// log tempering attempt here
			}
		}
		return null;
	}

	private UserDetails fromJSON(final byte[] userBytes) {
		try {
			return this.mapper.getObjectMapper().readValue(new ByteArrayInputStream(userBytes), Account.class);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private byte[] toJSON(UserDetails user) {
		try {
			return this.mapper.getObjectMapper().writeValueAsBytes(user);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	// synchronized to guard internal hmac object
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}

	private String createTokenForUser(UserDetails user) {
		byte[] userBytes = toJSON(user);
		byte[] hash = createHmac(userBytes);
		final StringBuilder sb = new StringBuilder(170);
		sb.append(toBase64(userBytes));
		sb.append(SEPARATOR);
		sb.append(toBase64(hash));
		return sb.toString();
	}



}
