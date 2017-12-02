package com.cse308.sbuify.customer.preferences;

import com.cse308.sbuify.customer.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreferenceRepository extends CrudRepository<Preference, Preference.PreferencePK> {

    @Query("SELECT p FROM Preference p WHERE p.customer = :c AND p.key = :p_key")
    Preference get(@Param("c") Customer customer, @Param("p_key") String key);

    @Query("UPDATE Preference p SET p.value = :val WHERE p.customer = :c AND p.key = :p_key")
    Void set(@Param("c") Customer customer, @Param("p_key") String key, @Param("val") String value);
}
