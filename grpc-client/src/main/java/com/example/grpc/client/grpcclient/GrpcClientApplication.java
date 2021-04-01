package com.example.grpc.client.grpcclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.grpc.client.grpcclient.storage.StorageService;
import com.example.grpc.client.grpcclient.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class GrpcClientApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(GrpcClientApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService ms) {
		return (args) -> {
			ms.deleteAll();
			ms.init();
		};
	}
}

