package com.camunda.consulting.two_ds.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.consulting.two_ds.data.Customer;
import com.camunda.consulting.two_ds.data.CustomerRepository;

@Component
public class CustomerLoggingTask implements JavaDelegate {
  
  private static final Logger LOG = LoggerFactory.getLogger(CustomerLoggingTask.class);

  CustomerRepository repository;
  
  @Autowired
	public CustomerLoggingTask(CustomerRepository repository) {
    super();
    this.repository = repository;
  }

  @Override
	public void execute(DelegateExecution execution) throws Exception {

		String firstname = (String) execution.getVariable("firstname");
		String lastname = (String) execution.getVariable("lastname");

		Customer customerToPersist = new Customer(firstname, lastname);

		LOG.info("Persisiting Customer entity");
		this.repository
			.save(customerToPersist);

		LOG.info("Persistance done(?)");
	}

}
