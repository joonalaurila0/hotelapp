package io.hotely.customer.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Booking;

@Repository
@Transactional
public interface BookingRepository extends CassandraRepository<Booking, UUID> {}
