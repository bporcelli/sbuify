package com.cse308.sbuify.search;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

@Service
public class HibernateSearchService {

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
     * Search for entities matching the given query criteria.
     *
     * @param keyword Search keyword.
     * @param clazz Entity class.
     */
    @Transactional
    public <T> List<T> fuzzySearch(String keyword, Class<T> clazz) {
        return fuzzySearch(keyword, null, clazz);
    }

    /**
     * Search for entities matching the given query criteria.
     *
     * @param keyword Search keyword.
     * @param owned Boolean indicating whether only owned entities should be returned.
     * @param clazz Entity class.
     */
    public <T> List<T> fuzzySearch(String keyword, Boolean owned, Class<T> clazz) {
        // build Lucene query
        QueryBuilder qb = entityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();

        Query query = qb
                .keyword()
                .fuzzy()
                .withPrefixLength(1)
                .onFields("name")
                .matching(keyword)
                .createQuery();

        // wrap Lucene query in persistenceQuery
        FullTextQuery persistenceQuery = entityManager.createFullTextQuery(query, clazz);

        // todo: filter out hidden playlists unless they are owned by the current user
        if (owned != null) {  // filter entities by ownership
            persistenceQuery.enableFullTextFilter("ownerFilter").setParameter("owned", owned);
        }

        // execute search
        List<T> result = null;
        try {
            result = persistenceQuery.getResultList();
        } catch (NoResultException nre) {
            // todo: log
        }
        return result;
    }
}
