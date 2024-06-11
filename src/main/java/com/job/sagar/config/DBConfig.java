package com.job.sagar.config;
import com.codahale.metrics.MetricRegistry;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation. Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "jobSagarEntityManagerFactory",
        transactionManagerRef = "jobSagarTransactionManager",
        basePackages = {"com.job.sagar.repository"})
public class DBConfig {
    @Autowired
    Environment environment;

    @Primary
    @Bean (name = "metricRegistryBeanPrimary")
    public MetricRegistry metricRegistry() { return new MetricRegistry(); }

    @Primary
    @Bean (name = "jobSagarDataSource")
    public DataSource dataSource (@Qualifier("metricRegistryBeanPrimary") MetricRegistry metricRegistry) {
        return DBConfigUtils.fetchDataSourceByDbType("primary", environment, metricRegistry);
    }
    @Primary
    @Bean (name = "jobSagarEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
                EntityManagerFactoryBuilder builder,
                @Qualifier("jobSagarDataSource") DataSource dataSource) {
            return builder.dataSource(dataSource)
                    .packages( "com.job.sagar.model").build();
        }

        @Primary
        @Bean (name = "jobSagarTransactionManager")
        public PlatformTransactionManager transactionManager(
                @Qualifier ("jobSagarEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }

        @Bean
public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);}
}
