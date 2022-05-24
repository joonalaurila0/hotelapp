package io.hotely.hotel.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import io.hotely.hotel.entities.Room;

public interface RoomRepository extends CassandraRepository<Room, Long> {}
