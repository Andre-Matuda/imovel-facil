package br.com.fiap.imovelfacil.repository;

import br.com.fiap.imovelfacil.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {


}
