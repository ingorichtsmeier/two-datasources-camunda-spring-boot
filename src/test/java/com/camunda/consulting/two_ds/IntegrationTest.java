package com.camunda.consulting.two_ds;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.camunda.consulting.two_ds.data.Customer;
import com.camunda.consulting.two_ds.data.CustomerRepository;

@SpringBootTest
public class IntegrationTest {
  
  @Autowired
  private RuntimeService runtimeService;
  
  @Autowired
  private CustomerRepository repository;
  
  @Test
  public void runProcessInstance() throws Exception {
    runtimeService.startProcessInstanceByKey("two-datasources-project-process", Map.of("firstname", "fn", "lastname", "ln"));
    
    List<Customer> customerList = repository.findByLastName("ln");
    assertThat(customerList).isNotNull();
    assertThat(customerList.get(customerList.size() - 1).getFirstName()).isEqualTo("fn");
  }
  
  @Test
  public void writeAndReadData() {
    Customer customer = new Customer("vor", "nach");
    Long customerId = null;
    try {
      customerId = repository.save(customer).getId();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception create occured");
    }
    
    try {
      List<Customer> customerList = repository.findByLastName("nach");
      assertThat(customerList).isNotNull();
//      assertThat(customerList).hasSize(1);
      assertThat(customerList.get(0).getFirstName()).isEqualTo("vor");
    } catch (Exception e) {
      e.printStackTrace();
      fail("exception read occured");
    }
    
    try {
      customer = repository.findById(customerId).get();
      customer.setLastName(customer.getLastName() + " nach");
      repository.save(customer);
    } catch (Exception e) {
      e.printStackTrace();
      fail("exception update occured");
    }
  }
  
  @Test
  public void updateByProcess() throws InterruptedException {
    Customer customer = new Customer("procVor", "procNach");
    Long customerId = repository.save(customer).getId();
    
    runtimeService.startProcessInstanceByKey("updateProcess", Map.of("customerId", customerId));
    
    Thread.sleep(3000L);
    
    Customer customerChanged = repository.findById(customerId).get();
    
    assertThat(customerChanged.getFirstName()).endsWith("vor vor");
  }
  
}
