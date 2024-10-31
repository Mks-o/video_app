package main.application.exceptions;

@SuppressWarnings("serial")
public class NoContentException extends RuntimeException {
	public NoContentException(String message) {
		super(message);
	}
}
