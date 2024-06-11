package com.job.sagar.config;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

public class DBConfigUtils {

    public static DataSource fetchDataSourceByDbType(String dbType, Environment environment, MetricRegistry metricRegistry) {
        Properties properties = new Properties();
        properties.put("minimumIdle", environment.getProperty(dbType + ".datasource.minimum-idle", "20"));
        properties.put("maximumPoolSize", environment.getProperty(dbType + ".datasource.maximum-pool-size", "50"));
        properties.put("connectionTimeout", environment.getProperty(dbType + ".datasource.connection-timeout", "20000"));
        properties.put("idleTimeout", environment.getProperty(dbType + ".datasource.idle-timeout", "10000"));
        properties.put("maxLifetime", environment.getProperty(dbType + ".datasource.max-lifetime", "1800000"));
        properties.put("autoCommit", environment.getProperty(dbType + ".datasource.auto-commit", "true"));
        HikariConfig config = new HikariConfig(properties);
        config.setJdbcUrl(environment.getProperty(dbType + ".datasource.jdbc-url"));
        config.setUsername(environment.getProperty(dbType + ".datasource.username"));
        config.setPassword(environment.getProperty(dbType + ".datasource.password"));
        config.setDriverClassName(environment.getProperty(dbType + ".datasource.driverClassName"));
        config.addDataSourceProperty("cachePrepStmts", environment.getProperty(dbType + ".datasource.data-source-properties.cachePrepStmts"));
        config.addDataSourceProperty("prepStmtCacheSize", environment.getProperty(dbType + ".datasource.data-source-properties.prepStmtCacheSize"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", environment.getProperty(dbType + ".datasource.data-source-properties.prepStmtCacheSqlLimit"));
        if (null != metricRegistry) {
            config.setMetricRegistry(metricRegistry);
        }

        return new HikariDataSource(config);
    }
}
