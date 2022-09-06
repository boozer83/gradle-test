package io.kakaoi.config;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Profile("test")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MySQLTestContainerConfig {

    private static class Const {
        static final String USERNAME = "user";
        static final String PASSWORD = "secret";
        static final String DATABASE_NAME = "sample";
        static final String TIMEZONE = "Asia/Seoul";
    }

    private final MySQLContainer<?> mySQLContainer;

    public MySQLTestContainerConfig() {
        DockerImageName mysqlImage = DockerImageName.parse("idock.daumkakao.io/cloud-service/mysql:5.7")
            .asCompatibleSubstituteFor("mysql");
        this.mySQLContainer = new MySQLContainer<>(mysqlImage)
            .withDatabaseName(Const.DATABASE_NAME)
            .withUsername(Const.USERNAME)
            .withPassword(Const.PASSWORD)
            .withEnv("LANG", "C.UTF-8")
            .withEnv("MYSQL_ROOT_PASSWORD", Const.PASSWORD)
            .withEnv("MYSQL_DATABASE", Const.DATABASE_NAME)
            .withEnv("MYSQL_USER", Const.USERNAME)
            .withEnv("MYSQL_PASSWORD", Const.PASSWORD)
            .withCommand("--wait-timeout=28800", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withFileSystemBind("local-dev/config/db", "/docker-entrypoint-initdb.d/");
            //.withInitScript("persist/db-init.sql");
    }

    public String getJdbcUrl() {
        return "jdbc:mysql://localhost:" + mySQLContainer.getMappedPort(MySQLContainer.MYSQL_PORT) + "/" + Const.DATABASE_NAME + "?&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=" + Const.TIMEZONE;
    }

    public String getUsername() {
        return Const.USERNAME;
    }

    public String getPassword() {
        return Const.PASSWORD;
    }

    @PostConstruct
    public void start() {
        this.mySQLContainer.start();
    }

    @PreDestroy
    public void stop() {
        this.mySQLContainer.stop();
    }

}
