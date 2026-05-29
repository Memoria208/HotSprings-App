package hot.spring.controller.error;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerErrorHandler {
	
	/*Add private enum LogStatus and have it let us specify whether we want to log the entire 
	 * stack trace, or just the message. Usually we will log the message only unless we get 
	 * an exception that we’re not expecting.*/
	
	private enum LogStatus {
		STACK_TRACE, MESSAGE_ONLY
	}
	
	/* Add @Data which will add Getters/Setters for us.
	 * 
	 * Create an object that will populate and then return the error handler and Jackson 
	 * will turn that into JSON for us. */
	
	@Data
	private class ExceptionMessage {
		private String message;
		private String statusReason;
		private int statusCode;
		private String timestamp;
		private String uri;
	}
	
	// exception handler for illegal state exception.
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExceptionMessage handleIllegalStateException(IllegalStateException ex, 
			WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.BAD_REQUEST, webRequest, 
				LogStatus.MESSAGE_ONLY);
	}
	
	/*Write code to change the 500 error after trying to delete all contributors.*/
	
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public ExceptionMessage handleUnsupportedOperationException(
		UnsupportedOperationException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.METHOD_NOT_ALLOWED, 
				webRequest, LogStatus.MESSAGE_ONLY);
	}
	
	/*Add handler for unexpected exception.
	/*return with stack trace*/
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionMessage handleException(Exception ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR,
				webRequest, LogStatus.STACK_TRACE);
	}
	
	/*the class we want to handle
	 * change the status that gets returned
	 * Create first handler for the 
	 * NoSuchElementException. Spring will pass in the exception that is thrown. 
	 * return buildExceptionMessage(ex, (then the message that we want) 
	 * HttpStatus.NOT_FOUND, webRequest, logStatus.MESSAGE_ONLY)*/
	
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ExceptionMessage handleNoSuchElementException(
			NoSuchElementException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.NOT_FOUND, webRequest, 			LogStatus.MESSAGE_ONLY);
	}
	
	/*handles duplicate emails so that not two soakers can have the same email.*/
	
	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ExceptionMessage handleDuplicateKeyException(
			DuplicateKeyException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.CONFLICT, 
				webRequest, LogStatus.MESSAGE_ONLY);
	}
	
	
	private ExceptionMessage buildExceptionMessage(Exception ex, 
			HttpStatus status, WebRequest webRequest, LogStatus logStatus) {
		
		/*Create variables to hold the different values that we want.*/
		
		String message = ex.toString();
		String statusReason = status.getReasonPhrase();
		int statusCode = status.value();
		String uri = null;
		String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		
		if(webRequest instanceof ServletWebRequest swr) {
			uri = swr.getRequest().getRequestURI();
		}
		
		if(logStatus == LogStatus.MESSAGE_ONLY) {
			log.error("Exception: {}", ex.toString());
		}
		else {
			log.error("Exception: ", ex);
		}
		
		/*Create ExceptionMessage object so it can be returned.*/
		
		ExceptionMessage excMsg = new ExceptionMessage();
		
		/*Fill out the ExceptionMessage object with the variables:*/
		
		excMsg.setMessage(message);
		excMsg.setStatusCode(statusCode);
		excMsg.setStatusReason(statusReason);
		excMsg.setTimestamp(timestamp);
		excMsg.setUri(uri);
		
		return excMsg;
	}
}
