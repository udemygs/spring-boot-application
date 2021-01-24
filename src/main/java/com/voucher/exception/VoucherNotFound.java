package com.voucher.exception;

public class VoucherNotFound extends RuntimeException {

	private String code;

	public VoucherNotFound(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
