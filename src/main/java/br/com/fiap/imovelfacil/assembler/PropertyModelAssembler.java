package br.com.fiap.imovelfacil.assembler;

import br.com.fiap.imovelfacil.controller.PropertyController;
import br.com.fiap.imovelfacil.model.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PropertyModelAssembler implements RepresentationModelAssembler<Property, EntityModel<Property>> {

    @Override
    public EntityModel<Property> toModel(Property property) {
        return EntityModel.of(property,
                linkTo(methodOn(PropertyController.class).findById(property.getId())).withSelfRel(),
                linkTo(methodOn(PropertyController.class).findAll(Pageable.unpaged())).withRel("properties")
        );
    }
}