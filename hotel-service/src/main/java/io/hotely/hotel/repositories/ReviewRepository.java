package io.hotely.hotel.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.hotel.entities.Review;

@Transactional
@Repository
public interface ReviewRepository extends CassandraRepository<Review, UUID> {}
