package io.hotely.customer.services;

import java.util.List;
import java.util.UUID;
import java.lang.IllegalArgumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Customer;
import io.hotely.customer.entities.Invoice;
import io.hotely.customer.repositories.CustomerRepository;
import io.hotely.customer.repositories.InvoiceRepository;
import io.hotely.customer.controllers.helpers.CustomerNotFoundException;

@Service
public class HotelService {

  private final CustomerRepository customerRepository;
  private final InvoiceRepository invoiceRepository;
  private static final Logger log = LoggerFactory.getLogger(HotelService.class);

  public HotelService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository) {
    this.customerRepository = customerRepository;
    this.invoiceRepository = invoiceRepository;
  }

  public void save(Customer customer) {
    log.info("Preloading .., saving " + customer.toString() + "to database.");
    customerRepository.save(customer);
  }

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Customer findById(UUID id) {
    return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
  }

  public Customer updateById(Customer customer, UUID id) {
    return customerRepository.findById(id)
      .map(updatableCustomer -> {
        if (customer.getName() != null) {
          updatableCustomer.setName(customer.getName());
        }

        if (customer.getPhone() != null) {
          updatableCustomer.setPhone(customer.getPhone());
        }  

        if (customer.getPhone() != null) {
          updatableCustomer.setAddress(customer.getAddress());
        }

        return customerRepository.save(updatableCustomer);
      })
    .orElseGet(() -> {
      customer.setId(id);
      return customerRepository.save(customer);
    });
  }

  public void deleteById(UUID id) {
    customerRepository.deleteById(id);
  }

  /* adds invoice to a customer */
  @Transactional
  public void addInvoice(UUID customerId, Invoice invoice) {
    Customer customer = this.findById(customerId);
    if (customer != null) {
      invoice.setCustomer(customer);
    }

    customer.addInvoice(invoice);
    invoiceRepository.save(invoice);
  }

  @Transactional
  public void updateInvoiceCanceledStatus(Boolean canceled, UUID id) {
    invoiceRepository.updateCanceledById(canceled, id);
  }

  /* updates paid status of an invoice for a customer */
  @Transactional
  public void updateInvoicePaidStatus(Boolean paid, UUID id) {
    invoiceRepository.updatePaidById(paid, id);
  }

  /* fetches all the invoices that belong to a customer */
  @Transactional
  public List<Invoice> fetchCustomerInvoices(UUID customerId) {
    List<Invoice> invoices = invoiceRepository.fetchCustomerInvoices(customerId);
    System.out.println(invoices);
    return invoices;
  }

}


