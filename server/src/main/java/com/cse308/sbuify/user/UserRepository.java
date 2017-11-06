package com.cse308.sbuify.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Integer> {
	Optional<AppUser> findByEmail(String email);
}
