package io.hotely.hotel.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.hotel.entities.Booking;

@Transactional
@Repository
public interface BookingRepository extends CassandraRepository<Booking, UUID> {

  @Transactional
  @Query("SELECT * FROM bookings where customer_id = ?0 ALLOW FILTERING")
  public Iterable<Booking> findCustomerBookings(UUID customerId);
}
