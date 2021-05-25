package com.stratio.comfandi;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * Automatizacion: proceso por el cual las tablas inmutables y mutables son transferidas a Datacentric con la informacion 
 * almacenada en el origen Comfandi.
 * @author crigueros
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException {	
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
