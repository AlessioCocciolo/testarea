package org.podcastpedia.batch.common.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class InfrastructureConfiguration {
	
	@Autowired
	Environment env;
	 
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	  LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	  em.setDataSource(dataSource());
	  em.setPackagesToScan(new String[] { "org.podcastpedia.batch.*" });

	  JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	  em.setJpaVendorAdapter(vendorAdapter);
	  em.setJpaProperties(additionalJpaProperties());

	  return em;
	}

	Properties additionalJpaProperties() {
		  Properties properties = new Properties();
		  //properties.setProperty("hibernate.hbm2ddl.auto", "create");
		  properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		  properties.setProperty("hibernate.show_sql", "true");
		  
		  return properties;
	}

	@Bean
	public DataSource dataSource(){	   
	   return DataSourceBuilder.create()
				.url(env.getProperty("db.url"))
				.driverClassName(env.getProperty("db.driver"))
				.username(env.getProperty("db.username"))
				.password(env.getProperty("db.password"))
				.build();		   
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
	  JpaTransactionManager transactionManager = new JpaTransactionManager();
	  transactionManager.setEntityManagerFactory(emf);

	  return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	  return new PersistenceExceptionTranslationPostProcessor();
	}	
 
}
