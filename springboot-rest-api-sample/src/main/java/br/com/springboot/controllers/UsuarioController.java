package br.com.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.model.Usuario;
import br.com.springboot.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/getall")
	@ResponseBody
	public ResponseEntity<?> getAllUsers(){
		
		List<Usuario> listaUsuarios = usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(listaUsuarios, HttpStatus.OK);
		
	}
	
	
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario usuario){
		
		Usuario user = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete")
	@ResponseBody
	public ResponseEntity<?> deleteUserById(@RequestParam(name = "idUser") Long idUser ){
		
		usuarioRepository.deleteById(idUser);
		
		return new ResponseEntity<String>("Deletado com sucesso", HttpStatus.OK);
		
	}
	
	@PutMapping("/update")
	@ResponseBody
	public ResponseEntity<?> updateUser(@RequestBody Usuario usuario){
		
		if(usuario.getId() !=null) {
			Usuario user = usuarioRepository.saveAndFlush(usuario);
			return new ResponseEntity<Usuario>(user, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("ID não encontrado",HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping("getbyname")
	@ResponseBody
	public ResponseEntity<?> getByName(@RequestParam(name = "nome") String nome){
		
		List<Usuario> listaUsuarioByName = usuarioRepository.getUserByName(nome);
		
		if(listaUsuarioByName.size() != 0 ) {
			return new ResponseEntity<List<Usuario>>(listaUsuarioByName,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Nenhum dado com esse nome",HttpStatus.NOT_FOUND);
		}
		
		
	}
	
}	
