package main.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPasswordDto {
	@Schema(description = "Unique field id", type = "Long", example = "1")
	Long userId;
	@Size( max=80, min=5, message =  "Password must contains only a-z A-Z 0-9 characters and in range 5-12 symbols")
	@Schema(description = "Password must contains only a-z A-Z 0-9 characters and in range 5-12 symbols", type = "String", example = "str284rtsZ")
	String password;
}
