package com.drools.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
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
//    Map<?, ?> parameter = buildParameter();
//    KieSession kieSession = kieContainer.newKieSession();
//    kieSession.setGlobal("logger", log);
//
//    KieCommands kieCommands = kieServices.getCommands();
//    List<Command<?>> commands = new LinkedList<>();
//    commands.add(kieCommands.newInsert(parameter, "map"));
//    commands.add(kieCommands.newFireAllRules());
//
//
//    ExecutionResults results = kieSession.execute(CommandFactory.
//      newBatchExecution(commands));
//
//    log.info("map result:" + mapper.writeValueAsString(parameter));
//
//    kieSession.dispose();
    KieServices ks = KieServices.Factory.get();
    KieFileSystem kfs = ks.newKieFileSystem();
    kfs.write(ks.getResources().newUrlResource(Demo.class.getResource("/pmml/demo.pmml"))
      .setSourcePath("/pmml/demo.pmml")
      .setResourceType(ResourceType.PMML));
    log.info("PMML Score card translated.");
    KieBuilder kieBuilder = ks.newKieBuilder(kfs);

    Results res = kieBuilder.buildAll().getResults();
    KieContainer kieContainer = ks.newKieContainer(kieBuilder.getKieModule().getReleaseId());

    KieBase kbase = kieContainer.getKieBase();
    KieSession session = kbase.newKieSession();

    FactType scorecardType = kbase.getFactType("org.drools.scorecards.example", "SampleScore");
    List<FactField> fields = scorecardType.getFields();
    log.info("FACT FIELDS:");
    for (FactField f : fields) {
      log.info("name: " + f.getName() + " type: " + f.getType().toString());
    }

    Object scorecard = scorecardType.newInstance();


    scorecardType.set(scorecard, "age", 10);
    session.insert(scorecard);
    session.fireAllRules();
    session.dispose();
    //occupation = 5, age = 25, validLicence -1
    log.info( "RESULT: " + scorecardType.get( scorecard, "scorecard_calculatedScore" ).toString());
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
