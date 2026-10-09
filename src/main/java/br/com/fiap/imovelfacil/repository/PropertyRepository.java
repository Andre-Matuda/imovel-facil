package br.com.fiap.imovelfacil.repository;

import br.com.fiap.imovelfacil.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
