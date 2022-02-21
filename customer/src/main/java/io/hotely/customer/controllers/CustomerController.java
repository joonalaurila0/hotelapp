package io.hotely.customer.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.customer.entities.Customer;
import io.hotely.customer.entities.CustomerModelAssembler;
import io.hotely.customer.services.HotelService;


@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

  private final HotelService hotelService;
  private final CustomerModelAssembler assembler;

  public CustomerController(HotelService hotelService, CustomerModelAssembler assembler) {
    this.hotelService = hotelService;
    this.assembler = assembler;
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Customer>>> all() {
    List<EntityModel<Customer>> customers = hotelService.findAll().stream()
      .map(assembler::toModel)
      .collect(Collectors.toList());

    return ResponseEntity.ok(CollectionModel.of(customers,
        linkTo(methodOn(CustomerController.class).all()).withSelfRel()));
  }

  @PostMapping
  public ResponseEntity<EntityModel<Customer>> newCustomer(@RequestBody Customer newCustomer) {
    hotelService.save(newCustomer);

    return ResponseEntity.created(
        linkTo(methodOn(CustomerController.class)
          .findOne(newCustomer.getId()))
        .toUri()).body(assembler.toModel(newCustomer));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Customer>> findOne(@PathVariable UUID id) {
    Customer customer = hotelService.findById(id);

    return ResponseEntity.ok(assembler.toModel(customer));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Customer>> updateCustomer(@RequestBody Customer newCustomer, @PathVariable UUID id) {
    Customer updatedCustomer = hotelService.updateById(newCustomer, id);
    EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
    hotelService.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}
