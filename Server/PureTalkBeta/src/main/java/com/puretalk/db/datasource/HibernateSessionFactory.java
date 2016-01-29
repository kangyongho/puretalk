package com.puretalk.db.datasource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateSessionFactory {
	@Autowired
	BasicDataSource dataSource;

	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder localSessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
		
		localSessionFactoryBuilder.scanPackages("com.puretalk.db.domain")
								  .setProperty("hibernate.dialect",	"org.hibernate.dialect.MySQLDialect");
		
		return localSessionFactoryBuilder.buildSessionFactory();
	}
}
