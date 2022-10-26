package com.projetoSpringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.springboot.model")
@ComponentScan(basePackages = {"com.*"}) // lendo todos os pacotes do projeto! 
@EnableJpaRepositories(basePackages = {"com.springboot.repository"})
@EnableTransactionManagement
public class ProjetoSpringbootmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringbootmvcApplication.class, args);
	}

}
