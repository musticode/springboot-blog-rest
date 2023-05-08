package com.example.springbootblogrest;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App REST API",
				description = "Spring boot app",
				version = "v1.0",
				contact = @Contact(
						name = "Musti",
						email = "musti"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring boot application",
				url = "springboot"
		)
)
public class SpringbootBlogRestApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApplication.class, args);
	}

}
