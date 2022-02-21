package io.hotely.customer.entities;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import io.hotely.customer.controllers.CustomerController;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

  /* add update and delete method ? */

  @Override
  public EntityModel<Customer> toModel(Customer customer) {
    return EntityModel.of(customer, 
        linkTo(methodOn(CustomerController.class).findOne(customer.getId())).withSelfRel(),
        linkTo(methodOn(CustomerController.class).all()).withRel("customers"));
  }
}
