package io.kakaoi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Configuration
@Profile("test")
public class TestDataSourceConfig {

    private final MySQLTestContainerConfig mySQLTestContainerConfig;

    public TestDataSourceConfig(MySQLTestContainerConfig mySQLTestContainerConfig) {
        this.mySQLTestContainerConfig = mySQLTestContainerConfig;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .url(mySQLTestContainerConfig.getJdbcUrl())
            .username(mySQLTestContainerConfig.getUsername())
            .password(mySQLTestContainerConfig.getPassword())
            .build();
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

}
