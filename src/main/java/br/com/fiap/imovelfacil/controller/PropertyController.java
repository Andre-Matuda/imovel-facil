package br.com.fiap.imovelfacil.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import br.com.fiap.imovelfacil.dto.PropertyRequest;
import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.repository.PropertyRepository;
import br.com.fiap.imovelfacil.service.PropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Endpoints para gerenciamento e busca de imóveis")
public class PropertyController {

    private final PropertyService service;

    // Injetando o repositório para suprir paginação e filtros sem alterar o service
    private final PropertyRepository repository;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Property>>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<Property> assembler) {

        Page<Property> properties = repository.findAll(pageable);

        PagedModel<EntityModel<Property>> pagedModel = assembler.toModel(properties, property ->
                EntityModel.of(property,
                        linkTo(methodOn(PropertyController.class).findById(property.getId())).withSelfRel(),
                        linkTo(methodOn(PropertyController.class).findAll(pageable, assembler)).withRel("properties")
                )
        );

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/example")
    public ResponseEntity<List<Property>> findByExample(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal price) {

        Property exampleProperty = new Property();
        exampleProperty.setTitle(title);
        exampleProperty.setPrice(price);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Property> example = Example.of(exampleProperty, matcher);

        List<Property> properties = repository.findAll(example);
        return ResponseEntity.ok(properties);
    }

    // TODO Diamante 03: GET /properties/search (Specification)
    // - Uma Specification por filtro combinadas com "and"
    // - Faixas e "contém"
    // - Filtros opcionais
    // - Paginado
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Property>>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<Property> assembler) {

        // 1. Uma Specification por filtro ("contém" e faixas)
        Specification<Property> citySpec = (root, query, builder) ->
                (city == null || city.trim().isEmpty()) ? null :
                        builder.like(builder.lower(root.get("city")), "%" + city.toLowerCase() + "%");

        Specification<Property> minPriceSpec = (root, query, builder) ->
                (minPrice == null) ? null :
                        builder.greaterThanOrEqualTo(root.get("price"), minPrice);

        Specification<Property> maxPriceSpec = (root, query, builder) ->
                (maxPrice == null) ? null :
                        builder.lessThanOrEqualTo(root.get("price"), maxPrice);

        // 2. Combinadas com "and"
        Specification<Property> spec = Specification.where(citySpec)
                .and(minPriceSpec)
                .and(maxPriceSpec);

        // 3. Resultado paginado usando o repository
        Page<Property> properties = repository.findAll(spec, pageable);

        // 4. Devolve PagedModel
        PagedModel<EntityModel<Property>> pagedModel = assembler.toModel(properties, property ->
                EntityModel.of(property,
                        linkTo(methodOn(PropertyController.class).findById(property.getId())).withSelfRel(),
                        linkTo(methodOn(PropertyController.class).findAll(null, null)).withRel("properties")
                )
        );

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Property>> findById(@PathVariable Long id) {
        Property property = service.findById(id);

        EntityModel<Property> entityModel = EntityModel.of(property,
                linkTo(methodOn(PropertyController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(PropertyController.class).findAll(null, null)).withRel("properties")
        );

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
    public ResponseEntity<Property> create(@RequestBody @Valid PropertyRequest request) {
        Property property = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(property.getId())
                .toUri();
        return ResponseEntity.created(location).body(property);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> update(@PathVariable Long id, @RequestBody @Valid PropertyRequest request) {
        Property updatedProperty = service.update(id, request);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}