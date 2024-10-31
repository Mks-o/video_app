package main.application.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import main.application.entity.*;
import main.application.source.ValidRoles;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Schema

public class UserAccountDto {
	@Schema(description = "Unique field id", type = "Long", example = "1")
	Long id;
	
	@Schema(description = "Not empty field must contains from 5 to 20 symbols", type = "String", example = "name")
	@Size(max=20, min=5, message = "Name must contains from 5 to 20 symbols")
	@NotEmpty
	@NotBlank
	String name;
	
	@Size( max=20, min=5, message = "Login must contains from 5 to 20 symbols")
	@Schema(description = "Not empty field must contains from 5 to 20 symbols", type = "String", example = "someLogin")
	@NotEmpty
	String login;
	
	@Schema(description = "mail", type = "String", example = "name@gmail.il")
	String mail;
	
	@Schema(defaultValue = "USER", description = "role [USER, GUEST, ADMINISTRATOR]", type = "Enum", example = "USER", allowableValues = {"USER", "ADMINISTRATOR", "GUEST"})
	@ValidRoles
	String role;
	
	LocalDate birthdate;

	@Schema(description = "raiting must be in range [-2000000, 2000000]", type = "Integer", example = "0")
	@Min(value = -2000000, message = "raiting must be greater then -2000000")
	@Max(value = 2000000, message = "raiting must be less then 2000000")
	Integer raiting;
	
	@Schema(description = "user video content", type = "Null", example = " ")
	@Setter
	@Nullable
	List<VideoContent> videos;
	
	@Setter
	@Schema(description = "user comments", type = "Null", example = " ")
	 @Nullable
	List<Comment> comments;
	
	@Valid
	public UserAccount convertToEntity(@Size( max=80, min=5, message = "Password must contains only a-z A-Z 0-9 characters and in range 5-15 symbols") String password) {
		return new UserAccount(id, name, login, password, mail, role, birthdate, raiting, videos, comments);
	}
}
