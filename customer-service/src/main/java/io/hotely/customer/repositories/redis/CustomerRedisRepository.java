package io.hotely.customer.repositories.redis;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hotely.customer.entities.Customer;

@Repository
public interface CustomerRedisRepository extends CrudRepository<Customer, UUID> {};
