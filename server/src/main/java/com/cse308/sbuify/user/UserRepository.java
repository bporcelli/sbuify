package com.cse308.sbuify.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
	Optional<User> findByToken(String token);
    Page<User> findAll(Pageable pageable);
}
