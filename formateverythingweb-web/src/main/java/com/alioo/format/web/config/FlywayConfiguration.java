//package com.alioo.web.config;
//
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.flywaydb.core.Flyway;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfiguration {
//
//
//    @Bean(initMethod = "migrate")
//    public Flyway flyway() {
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dataSource());
//        flyway.setBaselineOnMigrate(true);
//        return flyway;
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return new DruidDataSource();
//    }
//
//}
