package com.tuprimernegocio.library.database.configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Value("${spring.datasource.driverClassName:com.mysql.cj.jdbc.Driver}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        DriverManagerDataSource driverManager = new DriverManagerDataSource();
        driverManager.setDriverClassName(driver);
        driverManager.setUrl(url);
        driverManager.setUsername(username);
        driverManager.setPassword(password);
        return driverManager;
    }

    @Autowired
    @Bean
    public org.jooq.Configuration configuration(DataSource dataSource) {
        return new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.MARIADB);
    }

    @Bean
    @Autowired
    public DSLContext dslContext(org.jooq.Configuration configuration){
        return DSL.using(configuration);
    }

}
