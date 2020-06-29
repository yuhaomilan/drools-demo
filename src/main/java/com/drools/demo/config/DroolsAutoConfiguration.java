package com.drools.demo.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DroolsAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(KieContainer.class)
  public KieContainer kieContainer() throws IOException {
    KieServices kieServices = KieServices.Factory.get();
    return kieServices.getKieClasspathContainer();
  }

  @Bean
  @ConditionalOnMissingBean(KieServices.class)
  public KieServices kieServices() {
    return KieServices.Factory.get();
  }
}
