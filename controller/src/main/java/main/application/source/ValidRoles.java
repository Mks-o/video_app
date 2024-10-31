package main.application.source;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
/**
 * https://www.youtube.com/watch?v=gihbgwkNWug
 * https://stackoverflow.com/questions/6294587/java-string-validation-using-enum-values-and-annotation
 */
@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {
	String message() default "Invalid Role, Role must be ADMINISTRATOR, USER or GUEST";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
