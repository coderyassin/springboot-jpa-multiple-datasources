package com.example.demo.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo.repository.postgres",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresDBConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix="datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("postgresDataSource")
    public DataSource postgresDataSource() {
        DataSourceProperties primaryDataSourceProperties = postgresDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(primaryDataSourceProperties.getDriverClassName())
                .url(primaryDataSourceProperties.getUrl())
                .username(primaryDataSourceProperties.getUsername())
                .password(primaryDataSourceProperties.getPassword())
                .build();
    }

    @Bean
    public PlatformTransactionManager postgresTransactionManager()
    {
        EntityManagerFactory factory = postgresEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(postgresDataSource());
        factory.setPackagesToScan(new String[]{"com.example.demo.entity.postgres"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", env.getProperty("jpa.postgres.database-platform"));
        /*jpaProperties.put("javax.persistence.jdbc.url", "org.hibernate.dialect.PostgreSQLDialect");*/
        jpaProperties.put("hibernate.connection.url", env.getProperty("datasource.postgres.url"));
        jpaProperties.put("hibernate.connection.username", env.getProperty("datasource.postgres.username"));
        jpaProperties.put("hibernate.connection.password", env.getProperty("datasource.postgres.password"));
        jpaProperties.put("hibernate.connection.driver-class-name", env.getProperty("datasource.postgres.driver-class-name"));
        factory.setJpaProperties(jpaProperties);

        return factory;

    }

    /*@Bean
    public DataSourceInitializer postgresDataSourceInitializer()
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(postgresDataSource());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        //databasePopulator.addScript(new ClassPathResource("orders-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        //dataSourceInitializer.setEnabled(env.getProperty("datasource.postgres.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }*/
}
