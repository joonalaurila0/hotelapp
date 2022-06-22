package io.hotely.hotel.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.hotely.hotel.controllers.exceptions.BookingNotFoundException;
import io.hotely.hotel.controllers.json.BookingEntity;
import io.hotely.hotel.controllers.json.InvoiceEntity;
import io.hotely.hotel.entities.Booking;
import io.hotely.hotel.entities.BookingStatus;
import io.hotely.hotel.repositories.BookingRepository;
import io.hotely.hotel.services.CustomerProxy;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

  private static Logger log = LoggerFactory.getLogger(BookingController.class);

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private CustomerProxy customerProxy;

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
  
  @GetMapping("/customer/{id}")
  public Iterable<Booking> findCustomerBookings(@PathVariable("id") UUID customerId) {
    return bookingRepository.findCustomerBookings(customerId);
  }

  /* Uses a record to create a Booking type with ID, 
   * that can easily be fed to application/json parameters */
  @PostMapping("/createwid")
  public Booking createWithID(@RequestBody Booking booking) {
    log.debug("Here's the object: " + booking);
    //Booking newBooking = new Booking(
    //    booking.customer_id(), 
    //    booking.hotel_id(), 
    //    booking.room_id(), 
    //    booking.booking_status(), 
    //    booking.start_date(), 
    //    booking.end_date()
    //    );
    //newBooking.setId(booking.id());
    return bookingRepository.save(booking);
  }

  @PostMapping("/create")
  public Booking create(@RequestBody Booking booking) {
    log.debug("Here's the object: " + booking);
    booking.setId(UUID.randomUUID());
    return bookingRepository.save(booking);
  }

  /** @return {@link InvoiceEntity} 
   * Creates a booking and calls web client to create a invoice for the customer. */
  @PostMapping("/create-and-invoice")
  public Booking
    bookingWithInvoice(
        @RequestHeader(value = "Authorization") 
        String authHeader, @RequestBody Booking newBooking
        ) {
    // Creates a Booking class from the record BookingEntity class and sets the ID.
    log.debug("Booking made: {}", newBooking);
    newBooking.setId(UUID.randomUUID());
    Booking result = bookingRepository.save(newBooking);

    if (result != null) {
      InvoiceEntity invoiceEntity = new InvoiceEntity(
          UUID.randomUUID(), newBooking.getId(), 
          newBooking.getCustomerId(), 25.50f, 
          new Timestamp(new Date().getTime()), 
          false, newBooking.getEndDate().plusMonths(1), 
          false);
      customerProxy.createInvoice(authHeader, invoiceEntity);
    }

    return result;
  }

  @PutMapping("/update/{id}")
  public Booking update(
      @RequestParam("id") UUID id, 
      @RequestParam("customer_id") UUID customerId, 
      @RequestParam("room_id") Long roomId, 
      @RequestParam("hotel_id") Long hotelId, 
      @RequestParam("bookingStatus") BookingStatus bookingStatus, 
      @RequestParam("startDate") Date startDate, 
      @RequestParam("endDate") LocalDate endDate
      ) {
    Booking res = bookingRepository.findById(id)
      .orElseThrow(() -> new BookingNotFoundException(id));
    res.setHotelId(hotelId);
    res.setRoomId(roomId);
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
