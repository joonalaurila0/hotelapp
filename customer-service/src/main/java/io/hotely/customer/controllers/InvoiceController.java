package io.hotely.customer.controllers;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.customer.queue.Producer;
import io.hotely.customer.services.InvoiceService;
import io.hotely.customer.entities.Invoice;
import io.hotely.customer.controllers.exceptions.InvoiceNotFoundException;

@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {

  private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);
  private final Producer producer;
  private final InvoiceService invoiceService;

  @Autowired
  InvoiceController(Producer producer, InvoiceService invoiceService) {
    this.producer = producer;
    this.invoiceService = invoiceService;
  }

  @GetMapping("/all")
  public List<Invoice> fetchAll() {
    this.producer.sendMessage("Sending all invoices to the client");
    return invoiceService.findAll();
  }

  @GetMapping("/{id}")
  public Invoice fetchById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Searching for a invoice by the id of " + id);
    return invoiceService.findById(id);
  }

  @GetMapping("/customer/{id}")
  public Iterable<Invoice> fetchByCustomerId(@PathVariable("id") UUID customerId) {
    return invoiceService.findCustomerInvoices(customerId);
  }

  @PostMapping("/create")
  public Invoice create(@RequestBody Invoice invoice) {
    log.debug("InvoiceController.create called with -> {}", invoice);
    this.producer.sendMessage("Invoice added " + invoice.toString());
    return invoiceService.addInvoice(invoice);
  }

  @PutMapping("/update/{id}")
  public Invoice update(@RequestBody Invoice invoice) {
    this.producer.sendMessage("Searching for a invoice by the id of " + invoice.getId());
    return invoiceService.updateInvoice(invoice);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Removing a invoice by the id of " + id);
    invoiceService.destroyInvoice(id);
  }
}
