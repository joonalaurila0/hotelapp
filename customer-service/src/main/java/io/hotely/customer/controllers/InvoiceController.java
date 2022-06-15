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

import io.hotely.customer.entities.Invoice;
import io.hotely.customer.repositories.InvoiceRepository;
import io.hotely.customer.controllers.exceptions.InvoiceNotFoundException;

@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {

  private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

  @Autowired
  private InvoiceRepository invoiceRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Invoice>> fetchAll() {
    List<Invoice> res = invoiceRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Invoice fetchById(@PathVariable("id") UUID id) {
    return invoiceRepository.findById(id)
      .orElseThrow(() -> new InvoiceNotFoundException(id));
  }

  @GetMapping("/customer/{id}")
  public Iterable<Invoice> fetchByCustomerId(@PathVariable("id") UUID customerId) {
    return invoiceRepository.findCustomerInvoces(customerId);
  }

  @PostMapping("/create")
  public Invoice create(@RequestBody Invoice invoice) {
    log.debug("Here's the object: " + invoice);
    invoice.setId(UUID.randomUUID());
    return invoiceRepository.save(invoice);
  }

  @PutMapping("/update/{id}")
  public Invoice update(@RequestParam("id") UUID id, @RequestParam("total") Float total, @RequestParam("issued") Timestamp issued, @RequestParam("paid") Boolean paid, @RequestParam("paymentDate") LocalDate paymentDate, @RequestParam("cancelled") Boolean cancelled) {
    Invoice res = invoiceRepository.findById(id)
      .orElseThrow(() -> new InvoiceNotFoundException(id));
    res.setTotal(total);
    res.setIssued(issued);
    res.setPaid(paid);
    res.setPaymentDate(paymentDate);
    res.setCancelled(cancelled);
    return invoiceRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    invoiceRepository.deleteById(id);
  }
}
