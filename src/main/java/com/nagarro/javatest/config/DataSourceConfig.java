package com.nagarro.javatest.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfig {

	@Value("${datasource.url}")
	private String datasourceUrl;

	@Bean
	public DataSource createDataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl(datasourceUrl);
		dataSource.setDriverClass("net.ucanaccess.jdbc.UcanaccessDriver");
		return dataSource;
	}

}
