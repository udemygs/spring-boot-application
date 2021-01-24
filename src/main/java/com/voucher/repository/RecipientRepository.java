package com.voucher.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voucher.entity.Recipient;

public interface RecipientRepository extends JpaRepository<Recipient, String> {

}
