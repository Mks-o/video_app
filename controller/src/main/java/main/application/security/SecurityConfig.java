package main.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import main.application.security.filters.TokenFilter;
import main.application.security.filters.UserValidFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
/**
 * https://stackoverflow.com/questions/77196323/spring-boot-3-configure-spring-security-to-allow-swagger-ui
 */
public class SecurityConfig {
	UserService userService;
	TokenFilter tokenfilter;
	UserValidFilter userValidFilter;

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("http://localhost:8081"); // change here
		corsConfiguration.addAllowedOrigin("http://localhost:8082");
		corsConfiguration.addAllowedMethod("user/**");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(
						sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers("/registration/**").permitAll();
			authorize.requestMatchers(
					"/user/**",
					"/video/**",
					"/comments/**").hasAnyRole("ADMINISTRATOR", "USER");
			authorize.requestMatchers("/authenticated/**").permitAll();
		});

		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/h2-console/**")
				.permitAll())
				.headers(headers -> headers.frameOptions(opt -> opt.disable()));

		http.authorizeHttpRequests(request -> request.requestMatchers(
				"/v3/api-docs/**",
				"http://localhost:8081",
				"http://localhost:8080/v3/localhost:8080/",
				"/swagger-ui/**",
				"/swagger-ui.html").permitAll()
				.anyRequest().authenticated());

		http.addFilterBefore(tokenfilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(userValidFilter, TokenFilter.class);
		return http.build();
	}

	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(encoder());
		daoAuthenticationProvider.setUserDetailsService(userService);
		return daoAuthenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
