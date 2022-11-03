package com.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.model.Pessoa;
import com.springboot.model.Telefone;
import com.springboot.repository.PessoaRepository;
import com.springboot.repository.TelefoneRepository;

@Controller // le e mapeia os conteudos
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	
     @RequestMapping(method = RequestMethod.GET, value="/cadastropessoa") //intercepta essa url
	 public ModelAndView inicio() {
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
    	 Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
    	 andView.addObject("pessoas", pessoasIt);
    	 andView.addObject("pessoaObj", new Pessoa()); //inicia a tela com obj vazio, pois form espera um objeto
    
    	 return andView; 
	 }
     
     @RequestMapping(method = RequestMethod.POST, value="**/salvarpessoa") //intercepta os dados pela url e injeta no objeto pessoa
     public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) { // valida e passa obj para retornar msg's
    	 
    	 if(bindingResult.hasErrors()) {
    	
    		 	ModelAndView andView = new ModelAndView("cadastro/cadastropessoa.html");
    			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();	
    			andView.addObject("pessoas", pessoasIt);
    			andView.addObject("pessoaObj", pessoa); // retorna objeto que deu erro ao salvar 
    	 
    			List<String> msg = new ArrayList<String>();
    			
    			for(ObjectError objectError : bindingResult.getAllErrors()) { //valida e varre erros
    				msg.add(objectError.getDefaultMessage()); //vem das anotações da entidade (adiciona na variavel msg)
    				System.out.println(msg);
    			}
    			
    			return andView.addObject("msg", msg);
    	 } else {
    	 
    		pessoaRepository.save(pessoa);
  	
		   	ModelAndView andView = new ModelAndView("cadastro/cadastropessoa.html");
		   	Iterable<Pessoa> pessoasIt = pessoaRepository.findAll(); //retorna a lista após salvar, trazendo obj salvo 	
			andView.addObject("pessoas", pessoasIt); //passa pra lista o obj salvo
			andView.addObject("pessoaObj", new Pessoa()); //objeto vazio para o form
	    	
    	 return andView;
    	 
    	 }
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
    	 andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
    	 
    	 return andView;
    	 
    	 
     }
     @PostMapping("**/addfonePessoa/{pessoaid}") //recebe id pela url e aciona o metodo
     public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid")Long pessoaid){ // injeta no objeto e passa o parametro
    	 
    	 
    	 Pessoa pessoa = pessoaRepository.findById(pessoaid).get(); //busca todo o objeto pessoa
    	 telefone.setPessoa(pessoa); // entra com o idpessoa vindo por parametro
    	 
    	 if(telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
    		 
        	 ModelAndView andView = new ModelAndView("cadastro/telefones"); 
        	 andView.addObject("pessoaObj", pessoa);
        	 andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
    	    
        	 List<String> msg = new ArrayList<String>();
        	 
        	 if(telefone.getNumero().isEmpty() || telefone.getNumero() == null) {
        		 msg.add("Numero deve ser informado");
        	 }
        	 if(telefone.getTipo().isEmpty() || telefone.getTipo() == null) {
        		 msg.add("Tipo deve ser informado");
        	 }
        	 
        	 andView.addObject("msg", msg);
        	 
        	 return andView;
        	 
    	 }else {
    	 
	    	 telefoneRepository.save(telefone); // salva os atributos que estao no objeto telefone
	    	 
	    	 ModelAndView andView = new ModelAndView("cadastro/telefones"); 
	    	 andView.addObject("pessoaObj", pessoa);  //retorna para a view com o objeto pai
	    	 andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid)); //recebe o idpessoa e passa para o repository
	    	
	    	 
    	 return andView;
    	 }

     }
     @GetMapping("/removertelefone/{idtelefone}") //links no front sempre chama pela url
     public ModelAndView removertelefone (Telefone telefone, @PathVariable("idtelefone")Long idtelefone) {
    	 
    	 Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa(); // busca objeto pessoa
    	 
    	 telefoneRepository.deleteById(idtelefone);
    	 
    	 ModelAndView andView = new ModelAndView("cadastro/telefones");
    	 andView.addObject("pessoaObj", pessoa);
    	 andView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId())); //passa para o repository o id do telefone
    	 
    	 
    	 return andView;
     }
     
}






