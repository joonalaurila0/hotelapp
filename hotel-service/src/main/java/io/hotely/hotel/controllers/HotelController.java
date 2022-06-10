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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.controllers.exceptions.HotelNotFoundException;
import io.hotely.hotel.entities.Hotel;
import io.hotely.hotel.repositories.HotelRepository;

@RestController
@RequestMapping(value = "/hotels")
public class HotelController {

  private static Logger log = LoggerFactory.getLogger(HotelController.class);

  @Autowired
  private HotelRepository hotelRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Hotel>> fetchAll() {
    List<Hotel> res = hotelRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Hotel fetchById(@PathVariable("id") Long id) {
    return hotelRepository.findById(id)
      .orElseThrow(() -> new HotelNotFoundException(id));
  }

  @PostMapping("/create")
  public Hotel create(@RequestBody Hotel hotel) {
    log.debug("Here's the object: " + hotel);
    return hotelRepository.save(hotel);
  }

  @PutMapping("/update/{id}")
  public Hotel update(@PathVariable("id") Long id, @RequestParam("img") String img, @RequestParam("location") String location, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("phone") String phone) {
    Hotel res = hotelRepository.findById(id)
      .orElseThrow(() -> new HotelNotFoundException(id));
    res.setImg(img);
    res.setLocation(location);
    res.setName(name);
    res.setEmail(email);
    res.setPhone(phone);
    return hotelRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Long id) {
    hotelRepository.deleteById(id);
  }
}
