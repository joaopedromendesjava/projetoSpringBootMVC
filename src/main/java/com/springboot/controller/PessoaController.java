package com.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    	 Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
    	 andView.addObject("pessoas", pessoasIt);
    	 andView.addObject("pessoaObj", new Pessoa()); //inicia a tela com obj vazio, pois form espera um objeto
    
    	 return andView; 
	 }
     
     @RequestMapping(method = RequestMethod.POST, value="**/salvarpessoa") //intercepta os dados pela url e injeta no objeto pessoa
     public ModelAndView salvar(Pessoa pessoa) {
    	 
    	pessoaRepository.save(pessoa);
    	
	   	ModelAndView andView = new ModelAndView("cadastro/cadastropessoa.html");
	   	
	   	Iterable<Pessoa> pessoasIt = pessoaRepository.findAll(); //retorna a lista após salvar, trazendo obj salvo
	   	
		andView.addObject("pessoas", pessoasIt); //passa pra lista o obj salvo
		andView.addObject("pessoaObj", new Pessoa()); 
    	
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
    	 
    	 Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa); //carrega objeto pessoa do servidor buscando pelo id
    	 andView.addObject("pessoaObj", pessoa.get()); // passa o objeto pessoa pelo parametro pessoaObj 
    	 
    	 return andView;
    	 
    	 
     }
     
     @GetMapping("/excluirpessoa/{idpessoa}")
     public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
    	 
    	 pessoaRepository.deleteById(idpessoa); // deleta o id passado por parametro	
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa"); // retorna pra essa view
    	 andView.addObject("pessoas", pessoaRepository.findAll()); // retorna todos passando a lista atual (apos delete)
    	 
    	 andView.addObject("pessoaObj", new Pessoa()); //retorna obj vazio, limpo na tela(form)
    	 return andView;
    	 
     
     }
     
     @PostMapping("**/pesquisarpessoa") // intercepta
     public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) { //recebe o param enviado pelo campo no front
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa"); // retorna pra essa view
    	 
    	 andView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa)); // adc o objeto pessoas o que foi buscado
    	 andView.addObject("pessoaObj", new Pessoa());
    	 
    	 return andView;
     }
     
     @GetMapping("/telefones/{idpessoa}")//intercepta dados enviados pela url metodo editar e id
     public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa){ //tratar e retornar para a view
    	 
    	 Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa); //carrega objeto pessoa do servidor buscando pelo id
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/telefones"); // retorna para tela de telefones
    
    	 andView.addObject("pessoaObj", pessoa.get()); // passa o objeto pessoa pelo parametro pessoaObj 
    	 
    	 return andView;
    	 
    	 
     }
     
     
}






