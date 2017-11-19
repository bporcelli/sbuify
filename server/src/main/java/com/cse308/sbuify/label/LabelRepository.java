package com.cse308.sbuify.label;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LabelRepository extends CrudRepository<Label, Integer> {

    @Query("SELECT l FROM Label l WHERE LOWER(l.name) = LOWER(:keyword)")
    Iterable<Label> findByKeyword(@Param("keyword") String keyword);
}
