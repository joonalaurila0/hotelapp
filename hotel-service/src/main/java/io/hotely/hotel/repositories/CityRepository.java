package io.hotely.hotel.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import io.hotely.hotel.entities.City;

public interface CityRepository extends CassandraRepository<City, Long> {}
