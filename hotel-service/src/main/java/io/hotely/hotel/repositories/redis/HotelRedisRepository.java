package io.hotely.hotel.repositories.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hotely.hotel.entities.Hotel;

@Repository
public interface HotelRedisRepository extends CrudRepository<Hotel, Integer> {};
