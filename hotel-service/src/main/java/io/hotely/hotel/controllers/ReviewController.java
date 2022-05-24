package io.hotely.hotel.controllers;

import java.util.List;
import java.util.UUID;

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

import io.hotely.hotel.controllers.exceptions.ReviewNotFoundException;
import io.hotely.hotel.entities.Review;
import io.hotely.hotel.repositories.ReviewRepository;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

  private static Logger log = LoggerFactory.getLogger(ReviewController.class);

  @Autowired
  private ReviewRepository reviewRepository;

  @GetMapping("/all")
  public ResponseEntity<List<Review>> fetchAll() {
    List<Review> res = reviewRepository.findAll();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Review fetchById(@PathVariable("id") UUID id) {
    return reviewRepository.findById(id)
      .orElseThrow(() -> new ReviewNotFoundException(id));
  }

  @PostMapping("/create")
  public Review create(@RequestBody Review review) {
    log.debug("Here's the object: " + review);
    return reviewRepository.save(review);
  }

  @PutMapping("/update/{id}")
  public Review update(@PathVariable("id") UUID id, @RequestBody Review review) {
    Review res = reviewRepository.findById(id)
      .orElseThrow(() -> new ReviewNotFoundException(id));
    res.setCustomer_id(review.getCustomer_id());
    res.setHotel_id(review.getHotel_id());
    res.setRating(review.getRating());
    res.setDescription(review.getDescription());
    return reviewRepository.save(res);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable("id") UUID id) {
    reviewRepository.deleteById(id);
  }
}
