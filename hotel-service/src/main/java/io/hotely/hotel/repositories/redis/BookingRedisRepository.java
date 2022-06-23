package io.hotely.hotel.repositories.redis;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hotely.hotel.entities.Booking;

@Repository
public interface BookingRedisRepository extends CrudRepository<Booking, UUID> {};
