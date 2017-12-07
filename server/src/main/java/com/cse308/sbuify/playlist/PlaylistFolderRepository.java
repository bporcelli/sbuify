package com.cse308.sbuify.playlist;

import com.cse308.sbuify.customer.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaylistFolderRepository extends CrudRepository<PlaylistFolder, Integer> {
    List<PlaylistFolder> findAllByOwner_Id(Integer ownerId);
    List<PlaylistFolder> findByOwner(Customer customer);
}
