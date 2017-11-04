package com.cse308.sbuify.customer;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.admin.Admin;

public interface PreferenceRepository extends CrudRepository<Admin, Integer> {
}
