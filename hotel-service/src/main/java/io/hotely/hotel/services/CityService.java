package io.hotely.hotel.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import io.hotely.hotel.entities.City;

import io.hotely.hotel.repositories.CityRepository;
import io.hotely.hotel.repositories.redis.CityRedisRepository;

@Service
public class CityService {

  private static Logger log = LoggerFactory.getLogger(CityService.class);

  /** Defines the service to use for the cache service. */
  private CityRedisRepository cityRedisRepository; // Cache
  private CityRepository cityRepository; // Database

  @Autowired
  public CityService(CityRepository cityRepository, CityRedisRepository cityRedisRepository) {
    this.cityRedisRepository = cityRedisRepository;
    this.cityRepository = cityRepository;
  }

   /**
    * Cache Function: Returns a single city from cache if present, otherwise null.
    * @return {@link City}
    */
  private City peekCacheForCity(Integer id) {
    try {
      return cityRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of cities from the cache.
   * @return {@link Iterable<City>} */
  private Iterable<City> peekCacheForAllCities() {
    try {
      return cityRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link City} to cache */
  private void saveToCache(City city) {
    try {
      cityRedisRepository.save(city);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether city exists in the cache first, then proceeds to remove. */
  private void removeFromCache(Integer id) {
    try {
      Optional<City> cachedCity = cityRedisRepository.findById(id);
      if (cachedCity.isPresent())
        cityRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link City} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public City addCity(City city) {
    log.info("Adding city to the database -> {}", city);
    this.saveToCache(city); // Cache the city if possible.
    return cityRepository.save(city);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link City},
   * If cache is empty, gets the hotel from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link City} */
  public City findById(Integer id) {
    log.info("Searching for city by the id -> {}", id);
    // Try cache first
    City city = this.peekCacheForCity(id);
    log.info("City from cache -> {}", city);

    if (city != null) {
      log.debug("Succesfully retrieved the city from cache, city: {}", city);
      return city;
    }

    City dbCity = cityRepository.findById(id).orElse(null);
    if (dbCity != null)
      this.saveToCache(dbCity); // Cache the hotel if possible.

    return dbCity;
  }

  /** 
   * Peeks cache first, if cache is empty gets the hotels from storage,
   * if storage is not empty of hotels, caches the result and returns
   * the hotels from storage; If cache is not empty, returns the hotels
   * from cache.
   *
   * @return {@link List<City>} */
  public List<City> findAll() {
    log.info("Inspecting the all values of the cities table");
    // Try cache first
    Iterable<City> cities = this.peekCacheForAllCities();
    log.info("hasNext returns: {}", cities.iterator().hasNext());

    // false if list is empty, otherwise true
    if (cities.iterator().hasNext()) {
      List<City> meowList = new ArrayList<>();
      cities.forEach(meowList::add);
      log.info("Succesfully found cities from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<City> citylist = cityRepository.findAll();

    // If list is not empty
    if (!citylist.isEmpty()) {
      // Does caching
      citylist.forEach(city -> {
        this.saveToCache(city); // Cache the cities if possible.
      });
    }

    return citylist;
  }

  public City updateCity(City city) {
    log.info("Updating city by the id of {}", city.getId());
    City updatableCity = cityRepository.findById(city.getId()).orElse(null);
    if (updatableCity != null) {
      updatableCity.setName(city.getName());
      updatableCity.setRegion(city.getRegion());
      updatableCity.setPopulation(city.getPopulation());
      updatableCity.setLat(city.getLat());
      updatableCity.setLng(city.getLng());
    }
    return cityRepository.save(updatableCity);
  }

  /** Removes the city from storage and if present in cache, removes from cache as well. */
  public void destroyCity(Integer id) {
    log.info("Removing city by the id -> {}", id);
    cityRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether city exist in cache first
  }
}
