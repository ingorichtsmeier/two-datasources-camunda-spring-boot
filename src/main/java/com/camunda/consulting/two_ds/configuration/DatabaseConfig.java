package com.camunda.consulting.two_ds.configuration;

import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class DatabaseConfig {

    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    @Autowired
    private Environment env;

    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.msdatasource.enabled", havingValue = "true", matchIfMissing = false)
    public DataSource primaryDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.msdatasource.driver.class"));
        dataSource.setJdbcUrl(env.getProperty("spring.msdatasource.url"));
        dataSource.setUsername(env.getProperty("spring.msdatasource.username"));
        dataSource.setPassword(env.getProperty("spring.msdatasource.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.msdatasource.hikari.maximumPoolSize")));
        dataSource.setMinimumIdle(Integer.parseInt(env.getProperty("spring.msdatasource.hikari.minimumIdle")));

        dataSource.addDataSourceProperty("cachePrepStmts",
                Boolean.valueOf(env.getProperty("spring.msdatasource.hikari.cachePrepStmts")));
        dataSource.addDataSourceProperty("prepStmtCacheSize",
                Integer.parseInt(env.getProperty("spring.msdatasource.hikari.prepStmtCacheSize")));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit",
                Integer.parseInt(env.getProperty("spring.msdatasource.hikari.prepStmtCacheSqlLimit")));
        dataSource.addDataSourceProperty("useServerPrepStmts",
                Boolean.valueOf(env.getProperty("spring.msdatasource.hikari.useServerPrepStmts")));

        logger.info("msdatasource loaded successfully and JdbcUrl = " + dataSource.getJdbcUrl());
        return dataSource;
    }

    @Bean(name = "camundaBpmDataSource")
    @ConditionalOnProperty(name = "spring.camundadatasource.enabled", havingValue = "true", matchIfMissing = false)
    public DataSource secondaryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.camundadatasource.driver.class"));
        dataSource.setJdbcUrl(env.getProperty("spring.camundadatasource.url"));
        dataSource.setUsername(env.getProperty("spring.camundadatasource.username"));
        dataSource.setPassword(env.getProperty("spring.camundadatasource.password"));

        dataSource.setMaximumPoolSize(
                Integer.parseInt(env.getProperty("spring.camundadatasource.hikari.maximumPoolSize")));
        dataSource.setMinimumIdle(Integer.parseInt(env.getProperty("spring.camundadatasource.hikari.minimumIdle")));

        dataSource.addDataSourceProperty("cachePrepStmts",
                Boolean.valueOf(env.getProperty("spring.camundadatasource.hikari.cachePrepStmts")));
        dataSource.addDataSourceProperty("prepStmtCacheSize",
                Integer.parseInt(env.getProperty("spring.camundadatasource.hikari.prepStmtCacheSize")));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit",
                Integer.parseInt(env.getProperty("spring.camundadatasource.hikari.prepStmtCacheSqlLimit")));
        dataSource.addDataSourceProperty("useServerPrepStmts",
                Boolean.valueOf(env.getProperty("spring.camundadatasource.hikari.useServerPrepStmts")));

        logger.info("camundadatasource loaded successfully and JdbcUrl = " + dataSource.getJdbcUrl());
        return dataSource;
    }


    /*
     * Without defining name transactionManager we get following error message: The process could not be started. : No
     * bean named 'transactionManager' available: No matching TransactionManager bean found for qualifier
     * 'transactionManager' - neither qualifier match nor bean name match!
     */
    @Bean(name = "transactionManager")
    @Primary
    @ConditionalOnProperty(name = "spring.msdatasource.enabled", havingValue = "true", matchIfMissing = false)
    public PlatformTransactionManager primaryTransactionManager() {
        logger.info("primary transaction manager configured");
        return new JpaTransactionManager();
    }
    
    @Bean(name = "camundaBpmTransactionManager")
    @ConditionalOnProperty(name = "spring.camundadatasource.enabled", havingValue = "true", matchIfMissing = false)
    public PlatformTransactionManager camundaTransactionManager(
            @Qualifier("camundaBpmDataSource") DataSource dataSource) {
        logger.info("camundaBpmTransactionManager transaction manager configured");
        return new DataSourceTransactionManager(dataSource);
    }


}