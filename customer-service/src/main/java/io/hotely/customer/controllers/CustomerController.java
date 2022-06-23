package io.hotely.customer.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.customer.queue.Producer;
import io.hotely.customer.entities.Customer;
import io.hotely.customer.entities.enums.Role;
import io.hotely.customer.entities.enums.UserStatus;
import io.hotely.customer.services.CustomerService;
import io.hotely.customer.controllers.exceptions.CustomerNotFoundException;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
  private final Producer producer;
  private final CustomerService customerService;

  @Autowired
  CustomerController(Producer producer, CustomerService customerService) {
    this.producer = producer;
    this.customerService = customerService;
  }

  @GetMapping("/all")
  public List<Customer> fetchAll() {
    this.producer.sendMessage("Sending all customers to the client");
    return customerService.findAll();
  }

  @GetMapping("/{id}")
  public Customer fetchById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Searching for a customer by the id of " + id);
    return customerService.findById(id);
  }

  // Explicitly meant not to fail
  /** @return 0 if empty, 1 if not empty. */
  @GetMapping("/ask/{id}")
  public int askCustomerById(@PathVariable("id") UUID id) {
    return customerService.askCustomerById(id);
  }

  @PostMapping("/create")
  public Customer create(@RequestBody Customer customer) {
    log.debug("CustomerController.create called with -> {}", customer);
    this.producer.sendMessage("Customer added " + customer.toString());
    return customerService.addCustomer(customer);
  }

  @PutMapping("/update/{id}")
  public Customer update(@RequestBody Customer customer) {
    this.producer.sendMessage("Searching for a customer by the id of " + customer.getId());
    return customerService.updateCustomer(customer);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Removing a customer by the id of " + id);
    customerService.destroyCustomer(id);
  }
}
