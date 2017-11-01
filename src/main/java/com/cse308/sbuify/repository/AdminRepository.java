package com.cse308.sbuify.repository;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.domain.Admin;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
	Admin findByEmail(String email);
}
