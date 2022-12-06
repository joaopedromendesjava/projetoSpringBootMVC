package com.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pessoa implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message = "Nome não pode ser nulo")
	@NotEmpty(message = "Nome não pode ser vazio")
	private String nome;
	
	@NotNull(message = "Sobrenome não pode ser nulo")					// validações
	@NotEmpty(message = "Sobrenome nao pode ser vazio")
	private String sobrenome;
	
	@Min(value = 18, message = "idade inválida") // define a idade minima para cadastro
	private int idade;

	@Email(message = "Email não válido", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	private String email; //validação de email..
	
	@OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL) // mapeando para objeto pessoa
	private java.util.List<Telefone> telefones; //varios telefones para uma pessoa	
	
	
	private String cep;
	
	private String rua;
	
	private String cidade;
	
	private Integer numero;

	private String bairro;
	
	private String estado;
	
	private String complemento;
	
	private String sexo;
	
	@ManyToOne
	private Profissao profissao;
	
	@Enumerated(EnumType.STRING) // trabalhando com enum tipo string (cargos da pessoa )
	private Cargo cargo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd") //formato padrao
	@Temporal(TemporalType.DATE) // para compreender se é data ou data e hora
	private Date datanascimento;
	
	@Lob
	private byte[] curriculo; // tipo byte para recber arquivo
	
	private String nomeFileCurriculo;
	
	private String tipoFileCurriculo;
	
	
	
	
	public String getNomeFileCurriculo() {
		return nomeFileCurriculo;
	}
	public void setNomeFileCurriculo(String nomeFileCurriculo) {
		this.nomeFileCurriculo = nomeFileCurriculo;
	}
	
	public String getTipoFileCurriculo() {
		return tipoFileCurriculo;
	}
	
	public void setTipoFileCurriculo(String tipoFileCurriculo) {
		this.tipoFileCurriculo = tipoFileCurriculo;
	}
	
	
	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}
	
	public byte[] getCurriculo() {
		return curriculo;
	}
	
	public void setDatanascimento(Date datanascimento) {
		this.datanascimento = datanascimento;
	}
	
	public Date getDatanascimento() {
		return datanascimento;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public Cargo getCargo() {
		return cargo;
	}
	
	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}	

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public java.util.List<Telefone> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(java.util.List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	
}
