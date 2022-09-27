package com._an_5.UNUS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UnusApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnusApplication.class, args);
	}

	@Bean
	CommandLineRunner initUser(UserInterface userRepository) {
		return args -> {
			User user1 = new User("Sachin");
			userRepository.save(user1);

		};
	}
}
