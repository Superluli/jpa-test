package com.superluli.jpa;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("com.superluli")
public class AppServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NestedServerRuntimeException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request,
			NestedServerRuntimeException ex) {

		System.err.println(ex.getMessage());

		ErrorView errorView = new ErrorView(ex.getMessage());
		return new ResponseEntity<ErrorView>(errorView, ex.getStatus());
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request,
			Throwable ex) {

		System.err.println(ex.getMessage());

		ErrorView errorView = new ErrorView(ex.getMessage());
		return new ResponseEntity<ErrorView>(errorView,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static class ErrorView {

		String message;

		public ErrorView() {

		}

		ErrorView(String message) {

			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
