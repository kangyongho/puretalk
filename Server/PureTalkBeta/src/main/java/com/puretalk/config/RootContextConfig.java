package com.puretalk.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.puretalk.db.dao.DaoMarker;
import com.puretalk.db.datasource.DataSourceMarker;

@Configuration
@ComponentScan(basePackageClasses={DataSourceMarker.class, DaoMarker.class})
public class RootContextConfig {

}
