package io.hotely.customer.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.hotely.customer.entities.Room;

@Repository
@Transactional
public interface RoomRepository extends CassandraRepository<Room, Long> {}
