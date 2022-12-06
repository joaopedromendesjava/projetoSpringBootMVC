package com.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.model.Pessoa;
import com.springboot.model.Telefone;
import com.springboot.repository.PessoaRepository;
import com.springboot.repository.ProfissaoRepository;
import com.springboot.repository.TelefoneRepository;

@Controller // le e mapeia os conteudos
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private ReportUtil reportUtil;

	@Autowired
	private ProfissaoRepository profissaoRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa") // intercepta essa url
	public ModelAndView inicio() {

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
																																													// ordenando
		andView.addObject("pessoaObj", new Pessoa()); // inicia a tela com obj vazio, pois form espera um objeto
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")))); //buscando/paginando e ordenando
		andView.addObject("profissoes", profissaoRepository.findAll()); // atributo profissao

		return andView;
	}
	
	@GetMapping("/pessoaspag") // faz paginação
	public ModelAndView carregaPessoaPorPaginacao(@PageableDefault(size = 5)Pageable pageable,
			ModelAndView andView, @RequestParam("nomepesquisa") String nomepesquisa ) {
		
		Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable); 
		andView.addObject("pessoas", pagePessoa);
		andView.addObject("pessoaObj", new Pessoa());
		andView.addObject("nomepesquisa", nomepesquisa);
		andView.setViewName("cadastro/cadastropessoa");
		
		return andView;
	}

	@PostMapping(value = "**/salvarpessoa", consumes = { "multipart/form-data" }) // intercepta os dados pela url e injeta no objeto pessoa, consome arquivo para salvar
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file)
			throws IOException { // valida e passa obj para retornar msg's, passa parametro file vindo do form
									// arquivo

		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId())); // busca os telefones da pessoa para
																				// carregar em memoria

		if (bindingResult.hasErrors()) {

			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa.html");
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
			andView.addObject("pessoaObj", pessoa); // retorna objeto que deu erro ao salvar

			List<String> msg = new ArrayList<String>();

			for (ObjectError objectError : bindingResult.getAllErrors()) { // valida e varre erros
				msg.add(objectError.getDefaultMessage()); // vem das anotações da entidade (adiciona na variavel msg)
				System.out.println(msg);
			}

			return andView.addObject("msg", msg);

		} else {

			if (file.getSize() > 0) { // valida tamaho do arquivo maior que 0 e seta no obj pessoa
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoFileCurriculo(file.getContentType()); // setanto tipo do arquivo
				pessoa.setNomeFileCurriculo(file.getOriginalFilename()); // setando nome do arquivo

			} else {
				if (pessoa.getId() != 0 && pessoa.getId() > 0) { // editando

					Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get(); // busca arquivo ja salvo e
																							// mantem o mesmo

					pessoa.setCurriculo(pessoaTemp.getCurriculo());
					pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo()); // mantendo tudo que ja tem salvo
					pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());

				}
			}

			pessoaRepository.save(pessoa); // salva todo o objeto pessoa

			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa.html");
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")))); // passa pra lista o obj salvo
			andView.addObject("pessoaObj", new Pessoa()); // objeto vazio para o form
			andView.addObject("profissoes", profissaoRepository.findAll());

			return andView;

		}
	}

	@GetMapping(value = "/listapessoas")
	public ModelAndView pessoas() { 
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")))); // retorna todas pessoas para view//
		andView.addObject("pessoaObj", new Pessoa());

		return andView;

	}

	@GetMapping("/editarpessoa/{idpessoa}") // intercepta dados enviados pela url metodo editar e id
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) { // tratar e retornar para a view

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa); // carrega objeto pessoa do servidor buscando pelo id
		andView.addObject("pessoaObj", pessoa.get()); // passa o objeto pessoa pelo parametro pessoaObj
		andView.addObject("profissoes", profissaoRepository.findAll());

		return andView;

	}

	@GetMapping("/excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		pessoaRepository.deleteById(idpessoa); // deleta o id passado por parametro

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa"); // retorna pra essa view
		andView.addObject("pessoas", pessoaRepository.findAll()); // retorna todos passando a lista atual (apos delete)
		andView.addObject("pessoaObj", new Pessoa()); // retorna obj vazio, limpo na tela(form)

		return andView;
	}

	// metodo de download é sempre void
	@GetMapping("**/baixarcurriculo/{idpessoa}")
	public void baixarcurriculo(@PathVariable("idpessoa") Long idpessoa, HttpServletResponse response)
			throws IOException {

		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();

		if (pessoa.getCurriculo() != null) { // verificando se a pessoa tem arquivo salvo

			response.setContentLength(pessoa.getCurriculo().length); // passando o tamanho da resposta

			response.setContentType(pessoa.getTipoFileCurriculo()); // passando o tipo do arquivo

			// define cabecalho da resposta
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			response.setHeader(headerKey, headerValue);

			response.getOutputStream().write(pessoa.getCurriculo()); // passa o arquivo
		}
		
	}

	@PostMapping("**/pesquisarpessoa") 
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
			@RequestParam("pesquisasexo") String pesquisasexo, @PageableDefault(size = 5, sort = {"nome"})Pageable pageable) { 

		Page<Pessoa> pessoas = null; 

		if (pesquisasexo != null && !pesquisasexo.isEmpty()) { 
			pessoas = pessoaRepository.findPessoaBySexoPage(nomepesquisa, pesquisasexo, pageable); // faz a busca com nome e sexo
		
		} else { // se nao, busca somente nome
			pessoas = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
		}
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa"); // retorna pra essa view
		andView.addObject("pessoas", pessoas); // adc no objeto pessoas o que foi buscado e retorna a lista
		andView.addObject("pessoaObj", new Pessoa());
		andView.addObject("nomepesquisa", nomepesquisa);
		
		return andView;
		
	}
	@GetMapping("/telefones/{idpessoa}") // intercepta dados enviados pela url metodo editar e id
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) { // tratar e retornar para a view

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa); // carrega objeto pessoa do servidor buscando pelo id

		ModelAndView andView = new ModelAndView("cadastro/telefones"); // retorna para tela de telefones

		andView.addObject("pessoaObj", pessoa.get()); // passa o objeto pessoa pelo parametro pessoaObj
		andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));

		return andView;

	}

	@PostMapping("**/addfonePessoa/{pessoaid}") // recebe id pela url e aciona o metodo
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) { // injeta no objeto
																									// e passa o
																									// parametro
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get(); // busca todo o objeto pessoa
		telefone.setPessoa(pessoa); // entra com o idpessoa vindo por parametro

		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {

			ModelAndView andView = new ModelAndView("cadastro/telefones");
			andView.addObject("pessoaObj", pessoa);
			andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));

			List<String> msg = new ArrayList<String>();

			if (telefone.getNumero().isEmpty() || telefone.getNumero() == null) {
				msg.add("Numero deve ser informado");
			}
			if (telefone.getTipo().isEmpty() || telefone.getTipo() == null) {
				msg.add("Tipo deve ser informado");
			}

			andView.addObject("msg", msg);

			return andView;

		} else {

			telefoneRepository.save(telefone); // salva os atributos que estao no objeto telefone

			ModelAndView andView = new ModelAndView("cadastro/telefones");
			andView.addObject("pessoaObj", pessoa); // retorna para a view com o objeto pai
			andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid)); // recebe o idpessoa e passa para
																						// o repository

			return andView;
		}

	}

	@GetMapping("/removertelefone/{idtelefone}") // links no front sempre chama pela url
	public ModelAndView removertelefone(Telefone telefone, @PathVariable("idtelefone") Long idtelefone) {

		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa(); // busca objeto pessoa

		telefoneRepository.deleteById(idtelefone);

		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaObj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId())); // passa para o repository o id
																							// do telefone

		return andView;
	}

	// metodo que aciona a geração do relatorio, passa os parametros, valida.
	@GetMapping("**/pesquisarpessoa")
	public void imprimePDF(@RequestParam("nomepesquisa") String nomepesquisa,
			@RequestParam("pesquisasexo") String pesquisasexo, String sexo, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {

		List<Pessoa> pessoas = new ArrayList<Pessoa>(); // lista que o relatorio vai receber para criação

		if (pesquisasexo != null && !pesquisasexo.isEmpty() && nomepesquisa != null && !nomepesquisa.isEmpty()) { // busca por nome e sexo
			

			pessoas = pessoaRepository.findPessoaByNameandsexo(nomepesquisa, pesquisasexo);

		} else if (nomepesquisa != null && !nomepesquisa.isEmpty()) { // busca por nome

			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);

		} else if (pesquisasexo != null && !pesquisasexo.isEmpty()) { // busca por nome

			pessoas = pessoaRepository.findPessoaByName(sexo);

		} else { // busca todos
			Iterable<Pessoa> iterator = pessoaRepository.findAll();

			for (Pessoa pessoa : iterator) { // varre o iterable e add na lista o que foi recebido na condição

				pessoas.add(pessoa);
			}
		}
		// chamando o serviço que faz a geração do relatorio
		byte[] pdf = reportUtil.geraRelatorio(pessoas, "pessoa", httpServletRequest.getServletContext());

		// Tamanho da resposta do navegador
		httpServletResponse.setContentLength(pdf.length);

		// definir na resposta o tipo de arquivo
		httpServletResponse.setContentType("application/octet-stream");

		// Definindo cabeçalho da resposta
		String headkey = "Content-Disposition";
		String headerValue = String.format("attchament; filename=\"%s\"", "relatorio.pdf");
		httpServletResponse.setHeader(headkey, headerValue);

		// Finaliza resposta para o navegador
		httpServletResponse.getOutputStream().write(pdf);
	}

}
