package com.drools.demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

/**
 * 测试应用
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.drools")
@Slf4j
public class DroolsDemoApplication {

  public static void main(String[] args) {
    try {
      SpringApplication springApplication = new SpringApplication(
        DroolsDemoApplication.class);
      springApplication.addListeners(new ApplicationPidFileWriter());
      springApplication.run(args);
    } catch (Exception e) {
      log.error("DroolsDemoApplication running error：", e);
    }
  }
}
