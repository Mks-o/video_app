package main.application.configuration;

import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import main.application.Application;

@Component
@Endpoint(id = "changeLogLevel")
public class LoggindLevel {

	@WriteOperation
	public void setLoggingLevelDebug(@Selector Boolean flag) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = loggerContext.getLogger(Application.class.getPackageName());
		if (flag) {
			logger.setLevel(Level.toLevel("DEBUG"));
		} else
			logger.setLevel(Level.toLevel("INFO"));
	}

}
