package br.com.fiap.imovelfacil.controller;

import br.com.fiap.imovelfacil.assembler.PropertyModelAssembler;
import br.com.fiap.imovelfacil.dto.PropertyRequest;
import br.com.fiap.imovelfacil.model.Property;
import br.com.fiap.imovelfacil.model.PropertyType;
import br.com.fiap.imovelfacil.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Endpoints para a gestão e pesquisa de imóveis")
public class PropertyController {

    private final PropertyService service;
    private final PropertyModelAssembler assembler;
    private final PagedResourcesAssembler<Property> pagedResourcesAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os imóveis com paginação")
    @ApiResponse(responseCode = "200", description = "Página de imóveis devolvida com sucesso")
    public ResponseEntity<PagedModel<EntityModel<Property>>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Property> properties = service.findAll(pageable);
        PagedModel<EntityModel<Property>> pagedModel = pagedResourcesAssembler.toModel(properties, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/example")
    @Operation(summary = "Pesquisa de imóveis por Query by Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesquisa efetuada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content(schema = @Schema(example = "{\"error\": \"Invalid parameters\"}")))
    })
    public ResponseEntity<PagedModel<EntityModel<Property>>> findByExample(
            @Parameter(description = "Tipo do imóvel") @RequestParam(required = false) PropertyType type,
            @Parameter(description = "Cidade") @RequestParam(required = false) String city,
            @Parameter(description = "Bairro") @RequestParam(required = false) String neighborhood,
            @Parameter(description = "Número de quartos") @RequestParam(required = false) Integer bedrooms,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {

        Property probe = new Property();
        probe.setType(type);
        probe.setCity(city);
        probe.setNeighborhood(neighborhood);
        probe.setBedrooms(bedrooms);

        Page<Property> properties = service.findByExample(probe, pageable);
        PagedModel<EntityModel<Property>> pagedModel = pagedResourcesAssembler.toModel(properties, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/search")
    @Operation(summary = "Pesquisa avançada de imóveis com Specifications")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesquisa efetuada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content(schema = @Schema(example = "{\"error\": \"Invalid parameters\"}")))
    })
    public ResponseEntity<PagedModel<EntityModel<Property>>> findBySpecification(
            @Parameter(description = "Título contém (ignora maiúsculas/minúsculas)") @RequestParam(required = false) String title,
            @Parameter(description = "Cidade exata (ignora maiúsculas/minúsculas)") @RequestParam(required = false) String city,
            @Parameter(description = "Preço mínimo") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Preço máximo") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Quartos mínimos") @RequestParam(required = false) Integer minBedrooms,
            @Parameter(description = "Área mínima") @RequestParam(required = false) Double minArea,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {

        Page<Property> properties = service.findBySpecification(title, city, minPrice, maxPrice, minBedrooms, minArea, pageable);
        PagedModel<EntityModel<Property>> pagedModel = pagedResourcesAssembler.toModel(properties, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Procurar imóvel por ID (Com cache ativo)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imóvel encontrado"),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado", content = @Content(schema = @Schema(example = "{\"error\": \"Property not found\"}")))
    })
    public ResponseEntity<EntityModel<Property>> findById(@PathVariable Long id) {
        Property property = service.findById(id);
        return ResponseEntity.ok(assembler.toModel(property));
    }

    @PostMapping
    @Operation(summary = "Criar um novo imóvel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Imóvel criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content(schema = @Schema(example = "{\"error\": \"Validation error\"}")))
    })
    public ResponseEntity<EntityModel<Property>> create(@RequestBody @Valid PropertyRequest request) {
        Property property = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(property.getId())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(property));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um imóvel existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imóvel atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado", content = @Content(schema = @Schema(example = "{\"error\": \"Property not found\"}")))
    })
    public ResponseEntity<EntityModel<Property>> update(@PathVariable Long id, @RequestBody @Valid PropertyRequest request) {
        Property property = service.update(id, request);
        return ResponseEntity.ok(assembler.toModel(property));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar um imóvel")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imóvel eliminado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imóvel não encontrado", content = @Content(schema = @Schema(example = "{\"error\": \"Property not found\"}")))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}