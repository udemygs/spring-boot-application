package com.voucher.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voucher.entity.SpecialOffer;

@SpringBootTest
@AutoConfigureMockMvc
public class VoucherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testCreateVoucher() throws Exception {
		mockMvc.perform(post("/v1/recipients/vouchers", 1).queryParam("expiryDate", "2021-06-31")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new SpecialOffer("2.2 Shopee CNY", 80))))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdate() throws Exception {
		mockMvc.perform(put("/v1/recipients/{email}/vouchers/{code}", "ali@gmail.com", "123456789012")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllValidVouchersByEmail() throws Exception {
		mockMvc.perform(get("/v1/recipients/{email}/vouchers", "ali@gmail.com")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.email", is("ali@gmail.com"))).andExpect(jsonPath("$.name", is("Ali")));

	}

	@Test
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/v1/recipients")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", Matchers.hasSize(3))).andExpect(jsonPath("$[0].email", is("ali@gmail.com")))
				.andExpect(jsonPath("$[0].name", is("Ali"))).andExpect(jsonPath("$[1].email", is("jack@gmail.com")))
				.andExpect(jsonPath("$[1].name", is("Jack"))).andExpect(jsonPath("$[2].email", is("king@gmail.com")))
				.andExpect(jsonPath("$[2].name", is("King")));
	}
}
