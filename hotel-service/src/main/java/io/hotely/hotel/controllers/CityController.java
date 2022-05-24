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

import io.hotely.hotel.controllers.exceptions.CityNotFoundException;
import io.hotely.hotel.controllers.exceptions.HotelNotFoundException;
import io.hotely.hotel.entities.City;
import io.hotely.hotel.repositories.CityRepository;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

  private static Logger log = LoggerFactory.getLogger(CityController.class);

  @Autowired
  private CityRepository cityRepository;

  @GetMapping("/all")
  public ResponseEntity<List<City>> fetchAll() {
    List<City> res = cityRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public City fetchById(@PathVariable("id") Long id) {
    return cityRepository.findById(id)
      .orElseThrow(() -> new CityNotFoundException(id));
  }

  @PostMapping("/create")
  public City create(@RequestBody City city) {
    log.debug("Here's the object: " + city);
    return cityRepository.save(city);
  }

  @PutMapping("/update/{id}")
  public City update(@PathVariable("id") Long id, @RequestParam("name") String name, @RequestParam("region") String region, @RequestParam("population") int population, @RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
    City res = cityRepository.findById(id)
      .orElseThrow(() -> new HotelNotFoundException(id));
    res.setName(name);
    res.setRegion(region);
    res.setPopulation(population);
    res.setLat(lat);
    res.setLng(lng);
    return cityRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") Long id) {
    cityRepository.deleteById(id);
  }
}
