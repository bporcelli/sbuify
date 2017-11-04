package com.cse308.sbuify.admin;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.admin.Admin;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
	Admin findByEmail(String email);
}
