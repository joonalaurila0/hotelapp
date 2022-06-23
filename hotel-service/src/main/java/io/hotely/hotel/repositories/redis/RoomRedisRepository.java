package io.hotely.hotel.repositories.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import io.hotely.hotel.entities.Room;

@Repository
public interface RoomRedisRepository extends CrudRepository<Room, Integer> {};
