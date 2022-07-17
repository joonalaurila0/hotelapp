package io.hotely.hotel.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import io.hotely.hotel.entities.Booking;
import io.hotely.hotel.repositories.BookingRepository;
import io.hotely.hotel.repositories.redis.BookingRedisRepository;

@Service
public class BookingService {

  private static Logger log = LoggerFactory.getLogger(BookingService.class);

  /** Defines the service to use for the cache service. */
  private BookingRedisRepository bookingRedisRepository; // Cache
  private BookingRepository bookingRepository; // Database

  @Autowired
  public BookingService(BookingRedisRepository bookingRedisRepository, BookingRepository bookingRepository) {
    this.bookingRedisRepository = bookingRedisRepository;
    this.bookingRepository = bookingRepository;
  }

   /**
    * Cache Function: Returns a single booking from cache if present, otherwise null.
    * @return {@link Booking}
    */
  private Booking peekCacheForBooking(UUID id) {
    try {
      return bookingRedisRepository.findById(id)
        .orElse(null);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** 
   * Cache Function: Returns list of bookings from the cache.
   * @return {@link Iterable<Booking>} */
  private Iterable<Booking> peekCacheForAllBooking() {
    try {
      return bookingRedisRepository.findAll();
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to cache redis, {}", e);
      return null;
    }
  }

  /** Cache Function: Saves {@link Booking} to cache */
  private void saveToCache(Booking booking) {
    try {
      bookingRedisRepository.save(booking);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }

  /** Cache Function: Checks whether Booking exists in the cache first, then proceeds to remove. */
  private void removeFromCache(UUID id) {
    try {
      Optional<Booking> cachedbooking = bookingRedisRepository.findById(id);
      if (cachedbooking.isPresent())
        bookingRedisRepository.deleteById(id);
    } catch (RedisConnectionFailureException e) {
      log.error("Error encountered while trying to save to redis, {}", e);
    }
  }
  
  /** Adds the {@link Booking} to the storage and 
   * if it is not in the cache already, adds it to cache as well. */
  public Booking addBooking(Booking booking) {
    log.info("Adding booking to the database -> {}", booking);
    this.saveToCache(booking); // Cache the booking if possible.
    return bookingRepository.save(booking);
  }

  /** Adds the {@link Booking} to the storage and 
   * if it is not in the cache already, adds it to cache as well
   * Does not require ID to be inputted, generates itself. */
  public Booking addBookingNoId(Booking booking) {
    booking.setId(UUID.randomUUID());
    log.info("Adding booking to the database -> {}", booking);
    this.saveToCache(booking); // Cache the booking if possible.
    return bookingRepository.save(booking);
  }

  /** 
   * Peeks cache first, if cache is not empty returns the {@link Booking},
   * If cache is empty, gets the booking from storage, if storage
   * result is not empty, caches the result and returns it.
   *
   * @return {@link Booking} */
  public Booking findById(UUID id) {
    log.info("Searching for booking by the id -> {}", id);
    // Try cache first
    Booking booking = this.peekCacheForBooking(id);
    log.info("Booking from cache -> {}", booking);

    if (booking != null) {
      log.debug("Succesfully retrieved the booking from cache, booking: {}", booking);
      return booking;
    }

    Booking dbBooking = bookingRepository.findById(id).orElse(null);
    if (dbBooking != null)
      this.saveToCache(dbBooking); // Cache the booking if possible.

    return dbBooking;
  }

  public Iterable<Booking> findCustomerBookings(UUID customerId) {
    return bookingRepository.findCustomerBookings(customerId);
  }

  /** 
   * Peeks cache first, if cache is empty gets the bookings from storage,
   * if storage is not empty of bookings, caches the result and returns
   * the bookings from storage; If cache is not empty, returns the bookings
   * from cache.
   *
   * @return {@link List<Booking>} */
  public List<Booking> findAll() {
    // Try cache first
    Iterable<Booking> bookings = this.peekCacheForAllBooking();
    log.info("hasNext returns: {}", bookings.iterator().hasNext());

    // false if list is empty, otherwise true
    if (bookings.iterator().hasNext()) {
      List<Booking> meowList = new ArrayList<>();
      bookings.forEach(meowList::add);
      log.info("Succesfully found bookings from cache: {}", meowList);
      return meowList; // returns from the cache
    }

    // returns from database
    List<Booking> bookingList = bookingRepository.findAll();

    // If list is not empty
    if (!bookingList.isEmpty()) {
      // Does caching
      bookingList.forEach(booking -> {
        this.saveToCache(booking); // Cache the bookings if possible.
      });
    }

    return bookingList;
  }

  public Booking updateBooking(Booking booking) {
    log.info("Updating booking by the id of {}", booking.getId());
    Booking updatableBooking = bookingRepository.findById(booking.getId()).orElse(null);
    if (updatableBooking != null) {
      updatableBooking.setCustomerId(booking.getCustomerId());
      updatableBooking.setHotelId(booking.getHotelId());
      updatableBooking.setRoomId(booking.getRoomId());
      updatableBooking.setBookingStatus(booking.getBookingStatus());
      updatableBooking.setStartDate(booking.getStartDate());
      updatableBooking.setEndDate(booking.getEndDate());
    }
    return bookingRepository.save(updatableBooking);
  }

  /** Removes the booking from storage and if present in cache, removes from cache as well. */
  public void destroyBooking(UUID id) {
    log.info("Removing booking by the id -> {}", id);
    bookingRepository.deleteById(id);
    this.removeFromCache(id); // Implicitly checks whether booking exist in cache first
  }
}
