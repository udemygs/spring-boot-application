package com.voucher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return new ResponseEntity<>("message taken from the exception" + ex, HttpStatus.OK);
	}

	@ExceptionHandler(VoucherNotFound.class)
	public ResponseEntity<Object> handleVoucherNotFoundException(VoucherNotFound ex, WebRequest request) {
		return new ResponseEntity<>("Voucher Code : " + ex.getCode() + " not found!", HttpStatus.OK);
	}

	@ExceptionHandler(VoucherExpired.class)
	public ResponseEntity<Object> handleVoucherExpiredException(VoucherExpired ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(RecipientNotFound.class)
	public ResponseEntity<Object> handleRecipientNotFoundException(RecipientNotFound ex, WebRequest request) {
		return new ResponseEntity<>("Recipient : " + ex.getEmail() + " not found!", HttpStatus.OK);
	}
}
