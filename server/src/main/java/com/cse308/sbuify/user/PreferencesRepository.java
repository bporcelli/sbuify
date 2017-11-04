package com.cse308.sbuify.user;

import com.cse308.sbuify.customer.Preferences;
import org.springframework.data.repository.CrudRepository;

public interface PreferencesRepository extends CrudRepository<Preferences, Integer> {
}
