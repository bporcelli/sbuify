package com.cse308.sbuify.repository;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.domain.User;



public interface UserRepository extends CrudRepository<User, Integer> {

}
