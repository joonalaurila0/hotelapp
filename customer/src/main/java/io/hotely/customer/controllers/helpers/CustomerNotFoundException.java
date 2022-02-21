package io.hotely.customer.controllers.helpers;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException(UUID id) {
    super("Could not find customer " + id);
  }
}
