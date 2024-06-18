package cz.engeto.ja.genesisResources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cz.engeto.ja.genesisResources"})
public class GenesisResourcesApplication {
	public static void main(String[] args) {
		SpringApplication.run(GenesisResourcesApplication.class, args);
	}
}
