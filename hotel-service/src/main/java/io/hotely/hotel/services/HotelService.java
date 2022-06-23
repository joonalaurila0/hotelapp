package io.hotely.hotel.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import io.hotely.hotel.entities.Hotel;
import io.hotely.hotel.repositories.HotelRepository;
import io.hotely.hotel.repositories.redis.HotelRedisRepository;

@Service
public class HotelService {

  private static Logger log = LoggerFactory.getLogger(HotelService.class);

  /** Defines the service to use for the cache service. */
  private HotelRedisRepository hotelRedisRepository; // Cache
  private HotelRepository hotelRepository; // Database

  @Autowired
  public HotelService(HotelRedisRepository hotelRedisRepository, HotelRepository hotelRepository) {
    this.hotelRedisRepository = hotelRedisRepository;
    this.hotelRepository = hotelRepository;
  }

   /**
    * Cache Function: Returns a single hotel from cache if present, otherwise null.
    * @return {@link Hotel}
    */
  private Hotel peekCacheForHotel(Integer id) {
    try {
      return hotelRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of hotels from the cache.
   * @return {@link Iterable<Hotel>} */
  private Iterable<Hotel> peekCacheForAllHotels() {
    try {
      return hotelRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link Hotel} to cache */
  private void saveToCache(Hotel hotel) {
    try {
      hotelRedisRepository.save(hotel);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether Hotel exists in the cache first, then proceeds to remove. */
  private void removeFromCache(Integer id) {
    try {
      Optional<Hotel> cachedhotel = hotelRedisRepository.findById(id);
      if (cachedhotel.isPresent())
        hotelRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link Hotel} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public Hotel addHotel(Hotel hotel) {
    log.info("Adding city to the database -> {}", hotel);
    this.saveToCache(hotel); // Cache the hotel if possible.
    return hotelRepository.save(hotel);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link Hotel},
   * If cache is empty, gets the hotel from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link Hotel} */
  public Hotel findById(Integer id) {
    log.info("Searching for hotel by the id -> {}", id);
    // Try cache first
    Hotel hotel = this.peekCacheForHotel(id);
    log.info("Hotel from cache -> {}", hotel);

    if (hotel != null) {
      log.debug("Succesfully retrieved the hotel from cache, hotel: {}", hotel);
      return hotel;
    }

    Hotel dbHotel = hotelRepository.findById(id).orElse(null);
    if (dbHotel != null)
      this.saveToCache(dbHotel); // Cache the hotel if possible.

    return dbHotel;
  }

  /** 
   * Peeks cache first, if cache is empty gets the hotels from storage,
   * if storage is not empty of hotels, caches the result and returns
   * the hotels from storage; If cache is not empty, returns the hotels
   * from cache.
   *
   * @return {@link List<Hotel>} */
  public List<Hotel> findAll() {
    // Try cache first
    Iterable<Hotel> hotels = this.peekCacheForAllHotels();
    log.info("hasNext returns: {}", hotels.iterator().hasNext());

    // false if list is empty, otherwise true
    if (hotels.iterator().hasNext()) {
      List<Hotel> meowList = new ArrayList<>();
      hotels.forEach(meowList::add);
      log.info("Succesfully found hotels from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<Hotel> hotelList = hotelRepository.findAll();

    // If list is not empty
    if (!hotelList.isEmpty()) {
      // Does caching
      hotelList.forEach(hotel -> {
        this.saveToCache(hotel); // Cache the hotels if possible.
      });
    }

    return hotelList;
  }

  public Hotel updateHotel(Hotel hotel) {
    log.info("Updating hotel by the id of {}", hotel.getId());
    Hotel updatableHotel = hotelRepository.findById(hotel.getId()).orElse(null);
    if (updatableHotel != null) {
      updatableHotel.setEmail(hotel.getEmail());
      updatableHotel.setImg(hotel.getImg());
      updatableHotel.setLocation(hotel.getLocation());
      updatableHotel.setName(hotel.getName());
      updatableHotel.setPhone(hotel.getPhone());
    }
    return hotelRepository.save(updatableHotel);
  }

  /** Removes the hotel from storage and if present in cache, removes from cache as well. */
  public void destroyHotel(Integer id) {
    log.info("Removing hotel by the id -> {}", id);
    hotelRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether hotel exist in cache first
  }
}
