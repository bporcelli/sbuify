package com.sbuify.SBUify.repository;

import com.sbuify.SBUify.domain.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {

}
