package com.quintor.worqplace.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quintor.worqplace.security.data.User;
import com.quintor.worqplace.security.presentation.dto.Login;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Tries to authenticate a user, based on the incoming request.
 * <p>
 * Once authenticated, it will return a Bearer token (JWT) set in the
 * Authorization header of the 200 Response.
 * <p>
 * This exact Bearer has to be added in the Authorization header of subsequent
 * requests to restricted endpoints.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private final String secret;
	private final Integer expirationInMs;

	private final AuthenticationManager authenticationManager;

	/**
	 * Constructor of the class {@link JwtAuthenticationFilter} that extends
	 * {@link AbstractAuthenticationProcessingFilter}.
	 *
	 * @param path                  Path.
	 * @param secret                Secret.
	 * @param expirationInMs        Expiration in milliseconds.
	 * @param authenticationManager {@link AuthenticationManager} object.
	 */
	public JwtAuthenticationFilter(
			String path,
			String secret,
			Integer expirationInMs,
			AuthenticationManager authenticationManager) {

		super(new AntPathRequestMatcher(path));

		this.secret = secret;
		this.expirationInMs = expirationInMs;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		Login login = new ObjectMapper()
				.readValue(request.getInputStream(), Login.class);

		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.username(), login.password())
		);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain filterChain, Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		List<String> roles = user
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		byte[] signingKey = this.secret.getBytes();

		String token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("type", "JWT")
				.setIssuer("quintor-worqplace-api")
				.setAudience("quintor-worqplace")
				.setSubject(user.getEmployee().getId().toString())
				.setExpiration(new Date(System.currentTimeMillis() + this.expirationInMs))
				.claim("role", roles)
				.compact();

		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
	}
}
