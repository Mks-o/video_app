package main.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "RandomVideoContentApplication", description = "Authentication contains requests for authentication and authorization"
		+ " \n\n UserAccount contains requests for C.R.U.D. user accounts"
		+ " \n\n VideoContent contains requests for C.R.U.D. video content data", version = "1.0.0", summary = "i dont know where int happend"), servers = {
				@Server(description = "default using localhost", url = "http://localhost:8080"),
				@Server(description = "ather server for test", url = "google.com") })

// @PropertySource(name = "swagger.properties", value = { "swagger.properties"
// })
 //@PropertySource(name = "H2.properties", value = { "H2.properties" })
@PropertySource(name = "SQL.properties", value = { "SQL.properties" })
@SpringBootApplication()
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
