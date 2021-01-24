package com.voucher;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.voucher.controller.VoucherController;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private VoucherController controller;

	@Test
	void contextLoads() {
		assertNotNull(controller);
	}

}
