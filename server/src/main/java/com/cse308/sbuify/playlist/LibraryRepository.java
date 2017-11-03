package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.Customer;
import org.springframework.data.repository.CrudRepository;

public interface LibraryRepository extends CrudRepository<Library, Integer> {
    Library findByCustomer(Customer customer);
}
