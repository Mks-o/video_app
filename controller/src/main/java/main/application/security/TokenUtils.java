package main.application.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.application.constants.Constants;
@Component
public class TokenUtils {
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		claims.put("role", rolesList);
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + Constants.MILISTIME);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(startDate)
				.setExpiration(endDate)
				.signWith(SignatureAlgorithm.HS256, Constants.SECRET)
				.compact();
	}
		
	public String getUsername(String token) {
		return getAllClaimsFromTocken(token).getSubject();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRole(String token){
		return getAllClaimsFromTocken(token).get("role",List.class);
	}
	
	private Claims getAllClaimsFromTocken(String token) {
		return Jwts.parser()
				.setSigningKey(Constants.SECRET)
				.parseClaimsJws(token)
				.getBody();
	}
}
