package main.application.exceptions;

@SuppressWarnings("serial")
public class ContentExistException extends RuntimeException {
	public ContentExistException(String message) {
		super(message);
	}
}
