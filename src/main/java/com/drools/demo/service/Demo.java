package com.drools.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Demo implements InitializingBean {
//  public static final Logger logger = LoggerFactory.getLogger(Demo.class);

  public static final ObjectMapper mapper = new ObjectMapper();


  @Autowired
  private KieContainer kieContainer;

  @Autowired
  private KieServices kieServices;

  @Override
  public void afterPropertiesSet() throws Exception {
    Map<?, ?> parameter = buildParameter();
    KieSession kieSession = kieContainer.newKieSession();
    kieSession.setGlobal("logger", log);

    KieCommands kieCommands = kieServices.getCommands();
    List<Command<?>> commands = new LinkedList<>();
    commands.add(kieCommands.newInsert(parameter, "map"));
    commands.add(kieCommands.newFireAllRules());


    ExecutionResults results = kieSession.execute(CommandFactory.
      newBatchExecution(commands));

    log.info("map result:" + mapper.writeValueAsString(parameter));

    kieSession.dispose();
  }

  private Map<?, ?> buildParameter() throws JsonProcessingException {
    Map<String, Object> parameter = Maps.newHashMap();
    parameter.put("name", "郭鑫");
    parameter.put("sex", "female");
    parameter.put("age", 31);

    mapper.writeValueAsString(parameter);
    return parameter;
  }
}
