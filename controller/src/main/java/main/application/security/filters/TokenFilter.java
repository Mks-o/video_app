package main.application.security.filters;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.security.TokenUtils;

/**
 * https://stackoverflow.com/questions/34595605/how-to-manage-exceptions-thrown-in-filters-in-spring
 */

@Component
@Getter
@RequiredArgsConstructor
@Order(20)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenFilter extends OncePerRequestFilter {
	TokenUtils tokenUtils;

	@SuppressWarnings("null")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException {
		try {
			String authHeader = request.getHeader("Authorization");
			String username = null;
			String jwt = null;
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				jwt = authHeader.substring(7);
				username = tokenUtils.getUsername(jwt);
			}
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null,
						tokenUtils.getRole(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			filterChain.doFilter(request, response);
		} catch (
		Exception e) {
			System.out.println(e.getMessage());
			response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
			response.getWriter().write(e.getMessage());
		}
	}
}
