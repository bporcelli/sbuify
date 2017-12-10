package com.cse308.sbuify.customer;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FollowedArtistRepository extends CrudRepository<FollowedArtist, Integer> {
    Optional<FollowedArtist> findByCustomerAndArtist_Id(User customer, Integer artistId);

    @Modifying
    void deleteByCustomerAndArtist_Id(User customer, Integer artistId);

    boolean existsByCustomerAndArtist_Id(User customer, Integer artistId);

    List<FollowedArtist> findAllByCustomer(User customer);

    Integer countByCustomerAndArtist(User customer, Artist artist);
}
