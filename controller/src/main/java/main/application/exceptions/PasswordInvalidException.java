package main.application.exceptions;

@SuppressWarnings("serial")
public class PasswordInvalidException extends RuntimeException {
	public PasswordInvalidException(String message) {
		super(message);
	}
}
