package br.com.fiap.imovelfacil.specification;

import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.model.PropertyType;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecification {

    public static Specification<Property> titleContains(String title) {
        return (root, query, cb) -> title == null ? null :
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Property> cityEquals(String city) {
        return (root, query, cb) -> city == null ? null :
                cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Property> neighborHood(String neighborHood) {
        return (root, query, cb) -> neighborHood == null ? null :
                cb.equal(cb.lower(root.get("neighborHood")), neighborHood.toLowerCase());
    }

    public static Specification<Property> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) -> minPrice == null ? null :
                cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Property> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null :
                cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Property> bedroomsGreaterThanOrEqual(Integer minBedrooms) {
        return (root, query, cb) -> minBedrooms == null ? null :
                cb.greaterThanOrEqualTo(root.get("bedrooms"), minBedrooms);
    }

    public static Specification<Property> areaGreaterThanOrEqual(Double minArea) {
        return (root, query, cb) -> minArea == null ? null :
                cb.greaterThanOrEqualTo(root.get("area"), minArea);
    }
}