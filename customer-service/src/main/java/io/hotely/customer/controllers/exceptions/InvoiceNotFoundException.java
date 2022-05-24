package io.hotely.customer.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvoiceNotFoundException extends RuntimeException {
  public InvoiceNotFoundException(UUID id) {
    super("Could not find Invoice with id: " + id);
  }
}
