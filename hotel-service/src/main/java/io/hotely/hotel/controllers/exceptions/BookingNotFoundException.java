package io.hotely.hotel.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {
  public BookingNotFoundException(UUID id) {
    super("Could not find booking with id: " + id);
  }
}
