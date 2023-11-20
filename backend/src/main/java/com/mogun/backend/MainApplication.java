package com.mogun.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class MainApplication {

	public void started() {

		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
	}
	public static void main(String[] args) {

		SpringApplication.run(MainApplication.class, args);
	}
}
