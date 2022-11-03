package com.camunda.consulting.two_ds.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class TwoDatasourcesConfiguration {
  
  private static final Logger LOG = LoggerFactory.getLogger(TwoDatasourcesConfiguration.class);

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "customer.datasource")
	public DataSourceProperties customerDataSourceProperties() {
    return new DataSourceProperties();
	}

  @Bean
  @Primary
  @ConfigurationProperties("customer.datasource.configuration")
  public HikariDataSource customerDataSource(DataSourceProperties properties) {
    LOG.info("create customer datasource");
    return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }
  
  @Bean
  @ConfigurationProperties(prefix = "camunda-bpm.datasource")
  public DataSourceProperties camundaDataSourceProperties() {
    return new DataSourceProperties();
  }
  
  @Bean("camundaBpmDataSource")
  @ConfigurationProperties(prefix = "camunda-bpm.datasource.configuration")
  public HikariDataSource camundaDataSource(@Qualifier("camundaDataSourceProperties") DataSourceProperties properties) {	  
    LOG.info("create camunda datasource");
    return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }
  
}

/*

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.sample.workflow.dao", entityManagerFactoryRef = "epDataEntityManagerFactory", transactionManagerRef = "transactionManager")

  @Primary
  @Bean("epDataEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean epDataEntityManagerFactory() {
    LOG.info("Create ep Entity manager");
    
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(epDataSource());
    em.setPackagesToScan(new String[] { "com.sample.workflow.entities" });
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.H2);
    vendorAdapter.setGenerateDdl(false);
    em.setJpaVendorAdapter(vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "create-drop");
    properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Primary
  @Bean("transactionManager")
  public PlatformTransactionManager transactionManager(final @Qualifier("epDataEntityManagerFactory") LocalContainerEntityManagerFactoryBean epDataEntityManagerFactory) {
    
    LOG.info("create ep data transaction manager");
    
    return new JpaTransactionManager(epDataEntityManagerFactory.getObject());
  }

  @Bean("camundaBpmTransactionManager")
  public PlatformTransactionManager camundaBpmTransactionManager(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
    
    LOG.info("Create Camunda transaction manager");
    return new DataSourceTransactionManager(dataSource);
  }

*/


