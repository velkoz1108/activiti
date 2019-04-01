package com.wangtao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author : want
 * @Team Home
 * @date : 2019-3-4 14:34 星期一
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "myDataSource")
    @Qualifier("myDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getMyDataSource() {
        return DataSourceBuilder.create().build();
    }
}