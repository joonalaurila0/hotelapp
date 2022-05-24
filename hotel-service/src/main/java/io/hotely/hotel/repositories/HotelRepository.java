package io.hotely.hotel.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import io.hotely.hotel.entities.Hotel;

public interface HotelRepository extends CassandraRepository<Hotel, Long> {}
