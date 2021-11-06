package br.com.cursospringboot.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import br.com.cursospringboot.model.Pessoa;
import br.com.cursospringboot.model.Profissao;
import br.com.cursospringboot.repository.PessoaRepository;
import br.com.cursospringboot.repository.ProfissaoRepository;
import br.com.cursospringboot.repository.TelefoneRepository;
import br.com.cursospringboot.util.ReportUtil;

@Controller
public class PessoaController {

	@Autowired
	private ReportUtil reportUtil;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;

	@RequestMapping(value = "**/salvarpessoa",method = RequestMethod.POST,consumes = {"multipart/form-data"})
	public ModelAndView salvarPessoa(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file) throws IOException {
		
		if(pessoa.getId()!= null && pessoa.getId() > 0) {
			pessoa.setTelefone(telefoneRepository.getTelefonesById(pessoa.getId()));
			if(file.getSize() > 0) {
				pessoa.setNomeArquivoCurriculo(file.getOriginalFilename());
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoArquivoCurriculo(file.getContentType());
			}else {
				Pessoa pessoaTemp = pessoaRepository.getById(pessoa.getId());
				pessoa.setCurriculo(pessoaTemp.getCurriculo());
				pessoa.setNomeArquivoCurriculo(pessoa.getNomeArquivoCurriculo());
				pessoa.setTipoArquivoCurriculo(pessoa.getTipoArquivoCurriculo());
			}
		}else {
			if(file.getSize()>0) {
				pessoa.setNomeArquivoCurriculo(file.getOriginalFilename());
				pessoa.setCurriculo(file.getBytes());
				pessoa.setTipoArquivoCurriculo(file.getContentType());
			}
		}
		
		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas", new ArrayList<>());
			modelAndView.addObject("pessoa", pessoa);
			List<String> msgsError = new ArrayList<String>();
			for (ObjectError objectError : bindingResult.getAllErrors()) {
				msgsError.add(objectError.getDefaultMessage());
				System.out.println(msgsError);
			}
			modelAndView.addObject("msgsError", msgsError);
			return modelAndView;
		} else {
			
			for (int i = 0; i < 30; i++) {
				pessoaRepository.saveAndFlush(pessoa);

			}
			
			return mostrarPessoas();
		}
	}
	
	@GetMapping("/pessoapag")
	public ModelAndView pessoaPorPaginacao(@PageableDefault(size = 5) Pageable pageable,@RequestParam(name = "nomepesquisa")String nomepesquisa,@RequestParam(name="sexopessoa")String sexopessoa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		List<Profissao> listaProfissao = profissaoRepository.findAll();

		Page<Pessoa> listaPessoas = null;
		
		if(nomepesquisa.trim().equalsIgnoreCase("")&&sexopessoa.trim().equalsIgnoreCase("")) {
			listaPessoas = pessoaRepository.findAll(pageable);
		}else if(nomepesquisa.trim()!=""&&sexopessoa.trim()==""){
			listaPessoas = pessoaRepository.findPessoaByNome(nomepesquisa, pageable);
		}else if(nomepesquisa.trim()=="" && sexopessoa.trim()!=""){
			listaPessoas = pessoaRepository.findPessoaBySexo(sexopessoa, pageable);
		}else {
			if(nomepesquisa != null && nomepesquisa
					.trim() != "" && !(sexopessoa.equalsIgnoreCase("todos"))) {
				listaPessoas = pessoaRepository.findPessoaByNomeAndSexo(nomepesquisa, sexopessoa, pageable);
			}else if(nomepesquisa != null && nomepesquisa.trim() != "" &&(sexopessoa.equalsIgnoreCase("todos"))) {
				listaPessoas = pessoaRepository.findPessoaByNome(nomepesquisa, pageable);
			}else if(nomepesquisa == null || nomepesquisa.trim()== "" &&  !(sexopessoa.equalsIgnoreCase("todos"))) {
				listaPessoas = pessoaRepository.findPessoaBySexo(sexopessoa, pageable);
			}
		}
		
		modelAndView.addObject("nomepesquisa",nomepesquisa);
		modelAndView.addObject("pessoas", listaPessoas);
		modelAndView.addObject("pessoa", new Pessoa());
		modelAndView.addObject("profissao",listaProfissao);
		return modelAndView;
		
	}
	
	
	
	@GetMapping("**/cadastropessoa/**")
	public ModelAndView mostrarPessoas() {
		Page<Pessoa> listaPessoas = pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome")));
		List<Profissao> listaProfissoes = profissaoRepository.findAll();
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", listaPessoas);
		modelAndView.addObject("pessoa", new Pessoa());
		modelAndView.addObject("profissao",listaProfissoes);
		return modelAndView;
	}

	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editarPessoa(@PathVariable("idpessoa") Long idpessoa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		if(pessoa.getProfissao()==null) {
			List<Profissao> listaProfissoes = profissaoRepository.findAll();
			modelAndView.addObject("profissao", listaProfissoes);
		}
		modelAndView.addObject("pessoa", pessoa);
		return modelAndView;
	}

	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView removerPessoa(@PathVariable(name = "idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		return mostrarPessoas();
	}

	@PostMapping("**/mostrarpessoaspornome")
	public ModelAndView mostrarPessoaPorNome(@RequestParam(name = "nome") String nome,@RequestParam(name="sexo") String sexo,@PageableDefault(size=5,sort = {"nome"})Pageable pageable) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		Page<Pessoa> listaPessoas = null;
		
		if(nome != null && nome.trim() != "" && !(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findPessoaByNomeAndSexo(nome, sexo, pageable);
		}else if(nome != null && nome.trim() != "" &&(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findPessoaByNome(nome, pageable);
		}else if(nome == null || nome.trim()== "" &&  !(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findPessoaBySexo(sexo, pageable);
		}
		modelAndView.addObject("pessoas", listaPessoas);
		modelAndView.addObject("pessoa", new Pessoa());
		modelAndView.addObject("nomepesquisa", nome);
		return modelAndView;
	}

	@GetMapping("/verpessoa/{idpessoa}")
	public ModelAndView verPessoa(@PathVariable(name = "idpessoa") Long idpessoa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/telefone");
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		modelAndView.addObject("pessoa", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefonesById(idpessoa));
		return modelAndView;
	}
	
	@GetMapping("**/mostrarpessoaspornome")
	public void gerarRelatorioPessoa(@RequestParam(name = "nome") String nome,@RequestParam(name="sexo") String sexo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		System.out.println("entrou");
		
		List<Pessoa> listaPessoas = new ArrayList<Pessoa>();
		
		if(nome != null && nome.trim() != "" && !(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findByNomeAndSexo(nome, sexo);
		}else if(nome != null && nome.trim() != "" &&(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findByNome(nome);
		}else if(nome == null || nome.trim()== "" &&  !(sexo.equalsIgnoreCase("todos"))) {
			listaPessoas = pessoaRepository.findBySexo(sexo);
		}else {
			listaPessoas = pessoaRepository.findAll();
		}
		
		byte[] pdf = reportUtil.gerarRelatorio(listaPessoas, "pessoa", request.getServletContext());
		
		response.setContentLength(pdf.length);
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		
		response.setHeader(headerKey, headerValue);
		
		response.getOutputStream().write(pdf); 	
	}
	
	
	
	@GetMapping("/baixarcurriculo/{idpessoa}")
	public void baixarCurriculo(@PathVariable(name="idpessoa")Long idpessoa, HttpServletResponse response ) throws IOException {
		
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		
		if(pessoa.getCurriculo()!=null) {
			response.setContentLength(pessoa.getCurriculo().length);
			response.setContentType(pessoa.getTipoArquivoCurriculo());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeArquivoCurriculo());
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(pessoa.getCurriculo());
		}
		
		
	}
	
}
