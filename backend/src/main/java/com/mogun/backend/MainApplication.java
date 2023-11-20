package com.mogun.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class MainApplication {

	@PostConstruct
	public void started() {

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {

		SpringApplication.run(MainApplication.class, args);
	}
}
