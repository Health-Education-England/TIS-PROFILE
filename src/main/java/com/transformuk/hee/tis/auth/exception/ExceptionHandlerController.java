package com.transformuk.hee.tis.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionHandlerController {

	/**
	 * Handles an exception and returns {@link ResponseEntity} with errorMessage
	 * @param ex Exception to be handled
	 * @return {@link ResponseEntity}
	 * @throws IOException
	 */
	@ExceptionHandler(value = {Exception.class, RuntimeException.class})
	public ResponseEntity<Map<String,Object>> handleException(Exception ex) throws IOException {
		HttpStatus status = INTERNAL_SERVER_ERROR;
		Map<String,Object> errorMap = new HashMap<>();
		if(ex instanceof IllegalArgumentException || ex instanceof MethodArgumentNotValidException) {
			status = BAD_REQUEST;
		}
		if(ex instanceof UserNotFoundException) {
			status = NOT_FOUND;
		}
		ex.printStackTrace();
		errorMap.put("errorMessage", ex.getMessage());
		return new ResponseEntity<>(errorMap, status);
	}
}
