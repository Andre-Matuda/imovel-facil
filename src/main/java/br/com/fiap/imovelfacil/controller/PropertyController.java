package br.com.fiap.imovelfacil.controller;

import java.net.URI;
import java.util.List;

import br.com.fiap.imovelfacil.dto.PropertyRequest;
import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService service;

    // TODO Diamante 03: paginar e devolver PagedModel
    @GetMapping
    public List<Property> findAll() {
        return service.findAll();
    }

    // TODO Diamante 03: GET /properties/example (Query by Example)
    // TODO Diamante 03: GET /properties/search (Specification)

    // TODO Diamante 03: devolver EntityModel com os links self e properties
    @GetMapping("/{id}")
    public Property findById(@PathVariable Long id) {
        return service.findById(id);
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
    public Property update(@PathVariable Long id, @RequestBody @Valid PropertyRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
