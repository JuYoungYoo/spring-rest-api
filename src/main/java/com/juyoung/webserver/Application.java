package com.juyoung.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing 		// JPA auditing 활성화 : createdAt, By ..
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder){
//		return builder.build();
//	}
	/*@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception{
		return args -> {
		}
	}*/
}
