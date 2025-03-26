package com.dev.agregador_investimento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgregadorInvestimentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorInvestimentoApplication.class, args);
	}

}
