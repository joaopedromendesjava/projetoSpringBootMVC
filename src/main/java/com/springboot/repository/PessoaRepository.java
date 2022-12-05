package com.springboot.repository;

import java.util.List;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{ // pede uma classe e um valor de chave primaria
	@Transactional
	@Query(value = "select p from Pessoa p where p.nome like %:nome%") //usando parametros nomeados
	 List<Pessoa> findPessoaByName(@Param("nome")String nome);

	@Transactional
	@Query(value = "select p from Pessoa p where p.nome like %:nome% and p.sexo = :sexo") //usando parametros nomeados
	 List<Pessoa> findPessoaByNameandsexo(@Param("nome")String nome, @Param("sexo") String sexo);

	@Transactional
	@Query(value = "select p from Pessoa p where p.sexo like %:sexo%") //usando parametros nomeados
	 List<Pessoa> findPessoaBySexo(@Param("sexo")String sexo);
	
	default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable){
	
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		// configurando a pesquisa para consultar por partes do nome no banco de dados, igual like in sql
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome", 
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		// passa uniao dos valores para o banco com a config
		org.springframework.data.domain.Example<Pessoa> example = org.springframework.data.domain.Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		
		return pessoas;
		
	}
	
	default Page<Pessoa> findPessoaBySexoPage(String nome, String sexo, Pageable pageable){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);
		
		// configurando a pesquisa para consultar por partes do nome no banco de dados, igual like in sql
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome", 
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher("sexo", 
						ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		// passa uniao dos valores para o banco com a config
		org.springframework.data.domain.Example<Pessoa> example = org.springframework.data.domain.Example.of(pessoa, exampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		
		return pessoas;
		
	}
	
}
