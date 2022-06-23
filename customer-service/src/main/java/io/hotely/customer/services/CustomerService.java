package io.hotely.customer.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import io.hotely.customer.entities.Customer;
import io.hotely.customer.repositories.CustomerRepository;
import io.hotely.customer.repositories.redis.CustomerRedisRepository;

@Service
public class CustomerService {

  private static Logger log = LoggerFactory.getLogger(InvoiceService.class);

  /** Defines the service to use for the cache service. */
  private CustomerRedisRepository customerRedisRepository; // Cache
  private CustomerRepository customerRepository; // Database

  @Autowired
  public CustomerService(CustomerRedisRepository customerRedisRepository, CustomerRepository customerRepository) {
    this.customerRedisRepository = customerRedisRepository;
    this.customerRepository = customerRepository;
  }

   /**
    * Cache Function: Returns a single customer from cache if present, otherwise null.
    * @return {@link Customer}
    */
  private Customer peekCacheForCustomer(UUID id) {
    try {
      return customerRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of customers from the cache.
   * @return {@link Iterable<Customer>} */
  private Iterable<Customer> peekCacheForAllCustomers() {
    try {
      return customerRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link Customer} to cache */
  private void saveToCache(Customer customer) {
    try {
      customerRedisRepository.save(customer);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether Customer exists in the cache first, then proceeds to remove. */
  private void removeFromCache(UUID id) {
    try {
      Optional<Customer> cachedCustomer = customerRedisRepository.findById(id);
      if (cachedCustomer.isPresent())
        customerRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link Customer} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public Customer addCustomer(Customer customer) {
    log.info("Adding Customer to the database -> {}", customer);
    this.saveToCache(customer); // Cache the customer if possible.
    return customerRepository.save(customer);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link Customer},
   * If cache is empty, gets the customer from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link Customer} */
  public Customer findById(UUID id) {
    log.info("Searching for customer by the id -> {}", id);
    // Try cache first
    Customer customer = this.peekCacheForCustomer(id);
    log.info("Customer from cache -> {}", customer);

    if (customer != null) {
      log.debug("Succesfully retrieved the customer from cache, customer: {}", customer);
      return customer;
    }

    Customer dbCustomer = customerRepository.findById(id).orElse(null);
    if (dbCustomer != null)
      this.saveToCache(dbCustomer); // Cache the customer if possible.

    return dbCustomer;
  }

  // Queries whether customer by such Id exists
  // Explicitly meant not to fail
  /** @return 0 if empty, 1 if not empty. */
  public int askCustomerById(UUID id) {
    Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isEmpty())
      return 0;

    return 1;
  }

  /** 
   * Peeks cache first, if cache is empty gets the Customers from storage,
   * if storage is not empty of customers, caches the result and returns
   * the customers from storage; If cache is not empty, returns the customers
   * from cache.
   *
   * @return {@link List<Customer>} */
  public List<Customer> findAll() {
    // Try cache first
    Iterable<Customer> customers = this.peekCacheForAllCustomers();
    log.info("hasNext returns: {}", customers.iterator().hasNext());

    // false if list is empty, otherwise true
    if (customers.iterator().hasNext()) {
      List<Customer> meowList = new ArrayList<>();
      customers.forEach(meowList::add);
      log.info("Succesfully found customers from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<Customer> customerlist = customerRepository.findAll();

    // If list is not empty
    if (!customerlist.isEmpty()) {
      // Does caching
      customerlist.forEach(customer -> {
        this.saveToCache(customer); // Cache the customers if possible.
      });
    }

    return customerlist;
  }

  public Customer updateCustomer(Customer customer) {
    log.info("Updating customer by the id of {}", customer.getId());
    Customer updatableCustomer = customerRepository.findById(customer.getId()).orElse(null);
    if (updatableCustomer != null) {
      updatableCustomer.setEmail(customer.getEmail());
      updatableCustomer.setPassword(customer.getPassword());
      updatableCustomer.setRole(customer.getRole());
      updatableCustomer.setUserstatus(customer.getUserstatus());
    }
    return customerRepository.save(updatableCustomer);
  }

  /** Removes the customer from storage and if present in cache, removes from cache as well. */
  public void destroyCustomer(UUID id) {
    log.info("Removing customer by the id -> {}", id);
    customerRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether customer exist in cache first
  }
}

