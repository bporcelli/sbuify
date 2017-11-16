package com.cse308.sbuify.label;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RecordLabelRepository extends CrudRepository<RecordLabel, Integer> {

    @Query("FROM RecordLabel rl where LOWER(rl.label.name) = LOWER(:keyword)")
    Iterable<RecordLabel> findByKeyword(@Param("keyword") String keyword);
}
