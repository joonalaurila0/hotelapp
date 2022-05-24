package io.hotely.customer.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException(Long id) {
    super("Could not find Room with id: " + id);
  }
}
