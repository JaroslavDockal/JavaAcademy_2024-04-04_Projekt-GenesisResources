package com.engeto.ja.genesisResources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.engeto.ja.genesisResources.model.User")
public class GenesisResourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenesisResourcesApplication.class, args);
	}
}
