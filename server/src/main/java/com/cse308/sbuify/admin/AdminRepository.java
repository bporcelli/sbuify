package com.cse308.sbuify.admin;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
	Admin findByEmail(String email);
}
