package br.com.fiap.imovelfacil.service;

import java.util.List;

import br.com.fiap.imovelfacil.dto.PropertyRequest;
import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    public List<Property> findAll() {
        return repository.findAll();
    }

    public Property findById(Long id) {
        log.info("🔎 Fetching property {} from the database", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }

    public Property create(PropertyRequest request) {
        return repository.save(request.toEntity());
    }

    public Property update(Long id, PropertyRequest request) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        Property property = request.toEntity();
        property.setId(id);
        return repository.save(property);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        repository.deleteById(id);
    }
}
