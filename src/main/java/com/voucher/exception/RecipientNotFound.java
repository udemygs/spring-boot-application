package com.voucher.exception;

public class RecipientNotFound extends RuntimeException {

	private String email;

	public RecipientNotFound(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
