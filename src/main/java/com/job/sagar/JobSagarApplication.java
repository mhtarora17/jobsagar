package com.job.sagar;


import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Application.
 *
 * @author Mohit Arora
 */
@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@ComponentScan(basePackages = {"com.job.sagar"})
@EnableAutoConfiguration
@EnableSchedulerLock(defaultLockAtMostFor = "PT15M")
@EnableAsync
public class JobSagarApplication {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
		SpringApplication.run(JobSagarApplication.class, args);
	}
}
