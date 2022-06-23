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

import io.hotely.customer.entities.Invoice;
import io.hotely.customer.repositories.InvoiceRepository;
import io.hotely.customer.repositories.redis.InvoiceRedisRepository;

@Service
public class InvoiceService {

  private static Logger log = LoggerFactory.getLogger(InvoiceService.class);

  /** Defines the service to use for the cache service. */
  private final InvoiceRedisRepository invoiceRedisRepository; // Cache
  private final InvoiceRepository invoiceRepository; // Database

  @Autowired
  public InvoiceService(InvoiceRedisRepository invoiceRedisRepository, InvoiceRepository invoiceRepository) {
    this.invoiceRedisRepository = invoiceRedisRepository;
    this.invoiceRepository = invoiceRepository;
  }

   /**
    * Cache Function: Returns a single invoice from cache if present, otherwise null.
    * @return {@link Invoice}
    */
  private Invoice peekCacheForInvoice(UUID id) {
    try {
      return invoiceRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of invoices from the cache.
   * @return {@link Iterable<Invoice>} */
  private Iterable<Invoice> peekCacheForAllInvoices() {
    try {
      return invoiceRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link Invoice} to cache */
  private void saveToCache(Invoice invoice) {
    try {
      invoiceRedisRepository.save(invoice);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether Invoice exists in the cache first, then proceeds to remove. */
  private void removeFromCache(UUID id) {
    try {
      Optional<Invoice> cachedInvoice = invoiceRedisRepository.findById(id);
      if (cachedInvoice.isPresent())
        invoiceRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link Invoice} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public Invoice addInvoice(Invoice invoice) {
    log.info("Adding Invoice to the database -> {}", invoice);
    this.saveToCache(invoice); // Cache the invoice if possible.
    return invoiceRepository.save(invoice);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link Invoice},
   * If cache is empty, gets the invoice from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link Invoice} */
  public Invoice findById(UUID id) {
    log.info("Searching for invoice by the id -> {}", id);
    // Try cache first
    Invoice invoice = this.peekCacheForInvoice(id);
    log.info("Invoice from cache -> {}", invoice);

    if (invoice != null) {
      log.debug("Succesfully retrieved the invoice from cache, invoice: {}", invoice);
      return invoice;
    }

    Invoice dbInvoice = invoiceRepository.findById(id).orElse(null);
    if (dbInvoice != null)
      this.saveToCache(dbInvoice); // Cache the invoice if possible.

    return dbInvoice;
  }

  /** 
   * Peeks cache first, if cache is empty gets the invoices from storage,
   * if storage is not empty of invoices, caches the result and returns
   * the invoices from storage; If cache is not empty, returns the invoices
   * from cache.
   *
   * @return {@link List<Invoice>} */
  public List<Invoice> findAll() {
    // Try cache first
    Iterable<Invoice> invoices = this.peekCacheForAllInvoices();
    log.info("hasNext returns: {}", invoices.iterator().hasNext());

    // false if list is empty, otherwise true
    if (invoices.iterator().hasNext()) {
      List<Invoice> meowList = new ArrayList<>();
      invoices.forEach(meowList::add);
      log.info("Succesfully found invoices from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<Invoice> invoicelist = invoiceRepository.findAll();

    // If list is not empty
    if (!invoicelist.isEmpty()) {
      // Does caching
      invoicelist.forEach(invoice -> {
        this.saveToCache(invoice); // Cache the invoices if possible.
      });
    }

    return invoicelist;
  }

  public Iterable<Invoice> findCustomerInvoices(UUID customerId) {
    return invoiceRepository.findCustomerInvoices(customerId);
  }

  public Invoice updateInvoice(Invoice invoice) {
    log.info("Updating invoice by the id of {}", invoice.getId());
    Invoice updatableInvoice = invoiceRepository.findById(invoice.getId()).orElse(null);
    if (updatableInvoice != null) {
      updatableInvoice.setBookingId(invoice.getBookingId());
      updatableInvoice.setCustomerId(invoice.getCustomerId());
      updatableInvoice.setTotal(invoice.getTotal());
      updatableInvoice.setIssued(invoice.getIssued());
      updatableInvoice.setPaid(invoice.getPaid());
      updatableInvoice.setPaymentDate(invoice.getPaymentDate());
      updatableInvoice.setCancelled(invoice.getCancelled());
    }
    return invoiceRepository.save(updatableInvoice);
  }

  /** Removes the invoice from storage and if present in cache, removes from cache as well. */
  public void destroyInvoice(UUID id) {
    log.info("Removing invoice by the id -> {}", id);
    invoiceRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether invoice exist in cache first
  }
}

