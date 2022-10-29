package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends CrudRepository<Pessoa, Long>{ // pede uma classe e um valor de chave primaria
	@Transactional
	@Query(value = "select p from Pessoa p where p.nome like %:nome%") //usando parametros nomeados
	 List<Pessoa> findPessoaByName(@Param("nome")String nome);
}
