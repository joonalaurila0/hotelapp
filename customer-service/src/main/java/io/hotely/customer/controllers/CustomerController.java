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

import io.hotely.customer.entities.Customer;
import io.hotely.customer.entities.enums.Role;
import io.hotely.customer.entities.enums.UserStatus;
import io.hotely.customer.repositories.CustomerRepository;
import io.hotely.customer.controllers.exceptions.CustomerNotFoundException;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  private CustomerRepository customerRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Customer>> fetchAll() {
    List<Customer> res = customerRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Customer fetchById(@PathVariable("id") UUID id) {
    return customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(id));
  }

  // Explicitly meant not to fail
  /** @return 0 if empty, 1 if not empty. */
  @GetMapping("/ask/{id}")
  public int askCustomerById(@PathVariable("id") UUID id) {
    Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isEmpty())
      return 0;

    return 1;
  }

  // with id
  @PostMapping("/createwid")
  public Customer create_with_id(@RequestParam("id") UUID id, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("role") Role role, @RequestParam("userstatus") UserStatus userstatus) {
    Customer obj = new Customer(email, password, role, userstatus);
    obj.setId(id);
    log.debug("Here's the object: " + obj);
    return customerRepository.save(obj);
  }

  // without id
  @PostMapping("/create")
  public Customer create(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("role") Role role, @RequestParam("userstatus") UserStatus userstatus) {
    Customer obj = new Customer(email, password, role, userstatus);
    log.debug(this.getClass().getSimpleName());
    log.debug("Here's the object: " + obj);
    return customerRepository.save(obj);
  }

  @PutMapping("/update/{id}")
  public Customer update(@RequestParam("id") UUID id, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("role") Role role, @RequestParam("userstatus") UserStatus userstatus) {
    Customer res = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(id));
    res.setEmail(email);
    res.setPassword(password);
    res.setRole(role);
    res.setUserstatus(userstatus);
    return customerRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    customerRepository.deleteById(id);
  }
}
