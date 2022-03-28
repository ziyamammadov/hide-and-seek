package ee.ut.hideandseek.config.hibernate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    @Value("${spring.datasource.url}")
    private String DATASOURCE_URL;
    @Value("${spring.datasource.username}")
    private String DATASOURCE_USERNAME;
    @Value("#{'${spring.datasource.password}'}")
    private String DATASOURCE_PASSWORD;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String DDL_AUTO;
    @Value("${spring.jpa.hibernate.naming.physical-strategy}")
    private String PHYSICAL_NAMING_STRATEGY;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("ee.ut.hideandseek");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(DATASOURCE_URL);
        dataSource.setUsername(DATASOURCE_USERNAME);
        dataSource.setPassword(DATASOURCE_PASSWORD);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.physical_naming_strategy", PHYSICAL_NAMING_STRATEGY);
        properties.put("hibernate.hbm2ddl.auto", DDL_AUTO);
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        return properties;
    }
}


