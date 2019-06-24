package com.fensibox.cms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "dataSource", destroyMethod = "")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSourceThird() {
        return new HikariDataSource();
    }
}
