package com.start.abstat;


import javax.servlet.MultipartConfigElement;

import org.apache.solr.client.solrj.SolrServer;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;


@Configuration
@ComponentScan({"com.controller", "com.model", "com.service", "com.dao"})
@EnableSolrRepositories(basePackages="com.repository",multicoreSupport = true)
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	

	@Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

	
    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    

    @Bean
    public SolrServer solrServer() throws Exception
    {
        HttpSolrServerFactoryBean f = new HttpSolrServerFactoryBean();
        f.setUrl("http://localhost:8983/solr");
        f.afterPropertiesSet();
        return f.getSolrServer();
    }
    

	@Bean
	public SolrTemplate solrTemplate(SolrServer server) throws Exception {
		return new SolrTemplate(server);
	}
	

	@Bean
	MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize("100000MB");
	    factory.setMaxRequestSize("100000MB");
	    return factory.createMultipartConfig();
	}
    
    
    
    
    
}
