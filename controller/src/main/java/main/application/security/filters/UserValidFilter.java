package main.application.security.filters;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
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
import lombok.extern.slf4j.Slf4j;
import main.application.entity.UserAccount;
import main.application.exceptions.PasswordInvalidException;
import main.application.repository.UserRepository;
import main.application.security.TokenUtils;

@Component
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "filelogger")
//@Order(10)
public class UserValidFilter extends OncePerRequestFilter {
	TokenUtils tokenUtils;
	UserRepository repository;

	@SuppressWarnings("null")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException {
		String message = "";
		try {
			String auth = request.getHeader("Authorization");
			if(auth != null && auth.startsWith("Basic ")){
				String username = null;
				int index = auth.indexOf(" ");
				auth = auth.substring(index + 1);			
				byte[] byteDecode = Base64.getDecoder().decode(auth);
				String token = new String(byteDecode);
				String[] credentials = token.split(":");
				username = credentials[0];
				String password = credentials[1];
				
				UserAccount user = repository.findByLogin(username).get();
				if (user == null)
					message += "Account not exists";
				if (!BCrypt.checkpw(password, user.getPassword())) {
					message += "Password not valid";
					throw new PasswordInvalidException("Incorrect password " + password);
				}
				Set<String> authorityList = new HashSet<>();
				authorityList.add("ROLE_"+user.getRole());
				UsernamePasswordAuthenticationToken tok = new UsernamePasswordAuthenticationToken(username,password,AuthorityUtils.createAuthorityList(authorityList));
				SecurityContextHolder.getContext().setAuthentication(tok); 
						
				
				if (!BCrypt.checkpw(password, user.getPassword()))
					message += "Password not valid";
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", e.getClass().getSimpleName());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(message + "\n" + e.getMessage());
		}

	}
}
