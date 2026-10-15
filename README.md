# Imóvel Fácil — projeto base do Diamante 03

API de catálogo de imóveis em Spring Boot. Este é o **ponto de partida** do Projeto Diamante 03 de Java Advanced:
o CRUD já funciona, e a sua equipe acrescenta paginação, buscas, HATEOAS, cache, documentação e Actuator.

## O que já vem pronto

- Entidade `Property` e enum `PropertyType` (`APARTMENT`, `HOUSE`, `STUDIO`, `COMMERCIAL`)
- `DataLoader` com 60 imóveis em São Paulo, Rio de Janeiro, Belo Horizonte e Curitiba
- CRUD em `/properties` devolvendo JSON simples, sem paginação
- Bean Validation no corpo do POST e do PUT (`PropertyRequest`)
- Erros no formato `{"error": "..."}` (`GlobalExceptionHandler`)
- H2 em memória e CORS liberado

Stack: Java 25, Spring Boot 4.1 e Maven.

## Como rodar

Abra a pasta no IntelliJ como projeto Maven e execute `ImovelFacilApplication`,
ou rode `mvn spring-boot:run` no terminal. A API sobe em http://localhost:8080.

O arquivo `test.http` tem exemplos de todas as rotas.

## O que fazer

Siga o enunciado do Projeto Diamante 03. Os pontos de partida estão marcados com `TODO Diamante 03`
no `PropertyController`. Dependências que vocês vão precisar adicionar no `pom.xml`:

- `spring-boot-starter-hateoas`
- `spring-boot-starter-cache`
- `spring-boot-starter-actuator`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui` (versão 3.x, compatível com o Spring Boot 4)

Para conferir o contrato, use o app: https://github.com/joaocarloslima/imovel-facil-app

## Equipe

| Nome           | RM     |
|----------------|--------|
| Kaliel Aquino  | 567587 |
| Andre Matuda   | 566733 |
| Paulo Diedrich | 567618 |
 
