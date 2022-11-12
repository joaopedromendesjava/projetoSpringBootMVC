package com.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

///			-------   SPRING SECURITY	-------- \\\

@Configuration // escrevendo classe de configuração
@EnableWebSecurity //habilita modulo de segurança
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {  // extende de classe de segurança

	@Autowired
	private ImplementacaoUserDetailsService detailsService;
	
	
	@Override //configura as solicitações de acesso por http	
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().
		 disable() //desativa config. padrao de memoria		
		.authorizeRequests() //permitir restringir acessos
		.antMatchers(HttpMethod.GET,"/").permitAll() // qualquer usuario acessa a tela inicial
		.antMatchers("**/materialize/").permitAll()
		.antMatchers(HttpMethod.GET,"/cadastropessoa").hasAnyRole("ADMIN") // só é permitido pelo admin
		.anyRequest().authenticated()
		.and().formLogin().permitAll() //permite qualquer usuario // cria formulario de login automatico
		.loginPage("/login") //procurar pagina login
		.defaultSuccessUrl("/cadastropessoa") // se logar com sucesso acessa tela cadastro
		.failureUrl("/login?error=true") // se falhar login fica na tela de login mas com param de error
		.and().logout().logoutSuccessUrl("/login") // mapeia url de saida e invalida user autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")); // quando passar url invalida sessão
	
	}
	
	@Override //Cr	ia autenticação do usuario com dbo ou em memoria
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		auth.userDetailsService(detailsService).
		passwordEncoder(new BCryptPasswordEncoder()); // recebe usuario da interface e criptografa a senha
		
		
		/*
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("joao.mendes")
		.password("$2a$10$ye4cmPT7kok2G89GydABWujiV0NAdeV0eFJhsFGGxmpdVKVTeCtyK")
		.roles("ADMIN");
			*/
		}
	
	@Override // Ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
	
}
