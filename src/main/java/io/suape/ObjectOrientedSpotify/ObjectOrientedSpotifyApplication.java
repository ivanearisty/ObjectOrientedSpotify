package io.suape.ObjectOrientedSpotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ObjectOrientedSpotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObjectOrientedSpotifyApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder pwEncoder() {
		return new  BCryptPasswordEncoder(10);
	}

}
