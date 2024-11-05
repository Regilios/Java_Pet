package org.example.univer.config;

import org.example.univer.dao.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;

@Configuration
@ComponentScan("org.example.univer")
@PropertySource("classpath*:postgress.properties")
public class SpringConfig {

    @Value("${driver}")
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;

    @Value("schema.sql")
    Resource init;
    @Value("data.sql")
    Resource data;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setUrl(url);
        dataSource.setPassword(password);

        createSchema(dataSource);
        createData(dataSource);

        return dataSource;
    }

    @Bean
    @Scope("prototype")
    public Group group() {
        return new Group();
    }
    @Bean
    @Scope("prototype")
    public Student student() {
        return new Student();
    }
    @Bean
    @Scope("prototype")
    public Cathedra cathedra() {
        return new Cathedra();
    }
    @Bean
    @Scope("prototype")
    public Teacher teacher() {
        return new Teacher();
    }
    @Bean
    @Scope("prototype")
    public Vacation vacation() {
        return new Vacation();
    }
    @Bean
    @Scope("prototype")
    public Subject subject() {
        return new Subject();
    }
    @Bean
    @Scope("prototype")
    public LectureTime lectureTime() {
        return new LectureTime();
    }
    @Bean
    @Scope("prototype")
    public Audience audience() {
        return new Audience();
    }
    @Bean
    @Scope("prototype")
    public Holiday holiday() {
        return new Holiday();
    }
    @Bean
    @Scope("prototype")
    public Lecture lecture() {
        return new Lecture();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    private void createData(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(data);
        populator.execute(dataSource);
    }

    private void createSchema(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(init);
        populator.execute(dataSource);
    }
}