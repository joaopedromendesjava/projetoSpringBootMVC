package com.projetoSpringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.springboot.model")
@ComponentScan(basePackages = {"com.*"}) // lendo todos os pacotes do projeto! 
@EnableJpaRepositories(basePackages = {"com.springboot.repository"})
@EnableTransactionManagement
@EnableWebMvc // implementa recursos de mvc
public class ProjetoSpringbootmvcApplication implements WebMvcConfigurer {

	
	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringbootmvcApplication.class, args);
		
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	
		registry.addViewController("/login").setViewName("/login.html"); // mapeando url logine e a pagina que vai redirecionar
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	
	}
	

}
