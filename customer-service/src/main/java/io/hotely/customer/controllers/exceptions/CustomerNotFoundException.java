package io.hotely.customer.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
  public CustomerNotFoundException(UUID id) {
    super("Could not find customer with id: " + id);
  }
}
