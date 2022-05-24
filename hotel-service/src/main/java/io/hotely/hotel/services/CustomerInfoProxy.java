package io.hotely.hotel.services;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "customer-service") // Service ID of the service
public interface CustomerInfoProxy {
  @RequestMapping(
    method=RequestMethod.GET,
    value="/customers/{customerId}",
    consumes="application/json"
  )
    public ResponseEntity<?> findById(@PathVariable("customerId") UUID customerId);
}
