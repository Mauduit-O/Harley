package com.DharleyEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DharleyEcommerce.model.UserM;

public interface UserRepository extends JpaRepository<UserM, Long>{
	UserM findByEmail(String email);
}
