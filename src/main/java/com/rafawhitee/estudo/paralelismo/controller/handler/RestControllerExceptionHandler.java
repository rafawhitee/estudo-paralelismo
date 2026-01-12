package com.rafawhitee.estudo.paralelismo.controller.handler;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.rafawhitee.estudo.paralelismo.controller.handler.model.StandardErrorDTO;
import com.rafawhitee.estudo.paralelismo.controller.handler.model.ValidationErrorDTO;

@RestControllerAdvice
public class RestControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerExceptionHandler.class);
	public static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardErrorDTO> handleGenericException(Exception exception, WebRequest request) {
		LOGGER.error("handleGenericException: ", exception);
		return ResponseEntity.status(DEFAULT_STATUS).contentType(MediaType.APPLICATION_JSON)
				.body(StandardErrorDTO.builder().status(DEFAULT_STATUS.value())
						.message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Object handleNoHandlerFoundException(NoHandlerFoundException exception, WebRequest request) {
		LOGGER.debug("handleNoHandlerFoundException: ", exception);
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(StandardErrorDTO.builder()
				.status(status.value()).message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public Object handleNoResourceFoundException(NoResourceFoundException exception, WebRequest request) {
		LOGGER.debug("handleNoResourceFoundException: ", exception);
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(StandardErrorDTO.builder()
				.status(status.value()).message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardErrorDTO> handleIllegalArgumentException(IllegalArgumentException exception,
			WebRequest request) {
		LOGGER.error("handleIllegalArgumentException: ", exception);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(StandardErrorDTO.builder()
				.status(status.value()).message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<StandardErrorDTO> handleFileNotFoundException(FileNotFoundException exception,
			WebRequest request) {
		LOGGER.error("handleFileNotFoundException: ", exception);
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(StandardErrorDTO.builder()
				.status(status.value()).message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardErrorDTO> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException exception, WebRequest request) {
		LOGGER.error("handleHttpMessageNotReadableException: ", exception);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON)
				.body(StandardErrorDTO.builder().status(status.value()).error(status.getReasonPhrase())
						.timestamp(LocalDateTime.now()).message(this.getExceptionMessage(exception, request)).build());
	}


	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<StandardErrorDTO> handleAccessDeniedExceptionException(AccessDeniedException exception,
			WebRequest request) {
		LOGGER.debug("handleAccessDeniedExceptionException: ", exception);
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON)
				.body(StandardErrorDTO.builder().status(status.value()).error(status.getReasonPhrase())
						.timestamp(LocalDateTime.now()).message(this.getExceptionMessage(exception, request)).build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorDTO> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception, WebRequest request) {
		LOGGER.debug("handleMethodArgumentNotValidException: ", exception);
		ValidationErrorDTO validationError = this.createValidationError(exception.getBindingResult());
		return ResponseEntity.status(validationError.getStatus()).contentType(MediaType.APPLICATION_JSON)
				.body(validationError);
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<ValidationErrorDTO> handleWebExchangeBindException(WebExchangeBindException exception,
			WebRequest request) {
		LOGGER.debug("handleWebExchangeBindException: ", exception);
		ValidationErrorDTO validationError = this.createValidationError(exception.getBindingResult());
		return ResponseEntity.status(validationError.getStatus()).contentType(MediaType.APPLICATION_JSON)
				.body(validationError);
	}

	/**
	 * 
	 * PRIVATE's
	 *
	 **/

	private String getExceptionMessage(Exception ex, WebRequest request) {
		return ex.getMessage();
	}

	private ValidationErrorDTO createValidationError(BindingResult bindingResult) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ValidationErrorDTO validationError = ValidationErrorDTO.builder().status(status.value())
				.timestamp(LocalDateTime.now()).error(status.getReasonPhrase()).build();
		if (bindingResult != null && CollectionUtils.isNotEmpty(bindingResult.getFieldErrors())) {
			bindingResult.getFieldErrors()
					.forEach(fe -> validationError.addError(fe.getField(), fe.getDefaultMessage()));
		}
		return validationError;
	}

}