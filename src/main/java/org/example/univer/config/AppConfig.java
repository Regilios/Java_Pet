package org.example.univer.config;

import jakarta.annotation.PostConstruct;
import org.example.univer.dao.converter.GroupsConverter;
import org.example.univer.dao.converter.SubjectConverter;
import org.example.univer.dao.converter.TeacherConverter;
import org.example.univer.services.GroupService;
import org.example.univer.services.SubjectService;
import org.example.univer.services.TeacherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EntityScan("org.example.univer.models")
public class AppConfig implements WebMvcConfigurer {

    private final AppSettings appSettings;
    private final ApplicationContext context;
    private final DataSource dataSource;

    public AppConfig(AppSettings appSettings, ApplicationContext context, DataSource dataSource) {
        this.appSettings = appSettings;
        this.context = context;
        this.dataSource = dataSource;
    }

    @Value("classpath:schema.sql")
    private Resource schema;

    @Value("classpath:data.sql")
    private Resource data;

    @PostConstruct
    public void initializeDatabase() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schema, data);
        populator.execute(dataSource);
    }

    @Bean
    public Pageable defaultPageable() {
        return PageRequest.of(0, appSettings.getDefaultPageSize());
    }

    // Thymeleaf config
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(context);
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }

    // ArgumentResolvers
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, appSettings.getDefaultPageSize()));
        resolver.setOneIndexedParameters(true);
        resolvers.add(resolver);
    }

    // Formatters (Converters)
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(context.getBean(SubjectConverter.class));
        registry.addConverter(context.getBean(GroupsConverter.class));
        registry.addConverter(context.getBean(TeacherConverter.class));
    }

    // Filters
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> encodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8", true);
        FilterRegistrationBean<CharacterEncodingFilter> bean = new FilterRegistrationBean<>(filter);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> bean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public SubjectConverter subjectConverter(SubjectService subjectService) {
        return new SubjectConverter(subjectService);
    }

    @Bean
    public GroupsConverter groupsConverter(GroupService groupService) {
        return new GroupsConverter(groupService);
    }

    @Bean
    public TeacherConverter teacherConverter(TeacherService teacherService) {
        return new TeacherConverter(teacherService);
    }
}
