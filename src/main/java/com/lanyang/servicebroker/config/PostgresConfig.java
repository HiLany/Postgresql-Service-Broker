package com.lanyang.servicebroker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.config.BrokerApiVersionConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Ly on 3/5/18.
 */
@Configuration
@ComponentScan(basePackages = "com.lanyang.servicebroker", excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BrokerApiVersionConfig.class) })
public class PostgresConfig {

    private static final Logger logger = LoggerFactory.getLogger(PostgresConfig.class);

    //    jdbc:postgresql://192.168.6.172:5432/postgres?characterEncoding=utf8&useSSL=false
    @Value("${MASTER_JDBC_URL}")
    private String jdbcUrl;

    @Bean
    public Connection jdbc() {
        try {
            Connection conn = DriverManager.getConnection(this.jdbcUrl);

            String serviceTable = "CREATE TABLE IF NOT EXISTS service (serviceinstanceid varchar(200) not null default '',"
                    + " servicedefinitionid varchar(200) not null default '',"
                    + " planid varchar(200) not null default '',"
                    + " organizationguid varchar(200) not null default '',"
                    + " spaceguid varchar(200) not null default '')";

            Statement createServiceTable = conn.createStatement();
            createServiceTable.execute(serviceTable);
            return conn;
        } catch (SQLException e) {
            logger.error("Error while creating initial 'service' table", e);
            return null;
        }
    }

}
