package com.nagarro.javatest.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestAPIExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = e.getParameterName() + " parameter is missing";
		return buildResponseEntity(new GenericException(BAD_REQUEST, error));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(e.getContentType());
		builder.append("Type is not supported, supported are");
		e.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(
				new GenericException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2)));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Error writing JSON";
		return buildResponseEntity(new GenericException(HttpStatus.INTERNAL_SERVER_ERROR, error));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		GenericException error = new GenericException(BAD_REQUEST);
		error.setMessage(
				String.format("Could not find the %s method for URL %s", e.getHttpMethod(), e.getRequestURL()));
		return buildResponseEntity(error);
	}
	
	@ExceptionHandler(JwtException.class)
	protected ResponseEntity<Object> handleTokenViolation() {
		GenericException error = new GenericException(UNAUTHORIZED);
		return buildResponseEntity(error);
	}

	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException e) {
		GenericException error = new GenericException(NOT_FOUND);
		error.setMessage(String.format(e.getMessage()));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(ConcurrentLoginException.class)
	protected ResponseEntity<Object> handleConcurrentLoginException(ConcurrentLoginException e) {
		GenericException error = new GenericException(BAD_REQUEST);
		error.setMessage(String.format(e.getMessage()));
		return buildResponseEntity(error);
	}
	
	@ExceptionHandler(InvalidParameterRangeException.class)
	protected ResponseEntity<Object> handleInvalidAmountRangeException(InvalidParameterRangeException e) {
		GenericException error = new GenericException(BAD_REQUEST);
		error.setMessage(String.format(e.getMessage()));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(MissingParameterException.class)
	protected ResponseEntity<Object> handleMissingParamException(MissingParameterException e) {
		GenericException error = new GenericException(BAD_REQUEST);
		error.setMessage(String.format(e.getMessage()));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
		GenericException error = new GenericException(BAD_REQUEST);
		error.setMessage(String.format("Can not convert parameter '%s' of value '%s' to type '%s'",
				e.getName(), e.getValue(), Objects.requireNonNull(e.getRequiredType()).getSimpleName()));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(MethodNotAllowedException.class)
	protected ResponseEntity<Object> handleMethodNotAllowedException(MethodNotAllowedException e) {
		GenericException error = new GenericException(METHOD_NOT_ALLOWED);
		error.setMessage(String.format(Objects.requireNonNull(e.getMessage())));
		return buildResponseEntity(error);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(AccessDeniedException e) {
		GenericException error = new GenericException(UNAUTHORIZED);
		error.setMessage(e.getMessage());
		return buildResponseEntity(error);
	}

	private ResponseEntity<Object> buildResponseEntity(GenericException error) {
		return new ResponseEntity<>(error, error.getStatus());
	}

}
