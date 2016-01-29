package com.puretalk.db.datasource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class DataSource {
	BasicDataSource dataSource;
	
	@Bean
	public BasicDataSource getDataSource() {
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://your domain here/your database name here");
		dataSource.setUsername("your server database user name here");
		dataSource.setPassword("your server database password here");
		return dataSource;
	}
		
}
