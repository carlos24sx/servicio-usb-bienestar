package co.edu.unisimon.bienestar.configuration.database;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;


/**
 * Sets the configuration of the DataSource associated with SQL Server.
 *
 * <p>You can find more information on the following
 * <a href="https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa">blog.</a>
 * </p>
 *
 * @author William Torres
 * @version 1.0
 * @see org.springframework.context.annotation.PropertySource
 * @see org.springframework.context.annotation.PropertySource
 * @see EnableJpaRepositories
 * @see ConditionalOnClass
 * @since April, 2020
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@AllArgsConstructor
@EnableJpaRepositories(
        entityManagerFactoryRef = "crmEntityManagerFactory",
        transactionManagerRef = "crmTransactionManager",
        basePackages = {"co.edu.unisimon.bienestar.business.domain.persistence.repository"})
public class SqlServerConfiguration {

    private static final String PACKAGE_ENTITIES = "co.edu.unisimon.bienestar.business.domain.persistence.entity";

    /**
     * Allows access to the configuration parameters defined in the database.properties file.
     */
    private final Environment env;

    /**
     * Configures and returns the DataSource associated with the database.
     *
     * @return Development DataSource
     * @throws NullPointerException if the DriverClassName property is null.
     * @see DataSource
     */
    @Bean(name = "dataSource")
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("sqlserver.datasource.url"));
        dataSource.setUsername(env.getProperty("sqlserver.datasource.username"));
        dataSource.setPassword(env.getProperty("sqlserver.datasource.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("sqlserver.datasource.driver")));
        return dataSource;
    }

    /**
     * EntityManagerFactory is configured. The Hibernate configuration is established and the entity are declared.
     * through the path of the folder that contains them.
     *
     * @return A container-managed EntityManagerFactory
     */
    @Bean(name = "crmEntityManagerFactory")
    @ConditionalOnBean(name = "dataSource")
    @ConditionalOnMissingBean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(PACKAGE_ENTITIES);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        additionalProperties();
        em.setJpaProperties(additionalProperties());
        return em;
    }

    /**
     * Creates a JpaTransactionManager and associate it with entityManagerFactory.
     *
     * @param entityManagerFactory It's created with the {@code entityManagerFactory()} method and injected by Spring.
     * @return An appropriate transaction manager for applications that use a single JPA EntityManagerFactory for
     * access to transactional data.
     */
    @Bean(name = "crmTransactionManager")
    @ConditionalOnMissingBean(type = "JpaTransactionManager")
    JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Set a persistent set of Hibernate properties using the {@code Properties} class
     *
     * @return A persistent set of Hibernate properties.
     * @see Properties
     */
    private Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("sqlserver.jpa.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("sqlserver.jpa.show-sql"));
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("sqlserver.jpa.hibernate.ddl"));
        return hibernateProperties;
    }

}
