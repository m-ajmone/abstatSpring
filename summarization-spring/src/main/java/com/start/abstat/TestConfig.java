
package com.start.abstat;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"com.controller", "com.model", "com.service", "com.dao, com.start.abstat"})
@PropertySource(value = "classpath:application.properties")
public class TestConfig {

}
