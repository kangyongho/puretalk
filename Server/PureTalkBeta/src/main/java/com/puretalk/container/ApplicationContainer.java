package com.puretalk.container;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.puretalk.config.RootContextConfig;
import com.puretalk.config.ServletContextConfig;

// DispatcherServlet 설정
public class ApplicationContainer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 루트 컨텍스트 등록하고 리스너에 등록 추가
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(RootContextConfig.class);
		ServletContextListener listener = new ContextLoaderListener(rootContext);
		servletContext.addListener(listener);

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(ServletContextConfig.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring",
				new DispatcherServlet(applicationContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

}
