package org.example.univer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Profile("jdbc")
@ComponentScan("org.example.univer")
@PropertySource("classpath:postgress.properties")
@EnableTransactionManagement // Включение управления транзакциями
public class SpringConfig {
    @Value("${jndiUrl}")
    private String jndiUrl;

    @Value("classpath:schema.sql")
    private Resource schema;

    @Value("classpath:data.sql")
    private Resource data;

    /**
     * Бин для поддержки ${}-синтаксиса в аннотациях @Value.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Получение DataSource через JNDI.
     */
    @Bean
    public DataSource dataSource() {
        try {
            JndiDataSourceLookup jndiDataSource = new JndiDataSourceLookup();
            DataSource dataSource = jndiDataSource.getDataSource(jndiUrl);

            createSchema(dataSource);
            createData(dataSource);

            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("Failed to lookup JNDI DataSource", e);
        }
    }

    /**
     * Создание таблиц базы данных из schema.sql.
     */
    private void createSchema(DataSource dataSource) {
        try {
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(schema);
            databasePopulator.execute(dataSource);
            System.out.println("Schema initialized successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize schema", e);
        }
    }

    /**
     * Заполнение базы данных данными из data.sql.
     */
    private void createData(DataSource dataSource) {
        try {
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(data);
            databasePopulator.execute(dataSource);
            System.out.println("Data initialized successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize data", e);
        }
    }

    /**
     * Настройка SessionFactory для Hibernate.
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.example.univer.models");
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.afterPropertiesSet();

        return sessionFactory;
    }

    /**
     * Настройка менеджера транзакций.
     */
    @Bean
    public PlatformTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    /**
     * Свойства Hibernate.
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("logging.level.org.hibernate.stat", "DEBUG");
        properties.put("spring.jpa.properties.hibernate.generate_statistics", "true");


        return properties;
    }

}