package com.voucher.exception;

import com.voucher.entity.VoucherCode;

public class VoucherExpired extends RuntimeException {

	public VoucherExpired(VoucherCode voucher) {
		super("Voucher " + voucher.getCode() + " is expired or is redeemd!");
	}
}
