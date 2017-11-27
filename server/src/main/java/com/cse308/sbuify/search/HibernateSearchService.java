package com.cse308.sbuify.search;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.exception.EmptyQueryException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

@Service
public class HibernateSearchService {

    public final static String TERM_SEPARATOR = "$#!";
    public final static String FILTER_SEPARATOR = ":";

    @Autowired
    private AuthFacade authFacade;

    private FullTextEntityManager entityManager;

    @Autowired
    public HibernateSearchService(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        entityManager = Search.getFullTextEntityManager(em);
        initializeIndex();
    }

    /**
     * Initialize the Lucene search index.
     */
    private void initializeIndex() {
        try {
            entityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Failed to initialize Hibernate Search.");
            e.printStackTrace();
        }
    }

    /**
     * Get a list of entities matching the given query criteria.
     *
     * @param query Search query, including keywords & optional filters.
     * @param clazz Entity class.
     * @param limit Maximum number of results to return.
     * @param offset Offset to first result.
     */
    @Transactional
    public <T> List<T> search(String query, Class<T> clazz, Integer limit, Integer offset) {
        QueryBuilder qb = entityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();

        // parse the query string into search terms
        String[] terms = query.split(TERM_SEPARATOR);

        // construct a query to find all results matching the search keyword (first term)
        Query keywordQuery;
        try {
             keywordQuery = qb
                    .keyword()
                    .fuzzy()
                    .withPrefixLength(1)
                    .onFields("name")
                    .matching(terms[0])
                    .createQuery();
        } catch (EmptyQueryException nre) {
            return Collections.emptyList(); // invalid query -- return empty resultset
        }

        // add a boolean AND query for each optional filter (terms 2..N)
        BooleanJunction<?> junction = qb
                .bool()
                    .must(keywordQuery);

        for (int i = 1; i < terms.length; i++) {
            String[] filter = terms[i].split(FILTER_SEPARATOR);  // pieces[0] is field name, pieces[1] is value
            Query filterQuery = qb
                    .keyword()
                    .onField(filter[0])
                    .matching(filter[1])
                    .createQuery();
            junction.must(filterQuery);
        }

        // filter out playlists the user shouldn't see
        User user = authFacade.getCurrentUser();

        if (clazz.equals(Playlist.class) && !(user instanceof Admin)) {
            Query playlistFilter = qb
                    .bool()
                        .should(qb
                                .keyword()
                                .onField("hidden")
                                .matching("false")
                                .createQuery())
                        .should(qb
                                .keyword()
                                .onField("owner.id")
                                .matching(Integer.toString(user.getId()))
                                .createQuery())
                    .createQuery();
            junction.must(playlistFilter);
        }

        // build & wrap final query
        FullTextQuery jpaQuery = entityManager.createFullTextQuery(junction.createQuery(), clazz);

        jpaQuery.setFirstResult(offset);
        jpaQuery.setMaxResults(limit);

        // execute search
        List<T> result = null;
        try {
            result = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            // ignore
        }
        return result;
    }
}
