package com.transformuk.hee.tis.profile.web.rest.errors;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlerController {

	private static final Logger LOG = getLogger(ExceptionHandlerController.class);

	/**
	 * Handles an exception and returns {@link ResponseEntity} with errorMessage
	 *
	 * @param ex Exception to be handled
	 * @return {@link ResponseEntity}
	 * @throws IOException
	 */
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	public ResponseEntity<Map<String, Object>> handleException(Exception ex) throws IOException {
		HttpStatus status = INTERNAL_SERVER_ERROR;
		Map<String, Object> errorMap = new HashMap<>();
		if (ex instanceof IllegalArgumentException || ex instanceof MethodArgumentNotValidException) {
			status = BAD_REQUEST;
		}
		if (ex instanceof EntityNotFoundException) {
			status = NOT_FOUND;
		}
		LOG.error("Error occurred processing the request", ex);
		errorMap.put("errorMessage", ex.getMessage());
		return new ResponseEntity<>(errorMap, status);
	}
}
