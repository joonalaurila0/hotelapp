package io.hotely.hotel.controllers;

import java.util.List;

import io.hotely.hotel.queue.Producer;
import io.hotely.hotel.services.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.entities.City;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

  private static Logger log = LoggerFactory.getLogger(CityController.class);
  private final Producer producer;
  private CityService cityService;

  @Autowired
  CityController(CityService cityService, Producer producer) {
    this.cityService = cityService;
    this.producer = producer;
  }

  @GetMapping("/all")
  public List<City> fetchAll() {
    this.producer.sendMessage("Sending all cities to client");
    return cityService.findAll();
  }

  @GetMapping("/{id}")
  public City fetchById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Searching for city by the id of " + id);
    return cityService.findById(id);
  }

  @PostMapping("/create")
  public City create(@RequestBody City city) {
    log.debug("CityController.create called with -> {}", city);
    this.producer.sendMessage("City added " + city.toString());
    return cityService.addCity(city);
  }

  @PutMapping("/update/{id}")
  public City update(@RequestBody City city) {
    this.producer.sendMessage("Searching for city by the id of " + city.getId());
    return cityService.updateCity(city);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Removing a city by the id of " + id);
    cityService.destroyCity(id);
  }
}
