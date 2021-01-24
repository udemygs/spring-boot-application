package com.voucher.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "RECIPIENT", uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
@JsonInclude(value = Include.NON_NULL)
public class Recipient implements Serializable {

	private static final long serialVersionUID = -2047498844420896268L;

	@Id
	private String email;

	private String name;

	@OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
	private Set<VoucherCode> voucherCodes = new HashSet<>();

	public void addVoucher(VoucherCode voucher) {
		voucher.setRecipient(this);
		voucherCodes.add(voucher);
	}

	public Recipient() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<VoucherCode> getVoucherCodes() {
		if (CollectionUtils.isEmpty(voucherCodes)) {
			voucherCodes = new HashSet<>();
		}
		return voucherCodes;
	}

	public void setVoucherCodes(Set<VoucherCode> voucherCodes) {
		this.voucherCodes = voucherCodes;
	}
}
