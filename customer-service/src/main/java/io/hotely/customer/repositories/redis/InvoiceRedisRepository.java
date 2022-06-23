package io.hotely.customer.repositories.redis;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hotely.customer.entities.Invoice;

@Repository
public interface InvoiceRedisRepository extends CrudRepository<Invoice, UUID> {};
