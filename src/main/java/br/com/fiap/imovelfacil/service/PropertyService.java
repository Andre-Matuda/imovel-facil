package br.com.fiap.imovelfacil.service;

import java.math.BigDecimal;
import br.com.fiap.imovelfacil.dto.PropertyRequest;
import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.repository.PropertyRepository;
import br.com.fiap.imovelfacil.specification.PropertySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    public Page<Property> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Property> findByExample(Property probe, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Property> example = Example.of(probe, matcher);
        return repository.findAll(example, pageable);
    }

    public Page<Property> findBySpecification(
            String title, String city, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minBedrooms, Double minArea, Pageable pageable) {
        var spec = PropertySpecification.filter(title, city, minPrice, maxPrice, minBedrooms, minArea);
        return repository.findAll(spec, pageable);
    }

    @Cacheable(value = "properties", key = "#id")
    public Property findById(Long id) {
        log.info("🔎 A consultar a base de dados para o imóvel ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }

    public Property create(PropertyRequest request) {
        return repository.save(request.toEntity());
    }

    @CachePut(value = "properties", key = "#id")
    public Property update(Long id, PropertyRequest request) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        Property property = request.toEntity();
        property.setId(id);
        return repository.save(property);
    }

    @CacheEvict(value = "properties", key = "#id")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found");
        }
        repository.deleteById(id);
    }
}