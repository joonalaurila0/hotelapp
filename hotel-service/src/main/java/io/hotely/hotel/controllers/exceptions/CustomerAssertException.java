package io.hotely.hotel.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import feign.FeignException;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerAssertException extends FeignException {
  public CustomerAssertException(int status, String message) {
    super(404, "Could not find a customer with an id");
  }
}
