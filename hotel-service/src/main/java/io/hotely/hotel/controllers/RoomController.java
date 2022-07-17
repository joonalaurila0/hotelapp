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

import io.hotely.hotel.entities.Room;
import io.hotely.hotel.services.RoomService;
import io.hotely.hotel.queue.Producer;

@RestController
@RequestMapping(value = "/rooms")
public class RoomController {

  private static Logger log = LoggerFactory.getLogger(RoomController.class);
  private final Producer producer;
  private final RoomService roomService;

  @Autowired
  RoomController(Producer producer, RoomService roomService) {
    this.producer = producer;
    this.roomService = roomService;
  }

  @GetMapping("/all")
  public List<Room> fetchAll() {
    this.producer.sendMessage("Sending all rooms to client");
    return roomService.findAll();
  }

  @GetMapping("/{id}")
  public Room fetchById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Searching for room by the id of " + id);
    return roomService.findById(id);
  }

  @PostMapping("/create")
  public Room create(@RequestBody Room room) {
    log.debug("RoomController.create called with -> {}", room);
    this.producer.sendMessage("Room added " + room.toString());
    return roomService.addRoom(room);
  }

  @PutMapping("/update/{id}")
  public Room update(@PathVariable("id") Integer id, @RequestBody Room room) {
    this.producer.sendMessage("Searching for Room by the id of " + room.getId());
    return roomService.updateRoom(room);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Integer id) {
    this.producer.sendMessage("Removing a hotel by the id of " + id);
    roomService.destroyRoom(id);
  }
}
