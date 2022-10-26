package com.springboot.controller;

import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.model.Pessoa;
import com.springboot.repository.PessoaRepository;

@Controller // le e mapeia os conteudos
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
     @RequestMapping(method = RequestMethod.GET, value="/cadastropessoa") //intercepta essa url
	 public ModelAndView inicio() {
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
    	 andView.addObject("pessoaObj", new Pessoa());
    
    	 return andView; 
	 }
     
     @RequestMapping(method=RequestMethod.POST, value="/salvarpessoa") //intercepta os dados pela url e injeta no objeto pessoa
     public ModelAndView salvar(Pessoa pessoa) {
    	 
    	pessoaRepository.save(pessoa);
    	
	   	ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
	   	Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
    	
    	 return andView;
     }
     
     @RequestMapping(method = RequestMethod.GET, value="/listapessoas")
     public ModelAndView pessoas() { //metodo model e view especifica qual a view que será renderizada e quais dados vai usar    	 
     
     
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
    	 Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
    	 andView.addObject("pessoas", pessoasIt);
    	 andView.addObject("pessoaObj", new Pessoa());

 
    	 return andView;
     
     }
     
     @GetMapping("/editarpessoa/{idpessoa}")//intercepta dados enviados pela url metodo editar e id
     public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa){ //tratar e retornar para a view
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
    	 
    	 Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
    	 andView.addObject("pessoaObj", pessoa.get());
    	 
    	 return andView;
    	 
    	 
     }
     
}





