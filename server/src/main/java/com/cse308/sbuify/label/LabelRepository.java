package com.cse308.sbuify.label;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LabelRepository extends CrudRepository<Label, Integer> {

    @Query("FROM Label rl where LOWER(rl.name) = LOWER(:keyword) AND r1.owner == NULL")
    Iterable<Label> findByKeyword(@Param("keyword") String keyword);
}
