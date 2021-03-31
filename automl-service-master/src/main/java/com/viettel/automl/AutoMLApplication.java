package com.viettel.automl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Slf4j
public class AutoMLApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		log.info("starting app ...");
		SpringApplication.run(AutoMLApplication.class, args);
	}
}