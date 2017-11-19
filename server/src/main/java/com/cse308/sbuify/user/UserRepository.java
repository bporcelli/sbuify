package com.cse308.sbuify.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
	Optional<User> findByToken(String token);
}
