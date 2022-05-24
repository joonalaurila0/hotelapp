package io.hotely.hotel.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import io.hotely.hotel.entities.Review;

public interface ReviewRepository extends CassandraRepository<Review, UUID> {}
