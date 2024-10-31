package main.application.configuration;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import main.application.service.MainService;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartInitConfiguration {

	@Autowired
	MainService videoservice;
	
	@PostConstruct
	private void init() throws IOException, ParserConfigurationException, SAXException {
		videoservice.generateRandomContent();
	}
	
	@Bean
	OpenAPI openAPI() {
	    return new OpenAPI().addSecurityItem(new SecurityRequirement().
	            addList("Bearer Authentication"))
	        .components(new Components().addSecuritySchemes
	            ("Bearer Authentication", createAPIKeyScheme()))
	        .info(new Info().title("RandomVideoContentApplication")
	            .description("Authentication contains requests for authentication and authorization"
	            		+ " \n\n UserAccount contains requests for C.R.U.D. user accounts"
	            		+ " \n\n VideoContent contains requests for C.R.U.D. video content data")
	            .version("1.0.0").contact(new Contact().name("Maksim Ort")
	                .email( "www.baeldung.com").url("somemailtestnotexists@gmail.com"))
	            .license(new License().name("License of API")
	                .url("API license URL")));
	}
		
	SecurityScheme createAPIKeyScheme() {
		    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
		        .bearerFormat("JWT")
		        .scheme("bearer");
		}
}
