package com.softtek.assetworx_api;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.softtek.assetworx_api.repository.AssetAssignmentRepository;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class AssetworxApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetworxApiApplication.class, args);
		
	}
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}


}
