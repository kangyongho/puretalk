package com.puretalk.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.puretalk.controller.WebControllerMarker;
import com.puretalk.web.WebConfigurationMarker;

@Configuration
@ComponentScan(basePackageClasses={WebConfigurationMarker.class, WebControllerMarker.class})
public class ServletContextConfig {

}
