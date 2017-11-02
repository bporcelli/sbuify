package com.cse308.sbuify.user;

import java.util.Optional;

import com.cse308.sbuify.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}
