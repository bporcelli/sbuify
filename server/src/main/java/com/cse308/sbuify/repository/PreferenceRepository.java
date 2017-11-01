package com.cse308.sbuify.repository;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.domain.Admin;

public interface PreferenceRepository extends CrudRepository<Admin, Integer> {
}
