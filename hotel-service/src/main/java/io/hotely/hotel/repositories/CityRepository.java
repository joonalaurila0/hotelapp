package io.hotely.hotel.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.hotel.entities.City;

@Transactional
@Repository
public interface CityRepository extends CassandraRepository<City, Integer> {}
