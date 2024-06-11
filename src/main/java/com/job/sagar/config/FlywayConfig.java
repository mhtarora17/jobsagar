package com.job.sagar.config;

//import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

//    @Bean("jobSagarFlyway")
//    public Flyway flywayMigration(@Qualifier("jobSagarDataSource") DataSource dataSource) {
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dataSource);
//        flyway.setLocations("classpath:db/migration/mysql");
//        flyway.setOutOfOrder(true);
//        flyway.setIgnoreMissingMigrations(true);
//        flyway.migrate();
//        flyway.setBaselineOnMigrate(true);
//        return flyway;
//    }
}
