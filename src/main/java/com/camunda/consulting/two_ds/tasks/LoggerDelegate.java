package com.camunda.consulting.two_ds.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("logger")
public class LoggerDelegate implements JavaDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(LoggerDelegate.class);

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    Boolean forcedError = (Boolean) execution.getVariable("error");
    
    if (forcedError != null && forcedError == true) {
      LOG.info("Forcing an error");
      throw new RuntimeException("Forced error");
    }
    LOG.info("passing {} of process instance {}", execution.getCurrentActivityName(), execution.getProcessInstanceId());
  }

}
