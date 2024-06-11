package com.job.sagar;


import com.job.sagar.Utils.CommonUtils;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
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
public class JobSagarApplication implements ApplicationContextAware {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  private static final Logger logger = LogManager.getLogger(JobSagarApplication.class);
  public static void main(String[] args) {
        logger.debug("Initializing the Application");
        try {
             ConfigurableApplicationContext applicationContext = SpringApplication.run(JobSagarApplication.class,args);
        }  catch (Exception e) {
              logger.error("error");
        }

	}

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext)appContext;
        try{
            applicationContext.registerShutdownHook();
//            Flyway flyway = (Flyway) applicationContext.getBean("jobSagarFlyway");
//            flyway.migrate();
        } catch (Exception e) {
            logger.error(CommonUtils.exceptionFormatter(e));
        }
    }
}
