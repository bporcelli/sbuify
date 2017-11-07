//package com.cse308.sbuify.web;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//
//import org.apache.lucene.search.Query;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.Search;
//import org.hibernate.search.query.dsl.QueryBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.cse308.sbuify.user.User;
// 
//  
//@Service
//public class HibernateSearchService  { 
//    private final EntityManager entityManager;
// 
//    @Autowired
//    public HibernateSearchService(EntityManager entityManager) {
//        super();
//        this.entityManager = entityManager;
//    }
// 
// 
//    public void initializeHibernateSearch() {
// 
//        try {
//            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//            fullTextEntityManager.createIndexer().startAndWait();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//    
//    @Transactional
//    public List<User> fuzzySearch(String searchTerm){
// 
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
//        Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1).onFields("firstname", "lastname")
//                .matching(searchTerm).createQuery();
// 
//        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, User.class);
// 
//        // execute search
// 
//        List<User> employeeList = null;
//        try {
//            employeeList  = jpaQuery.getResultList();
//        } catch (NoResultException nre) {
// 
//        }
// 
//        return employeeList;
// 
//    
//    }
//}
