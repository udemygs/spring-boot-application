package com.voucher.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voucher.entity.Recipient;
import com.voucher.entity.SpecialOffer;
import com.voucher.entity.VoucherCode;
import com.voucher.exception.RecipientNotFound;
import com.voucher.exception.VoucherExpired;
import com.voucher.exception.VoucherNotFound;
import com.voucher.repository.RecipientRepository;
import com.voucher.repository.VoucherCodeRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1")
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Collection of Voucher that can be used by Customer.")
public class VoucherController {

	private VoucherCodeRepository voucherCodeRepo;

	private RecipientRepository recipientRepo;

	@Autowired
	public VoucherController(VoucherCodeRepository voucherCodeRepo, RecipientRepository recipientRepo) {
		this.voucherCodeRepo = voucherCodeRepo;
		this.recipientRepo = recipientRepo;
	}

	@PostMapping(value = "/recipients/vouchers")
	@ApiOperation(value = "Generate Voucher for each Recipient")
	public void createVoucher(@RequestBody SpecialOffer specialOffer,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate) {

		final SpecialOffer offer = new SpecialOffer(specialOffer.getName(), specialOffer.getDiscount());

		for (Recipient r : recipientRepo.findAll()) {
			VoucherCode voucher = new VoucherCode();
			voucher.setExpiryDate(expiryDate);
			voucher.setCreatedDateTime(LocalDateTime.now());
			voucher.setSpecialOffer(offer);

			r.addVoucher(voucher);
			recipientRepo.save(r);
		}
	}

	@PutMapping(value = "/recipients/{email}/vouchers/{code}")
	@ApiOperation(value = "Redeem Voucher by code and email", response = ResponseEntity.class)
	public ResponseEntity<Recipient> update(@PathVariable String email, @PathVariable String code) {
		Optional<Recipient> found = recipientRepo.findById(email).filter(p -> p.getEmail().equalsIgnoreCase(email));
		if (found.isPresent()) {
			Recipient recipient = found.get();
			VoucherCode voucher = recipient.getVoucherCodes().stream().filter(item -> item.getCode().equals(code))
					.findAny().orElse(null);

			// validations
			validateVoucher(voucher, code);

			recipient.getVoucherCodes().clear();

			voucher.setModifiedDateTime(LocalDateTime.now());
			voucher.setUsed(Boolean.TRUE);
			voucher.setUsedDate(LocalDate.now());
			voucherCodeRepo.save(voucher);

			recipient.addVoucher(voucher);
			return new ResponseEntity<Recipient>(recipient, HttpStatus.OK);
		} else {
			throw new VoucherNotFound(code);
		}
	}

	private void validateVoucher(VoucherCode voucher, String code) {
		LocalDate today = LocalDate.now();
		if (voucher == null) {
			throw new VoucherNotFound(code);
		}
		if (voucher.getExpiryDate().isBefore(today)) {
			throw new VoucherExpired(voucher);
		}
		if (voucher.isUsed()) {
			throw new VoucherExpired(voucher);
		}
	}

	@GetMapping(value = "/recipients/{email}/vouchers")
	@ApiOperation(value = "Retrieve all valid Vouchers with specified email", response = Recipient.class)
	public ResponseEntity<Recipient> getAllValidVouchersByEmail(@PathVariable String email) {
		LocalDate today = LocalDate.now();

		Optional<Recipient> found = recipientRepo.findById(email);

		if (found.isPresent()) {
			Recipient recipient = found.get();
			Set<VoucherCode> notValidList = new HashSet<>();
			recipient.getVoucherCodes().stream().filter(item -> item.isUsed() || item.getExpiryDate().isBefore(today))
					.forEach(r -> notValidList.add(r));
			recipient.getVoucherCodes().removeAll(notValidList);
			return new ResponseEntity<Recipient>(recipient, HttpStatus.OK);
		} else {
			throw new RecipientNotFound(email);
		}
	}

	@GetMapping(value = "/recipients")
	@ApiOperation(value = "View all Recipients")
	public ResponseEntity<List<Recipient>> findAll() {
		List<Recipient> recipientList = new ArrayList<>();
		recipientRepo.findAll().forEach(recipient -> recipientList.add(recipient));
		return new ResponseEntity<List<Recipient>>(recipientList, HttpStatus.OK);
	}
}
