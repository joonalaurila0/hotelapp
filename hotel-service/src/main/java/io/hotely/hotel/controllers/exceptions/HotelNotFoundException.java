package io.hotely.hotel.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HotelNotFoundException extends RuntimeException {
  public HotelNotFoundException(Long id) {
    super("Could not find Hotel with id: " + id);
  }
}
