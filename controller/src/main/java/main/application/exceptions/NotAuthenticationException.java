package main.application.exceptions;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@ToString
public class NotAuthenticationException extends RuntimeException{
	
	public NotAuthenticationException(int status, String message) {
		super(LocalDateTime.now()+ " " + status + " " + message);
	}
}
