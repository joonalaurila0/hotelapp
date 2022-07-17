package io.hotely.hotel.controllers;

import java.util.List;

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

import io.hotely.hotel.services.HotelService;
import io.hotely.hotel.entities.Hotel;
import io.hotely.hotel.queue.Producer;

@RestController
@RequestMapping(value = "/hotels")
public class HotelController {

  private static Logger log = LoggerFactory.getLogger(HotelController.class);
  private final Producer producer;
  private final HotelService hotelService;

  @Autowired
  HotelController(Producer producer, HotelService hotelService) {
    this.producer = producer;
    this.hotelService = hotelService;
  }

  @GetMapping("/all")
  public List<Hotel> fetchAll() {
    this.producer.sendMessage("Sending all hotels to client");
    return hotelService.findAll();
  }

  @GetMapping("/{id}")
  public Hotel fetchById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Searching for hotel by the id of " + id);
    return hotelService.findById(id);
  }

  @PostMapping("/create")
  public Hotel create(@RequestBody Hotel hotel) {
    log.debug("HotelController.create called with -> {}", hotel);
    this.producer.sendMessage("Hotel added " + hotel.toString());
    return hotelService.addHotel(hotel);
  }

  @PutMapping("/update/{id}")
  public Hotel update(@RequestBody Hotel hotel) {
    this.producer.sendMessage("Searching for hotel by the id of " + hotel.getId());
    return hotelService.updateHotel(hotel);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Removing a hotel by the id of " + id);
    hotelService.destroyHotel(id);
  }
}
