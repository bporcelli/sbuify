//package com.cse308.sbuify.web;
//
//import javax.persistence.EntityManager;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HibernateSearchConfiguration {
//	private final Logger logger = LoggerFactory.getLogger(HibernateSearchConfiguration.class);
//
//	@Autowired
//	private EntityManager entityManager;
//
//	@Bean
//	HibernateSearchService hibernateSearchService() {
//		HibernateSearchService hibernateSearchService = new HibernateSearchService(entityManager);
//		hibernateSearchService.initializeHibernateSearch();
//		return hibernateSearchService;
//	}
//}