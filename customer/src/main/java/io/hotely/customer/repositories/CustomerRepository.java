package io.hotely.customer.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Customer;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, UUID> {}
