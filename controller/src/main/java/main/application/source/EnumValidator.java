package main.application.source;

import java.util.Arrays;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import main.application.constants.Roles;

public class EnumValidator implements ConstraintValidator<ValidRoles, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return Arrays.stream(Roles.values()).anyMatch(roles->roles.name().equals(value));
	}
}
