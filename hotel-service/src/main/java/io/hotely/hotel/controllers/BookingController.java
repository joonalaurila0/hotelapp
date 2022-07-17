package io.hotely.hotel.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.queue.Producer;
import io.hotely.hotel.services.BookingService;
import io.hotely.hotel.controllers.json.InvoiceEntity;
import io.hotely.hotel.entities.Booking;
import io.hotely.hotel.services.CustomerProxy;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

  private static Logger log = LoggerFactory.getLogger(BookingController.class);

  private final Producer producer;
  private final BookingService bookingService;
  private final CustomerProxy customerProxy;

  @Autowired
  BookingController(BookingService bookingService, CustomerProxy customerProxy, Producer producer) {
    this.bookingService = bookingService;
    this.customerProxy = customerProxy;
    this.producer = producer;
  }

  @GetMapping("/all")
  public List<Booking> fetchAll() {
    this.producer.sendMessage("Sending all bookings to client");
    return bookingService.findAll();
  }

  @GetMapping("/{id}")
  public Booking fetchById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Searching for booking by the id of " + id);
    return bookingService.findById(id);
  }
  
  @GetMapping("/customer/{id}")
  public Iterable<Booking> findCustomerBookings(@PathVariable("id") UUID customerId) {
    this.producer.sendMessage("Searching for all customer bookings, customerId ->" + customerId);
    return bookingService.findCustomerBookings(customerId);
  }

  @PostMapping("/create")
  public Booking create(@RequestBody Booking booking) {
    log.debug("BookingController.create called with -> {}", booking);
    this.producer.sendMessage("Creating a booking by the id of " + booking.getId());
    return bookingService.addBooking(booking);
  }

  /** @return {@link InvoiceEntity} 
   * Creates a booking and calls web client to create a invoice for the customer. */
  @PostMapping("/create-and-invoice")
  public Booking
    bookingWithInvoice(
        @RequestHeader(value = "Authorization") 
        String authHeader, @RequestBody Booking newBooking
        ) {
    // Creates a Booking class from the record Booking class and sets the ID.
    log.debug("Booking made: {}", newBooking);
    Booking result = bookingService.addBookingNoId(newBooking);

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
  public Booking update(@RequestBody Booking booking) {
    this.producer.sendMessage("Updating a booking by the id of " + booking.getId());
    return bookingService.updateBooking(booking);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    this.producer.sendMessage("Removing a booking by the id of " + id);
    bookingService.destroyBooking(id);
  }
}
