package com.cse308.sbuify.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
