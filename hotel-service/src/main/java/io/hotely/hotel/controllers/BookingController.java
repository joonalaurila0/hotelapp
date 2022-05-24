package io.hotely.hotel.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.controllers.exceptions.BookingNotFoundException;
import io.hotely.hotel.entities.Booking;
import io.hotely.hotel.entities.Booking.BookingStatus;
import io.hotely.hotel.repositories.BookingRepository;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

  private static Logger log = LoggerFactory.getLogger(BookingController.class);

  @Autowired
  private BookingRepository bookingRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Booking>> fetchAll() {
    List<Booking> res = bookingRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Booking fetchById(@PathVariable("id") UUID id) {
    return bookingRepository.findById(id)
      .orElseThrow(() -> new BookingNotFoundException(id));
  }

  @PostMapping("/create")
  public Booking create(@RequestBody Booking booking) {
    log.debug("Here's the object: " + booking);
    return bookingRepository.save(booking);
  }

  @PutMapping("/update/{id}")
  public Booking update(@RequestParam("id") UUID id, @RequestParam("customer_id") UUID customer_id, @RequestParam("room_id") Long room_id, @RequestParam("hotel_id") Long hotel_id, @RequestParam("bookingStatus") BookingStatus bookingStatus, @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
    Booking res = bookingRepository.findById(id)
      .orElseThrow(() -> new BookingNotFoundException(id));
    res.setHotel_id(hotel_id);
    res.setRoom_id(room_id);
    res.setStartDate(startDate);
    res.setEndDate(endDate);
    res.setBookingStatus(bookingStatus);
    return bookingRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    bookingRepository.deleteById(id);
  }
}
