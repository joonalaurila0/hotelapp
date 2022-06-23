package io.hotely.hotel.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.services.CustomerProxy;

@RestController
@RequestMapping(value = "/to")
public class ProxyController {

  @Autowired
  private CustomerProxy customerProxy;

  /** returns 1 when Customer can be found, when not 0. */
  @GetMapping("/assert-customer/{id}")
  public ResponseEntity<?> assertCustomer(@PathVariable("id") UUID id) {
    return customerProxy.exists(id);
  }
}
