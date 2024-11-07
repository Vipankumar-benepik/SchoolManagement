package com.school.SchoolManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Enables scheduled tasks
public class SchoolManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementApplication.class, args);
	}

}
