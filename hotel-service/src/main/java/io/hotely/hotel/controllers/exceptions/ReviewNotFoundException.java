package io.hotely.hotel.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {
  public ReviewNotFoundException(UUID id) {
    super("Could not find Review with id: " + id);
  }
}
