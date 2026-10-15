package br.com.fiap.imovelfacil.specification;

import br.com.fiap.imovelfacil.model.Property;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Property> filter(
            String title, String city, BigDecimal minPrice, BigDecimal maxPrice, Integer minBedrooms, Double minArea) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (city != null && !city.isBlank()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")), city.toLowerCase()));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (minBedrooms != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bedrooms"), minBedrooms));
            }
            if (minArea != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area"), minArea));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}