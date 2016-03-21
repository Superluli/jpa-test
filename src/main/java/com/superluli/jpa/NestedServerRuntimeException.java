package com.superluli.jpa;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;

public class NestedServerRuntimeException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1056692694757017462L;

	private HttpStatus status;

	public NestedServerRuntimeException(HttpStatus status, String message) {
		
		super(message);
		this.status = status;
	}

	public NestedServerRuntimeException(HttpStatus status, String message,
			Throwable cause) {
		
		super(message, cause);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
