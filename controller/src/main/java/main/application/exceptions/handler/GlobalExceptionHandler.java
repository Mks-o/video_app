package main.application.exceptions.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import main.application.exceptions.ContentExistException;
import main.application.exceptions.NoContentException;
import main.application.exceptions.NotAuthenticationException;
import main.application.exceptions.NotFoundException;
import main.application.exceptions.PasswordInvalidException;

@Slf4j(topic = "filelogger") //<- logger
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ContentExistException.class)
	public ResponseEntity<?> handleAuthentication(ContentExistException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(NoContentException.class)
	public ResponseEntity<?> handleAuthentication(NoContentException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthentication(AuthenticationException authenticationException) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", authenticationException.getClass().getSimpleName());
		return new ResponseEntity<>(authenticationException.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(NotAuthenticationException.class)
	public ResponseEntity<?> handleAuthentication(NotAuthenticationException notAuthenticationException) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", notAuthenticationException.getClass().getSimpleName());
		return new ResponseEntity<>(notAuthenticationException.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleAuthentication(NotFoundException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<?> handleAuthentication(PasswordInvalidException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.REQUEST_TIMEOUT);
	}

	@ExceptionHandler(WebExchangeBindException.class)
	    public ResponseEntity<?> handleValidationExceptions(final WebExchangeBindException ex) {

	        final BindingResult bindingResult = ex.getBindingResult();
	        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
	        final Map<String, String> errors = new HashMap<>();
	        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
			log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", ex.getClass().getSimpleName());
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<?> handleAuthentication(NoSuchElementException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());

		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleAuthentication(NullPointerException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleAuthentication(ConstraintViolationException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<?> handleAuthentication(PersistenceException exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage() + exception.getLocalizedMessage(), HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAuthentication(Exception exception) {
		log.debug("handleResourceExistsException() -> Exception of type '{}' is occured", exception.getClass().getSimpleName());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
