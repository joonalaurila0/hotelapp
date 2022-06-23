package io.hotely.hotel.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import io.hotely.hotel.entities.Room;
import io.hotely.hotel.repositories.RoomRepository;
import io.hotely.hotel.repositories.redis.RoomRedisRepository;

@Service
public class RoomService {

  private static Logger log = LoggerFactory.getLogger(RoomService.class);

  /** Defines the service to use for the cache service. */
  private RoomRedisRepository roomRedisRepository; // Cache
  private RoomRepository roomRepository; // Database

  @Autowired
  public RoomService(RoomRedisRepository roomRedisRepository, RoomRepository roomRepository) {
    this.roomRedisRepository = roomRedisRepository;
    this.roomRepository = roomRepository;
  }

   /**
    * Cache Function: Returns a single Room from cache if present, otherwise null.
    * @return {@link Room}
    */
  private Room peekCacheForRoom(Integer id) {
    try {
      return roomRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of rooms from the cache.
   * @return {@link Iterable<Room>} */
  private Iterable<Room> peekCacheForAllRooms() {
    try {
      return roomRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link Room} to cache */
  private void saveToCache(Room room) {
    try {
      roomRedisRepository.save(room);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether Room exists in the cache first, then proceeds to remove. */
  private void removeFromCache(Integer id) {
    try {
      Optional<Room> cachedroom = roomRedisRepository.findById(id);
      if (cachedroom.isPresent())
        roomRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link Room} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public Room addRoom(Room room) {
    log.info("Adding city to the database -> {}", room);
    this.saveToCache(room); // Cache the room if possible.
    return roomRepository.save(room);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link Room},
   * If cache is empty, gets the Room from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link Room} */
  public Room findById(Integer id) {
    log.info("Searching for room by the id -> {}", id);
    // Try cache first
    Room room = this.peekCacheForRoom(id);
    log.info("Room from cache -> {}", room);

    if (room != null) {
      log.debug("Succesfully retrieved the room from cache, room: {}", room);
      return room;
    }

    Room dbRoom = roomRepository.findById(id).orElse(null);
    if (dbRoom != null)
      this.saveToCache(dbRoom); // Cache the room if possible.

    return dbRoom;
  }

  /** 
   * Peeks cache first, if cache is empty gets the rooms from storage,
   * if storage is not empty of rooms, caches the result and returns
   * the rooms from storage; If cache is not empty, returns the rooms
   * from cache.
   *
   * @return {@link List<Room>} */
  public List<Room> findAll() {
    // Try cache first
    Iterable<Room> rooms = this.peekCacheForAllRooms();
    log.info("hasNext returns: {}", rooms.iterator().hasNext());

    // false if list is empty, otherwise true
    if (rooms.iterator().hasNext()) {
      List<Room> meowList = new ArrayList<>();
      rooms.forEach(meowList::add);
      log.info("Succesfully found rooms from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<Room> roomlist = roomRepository.findAll();

    // If list is not empty
    if (!roomlist.isEmpty()) {
      // Does caching
      roomlist.forEach(room -> {
        this.saveToCache(room); // Cache the rooms if possible.
      });
    }

    return roomlist;
  }

  public Room updateRoom(Room room) {
    log.info("Updating room by the id of {}", room.getId());
    Room updatableRoom = roomRepository.findById(room.getId()).orElse(null);
    if (updatableRoom != null) {
      updatableRoom.setHotel_id(room.getHotel_id());
      updatableRoom.setAvailability(room.getAvailability());
      updatableRoom.setBooking_price(room.getBooking_price());
      updatableRoom.setCapacity(room.getCapacity());
      updatableRoom.setRoom_area(room.getRoom_area());
      updatableRoom.setRoom_status(room.getRoom_status());
    }
    return roomRepository.save(updatableRoom);
  }

  /** Removes the Room from storage and if present in cache, removes from cache as well. */
  public void destroyRoom(Integer id) {
    log.info("Removing room by the id -> {}", id);
    roomRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether room exist in cache first
  }
}
