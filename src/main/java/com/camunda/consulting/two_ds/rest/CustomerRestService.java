package com.camunda.consulting.two_ds.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camunda.consulting.two_ds.data.Customer;
import com.camunda.consulting.two_ds.data.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerRestService {
  
  CustomerRepository repository;
  
  @Autowired
  public CustomerRestService(CustomerRepository repository) {
    super();
    this.repository = repository;
  }

  @GetMapping("/hello-World")
  public String helloWorld() {
    return "Hello world!";
  }
  
  @GetMapping("/all")
  public String getAllCustomers() {
    List<Customer> allCustomers = (List<Customer>) repository.findAll();
    return allCustomers.stream().map(Customer::toString).collect(Collectors.joining(", "));
  }
  
  @GetMapping("/{id}")
  public String customerbyId(@PathVariable long id) {
    Customer customer = repository.findById(id);
    return customer.toString();
  }
  
  @GetMapping("/{lastname}")
  public String customerByLastName(@PathVariable String lastname) {
    List<Customer> customers = repository.findByLastName(lastname);
    return customers.stream().map(Customer::toString).collect(Collectors.joining(", "));
  }
  
  @PostMapping("/create/{firstname}/{lastname}")
  public String createCustomer(@PathVariable String firstname, @PathVariable String lastname) {
    Customer customer = repository.save(new Customer(firstname, lastname));
    return customer.getId().toString();
  }
  
}
