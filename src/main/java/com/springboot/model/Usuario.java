package com.springboot.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails { //implementa uma interface para utilizar os metodos e fazer validacao
	
			private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String login;
	private String senha;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_role", //cria tabela para unir  // cria tabela de acesso do usuario
		joinColumns = @JoinColumn(name = "usuario_id", //coluna da nova tabela
			referencedColumnName = "id", table = "usuario"), // referenciando
				inverseJoinColumns = @JoinColumn(name = "role_id", // cria coluna nova que aponta para tabela rule 
					referencedColumnName = "id", table = "rule")) // referenciando
	private List<Role> roles; // um para muitos

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return roles;
	}

	@Override
	public String getPassword() {
		
		return senha;
	}

	@Override
	public String getUsername() {

		return login;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}
	

}
