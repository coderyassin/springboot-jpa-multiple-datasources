package com.example.demo.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo.repository.oracle",
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager"
)
public class OracleDBConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix="datasource.oracle")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("oracleDataSource")
    public DataSource oracleDataSource() {
        DataSourceProperties primaryDataSourceProperties = oracleDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(primaryDataSourceProperties.getDriverClassName())
                .url(primaryDataSourceProperties.getUrl())
                .username(primaryDataSourceProperties.getUsername())
                .password(primaryDataSourceProperties.getPassword())
                .build();
    }

    @Bean
    public PlatformTransactionManager oracleTransactionManager()
    {
        EntityManagerFactory factory = oracleEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(oracleDataSource());
        factory.setPackagesToScan(new String[]{"com.example.demo.entity.oracle"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("jpa.show-sql"));
        jpaProperties.put("properties.hibernate.dialect", env.getProperty("jpa.oracle.database-platform"));
        jpaProperties.put("hibernate.connection.url", env.getProperty("datasource.oracle.url"));
        jpaProperties.put("hibernate.connection.username", env.getProperty("datasource.oracle.username"));
        jpaProperties.put("hibernate.connection.password", env.getProperty("datasource.oracle.password"));
        jpaProperties.put("hibernate.connection.driver-class-name", env.getProperty("datasource.oracle.driver-class-name"));

        factory.setJpaProperties(jpaProperties);

        return factory;

    }

    /*@Bean
    public DataSourceInitializer oracleDataSourceInitializer()
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(oracleDataSource());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        //databasePopulator.addScript(new ClassPathResource("orders-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(env.getProperty("datasource.oracle.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }*/
}
