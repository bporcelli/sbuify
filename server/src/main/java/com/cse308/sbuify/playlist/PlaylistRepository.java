package com.cse308.sbuify.playlist;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
    List<Playlist> findAllByOwner_Id(Integer ownerId);

    @Query(
            value = "SELECT p.* " +
                    "FROM playlist p, stream st " +
                    "WHERE p.id = st.playlist_id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY p.id " +
                    "ORDER BY MAX(st.time) DESC\n -- #pageable\n",
            countQuery = "SELECT COUNT(p.id) " +
                    "FROM playlist p, stream st " +
                    "WHERE p.id = st.playlist_id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY p.id",
            nativeQuery = true
    )
    Page<Playlist> getRecentlyPlayedByCustomer(@Param("customerId") Integer customerId, Pageable pageable);
}
