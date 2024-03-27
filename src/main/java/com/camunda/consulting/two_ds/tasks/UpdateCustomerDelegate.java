package com.camunda.consulting.two_ds.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.camunda.consulting.two_ds.data.Customer;
import com.camunda.consulting.two_ds.data.CustomerRepository;

@Component
public class UpdateCustomerDelegate implements JavaDelegate {

  private static final Logger LOG = LoggerFactory.getLogger(UpdateCustomerDelegate.class);
  
  private CustomerRepository repository;

  public UpdateCustomerDelegate(CustomerRepository repository) {
    super();
    this.repository = repository;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    LOG.info("Enter update");
    
    Long customerId = (Long) execution.getVariable("customerId");
    
    Customer customer = repository.findById(customerId).get();
    
    customer.setFirstName(customer.getFirstName() + " vor");
    
    repository.save(customer);
    
    LOG.info("End update");
  }

}
