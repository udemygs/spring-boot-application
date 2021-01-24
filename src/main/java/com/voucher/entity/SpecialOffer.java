package com.voucher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SPECIALOFFER")
public class SpecialOffer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "specialOfferId")
	private Long specialOfferId;

	private String name;

	private double discount;

	public SpecialOffer() {
		super();
	}

	public SpecialOffer(String name, double discount) {
		this.name = name;
		this.discount = discount;
	}

	public Long getSpecialOfferId() {
		return specialOfferId;
	}

	public void setSpecialOfferId(Long specialOfferId) {
		this.specialOfferId = specialOfferId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

}
