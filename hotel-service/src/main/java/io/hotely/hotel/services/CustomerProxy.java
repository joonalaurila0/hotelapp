package io.hotely.hotel.services;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.hotely.hotel.controllers.json.CustomerEntity;
import io.hotely.hotel.controllers.json.InvoiceEntity;

/** Web client for communicating with the customer-service. */
@FeignClient(
  name = "customer-service-api", 	// Arbitrary client name
  url = "http://localhost:8000"			// hostname
)
public interface CustomerProxy {
  @RequestMapping(
    method=RequestMethod.GET,
    value="/customers/{customerId}",
    consumes="application/json"
  )
  public CustomerEntity findById(@PathVariable("customerId") UUID customerId);

  @RequestMapping(
    method=RequestMethod.GET,
    value="/customers/ask/{customerId}",
    consumes="application/json"
  )
  public ResponseEntity<?> exists(@PathVariable("customerId") UUID customerId);

  @RequestMapping(
    method=RequestMethod.POST,
    value="/customers/create",
    consumes="application/json"
  )
  public CustomerEntity createUser(@RequestHeader(value = "Authorization", required = true) String authHeader, @RequestBody CustomerEntity customerEntity);

  @RequestMapping(
    method=RequestMethod.POST,
    value="/invoices/create",
    headers = "application/json",
    consumes="application/json"
  )
  public InvoiceEntity 
    createInvoice(@RequestHeader(value = "Authorization", required = true) String authHeader, @RequestBody InvoiceEntity invoiceEntity);

}
