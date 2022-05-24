package io.hotely.hotel.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.controllers.exceptions.RoomNotFoundException;
import io.hotely.hotel.entities.Room;
import io.hotely.hotel.repositories.RoomRepository;

@RestController
@RequestMapping(value = "/rooms")
public class RoomController {

  private static Logger log = LoggerFactory.getLogger(RoomController.class);

  @Autowired
  private RoomRepository roomRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Room>> fetchAll() {
    List<Room> res = roomRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Room fetchById(@PathVariable("id") Long id) {
    return roomRepository.findById(id)
      .orElseThrow(() -> new RoomNotFoundException(id));
  }

  @PostMapping("/create")
  public Room create(@RequestBody Room room) {
    log.debug("Here's the object: " + room);
    return roomRepository.save(room);
  }

  @PutMapping("/update/{id}")
  public Room update(@PathVariable("id") Long id, @RequestBody Room room) {
    Room res = roomRepository.findById(id)
      .orElseThrow(() -> new RoomNotFoundException(id));
    res.setHotel_id(room.getHotel_id());
    res.setRoom_area(room.getRoom_area());
    res.setAvailability(room.getAvailability());
    res.setBooking_price(room.getBooking_price());
    res.setRoom_status(room.getRoom_status());
    res.setCapacity(room.getCapacity());
    return roomRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Long id) {
    roomRepository.deleteById(id);
  }
}
