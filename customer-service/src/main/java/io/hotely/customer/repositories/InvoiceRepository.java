package io.hotely.customer.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Invoice;

@Repository
@Transactional
public interface InvoiceRepository extends CassandraRepository<Invoice, UUID> {

  @Transactional
  @Query("SELECT * FROM invoices where customer_id = ?0 ALLOW FILTERING")
  public Iterable<Invoice> findCustomerInvoces(UUID customerId);

  //@Transactional
  //@Query(value = "SELECT u FROM Invoice u WHERE u.customer.id = :customerId")
  //public List<Invoice> fetchCustomerInvoices(UUID customerId);

  //@Transactional
  //@Modifying(flushAutomatically = true)
  //@Query(value = "UPDATE Invoice u SET u.paid = :paid WHERE u.id = :id")
  //public void updatePaidById(Boolean paid, UUID id);

  //@Transactional
  //@Modifying(flushAutomatically = true)
  //@Query(value = "UPDATE Invoice u SET u.canceled = :canceled WHERE u.id = :id")
  //public void updateCanceledById(Boolean canceled, UUID id);
}
