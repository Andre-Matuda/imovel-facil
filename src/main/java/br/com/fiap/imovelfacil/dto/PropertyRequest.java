package br.com.fiap.imovelfacil.dto;

import java.math.BigDecimal;

import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.model.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// Corpo do POST e do PUT: o imóvel sem o id.
public record PropertyRequest(
        @NotBlank(message = "title is required") @Size(max = 100, message = "title must have at most 100 characters") String title,
        @Size(max = 500, message = "description must have at most 500 characters") String description,
        @NotNull(message = "type is required") PropertyType type,
        @NotBlank(message = "city is required") String city,
        @NotBlank(message = "neighborhood is required") String neighborhood,
        @NotNull(message = "price is required") @DecimalMin(value = "0.01", message = "price must be positive") BigDecimal price,
        @NotNull(message = "bedrooms is required") @Min(value = 0, message = "bedrooms must be zero or more") Integer bedrooms,
        @NotNull(message = "area is required") @Positive(message = "area must be positive") Double area
) {

    public Property toEntity() {
        return Property.builder()
                .title(title)
                .description(description)
                .type(type)
                .city(city)
                .neighborhood(neighborhood)
                .price(price)
                .bedrooms(bedrooms)
                .area(area)
                .build();
    }
}
