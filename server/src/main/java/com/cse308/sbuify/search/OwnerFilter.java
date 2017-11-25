package com.cse308.sbuify.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;

/**
 * Filter used to search for entities with a null or non-null owner.
 */
public class OwnerFilter {

    private Boolean owned;

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    @Factory
    public Query create() {
        String ownerValue = owned ? "!null" : "null";
        return new TermQuery(new Term("owner", ownerValue));
    }
}
