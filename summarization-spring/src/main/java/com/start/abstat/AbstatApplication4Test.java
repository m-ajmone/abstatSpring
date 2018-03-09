package com.start.abstat;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



@SpringBootApplication
public class AbstatApplication4Test {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
		
		TestSummarizer summ = context.getBean(TestSummarizer.class);
		summ.run();
		
		context.close();
	}
	
	
	
}
