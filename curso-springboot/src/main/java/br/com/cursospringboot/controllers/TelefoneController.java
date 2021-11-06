package br.com.cursospringboot.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.cursospringboot.model.Pessoa;
import br.com.cursospringboot.model.Telefone;
import br.com.cursospringboot.repository.PessoaRepository;
import br.com.cursospringboot.repository.TelefoneRepository;

@Controller
public class TelefoneController {
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@PostMapping("/cadastrartelefone/{pessoaid}")
	public ModelAndView cadastrarTelefone(@PathVariable(name = "pessoaid")Long pessoaid,@Valid Telefone telefone,BindingResult bindingResult)
	{
		ModelAndView modelAndView = new ModelAndView("cadastro/telefone");
		Pessoa pessoaPorId = pessoaRepository.findById(pessoaid).get();
		if(bindingResult !=  null) {
			if(bindingResult.hasErrors()) {
				List<String> msgErrors = new ArrayList<String>();
				for (ObjectError objectError : bindingResult.getAllErrors()) {
					msgErrors.add(objectError.getDefaultMessage());
				}
				modelAndView.addObject("msgErrors",msgErrors);
			}else {
				if(telefone != null) {
					telefone.setPessoa(pessoaPorId);
					telefoneRepository.save(telefone);
				}
			}
		}
		modelAndView.addObject("pessoa",pessoaPorId);
		modelAndView.addObject("telefones",telefoneRepository.getTelefonesById(pessoaid));
		return modelAndView;
	}
	
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removerTelefone(@PathVariable(name = "idtelefone")Long idtelefone){
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		
		telefoneRepository.deleteById(idtelefone);
		System.out.println(pessoa.getId());
		return cadastrarTelefone(pessoa.getId(), null,null);
	}
}
