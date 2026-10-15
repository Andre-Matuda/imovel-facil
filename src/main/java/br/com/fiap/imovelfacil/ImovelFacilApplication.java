package br.com.fiap.imovelfacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImovelFacilApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImovelFacilApplication.class, args);
    }
}
