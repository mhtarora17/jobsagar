package com.job.sagar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

public class FilterConfig {
    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Bean
    public FilterRegistrationBean newMethodFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean();
        Filter myFilter = new MethodFilter();
        beanFactory.autowireBean(myFilter);
        registration.setFilter(myFilter);
        registration.addUrlPatterns(new String[]{"/**"});
        return registration;
    }


    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return customizer -> customizer.addConnectorCustomizers(connector -> {
            connector.setAllowTrace(true);
        });
    }
}
