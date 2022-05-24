package io.hotely.hotel.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CityNotFoundException extends RuntimeException {
  public CityNotFoundException(Long id) {
    super("Could not find city with id: " + id);
  }
}
