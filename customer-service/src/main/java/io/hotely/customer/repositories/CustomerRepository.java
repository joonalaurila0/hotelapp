package io.hotely.customer.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Customer;

@Repository
@Transactional
public interface CustomerRepository extends CassandraRepository<Customer, UUID> {}
