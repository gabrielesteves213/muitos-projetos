package br.com.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.model.Usuario;
import br.com.springboot.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class GreetingsController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/users")
	@ResponseBody
	public ResponseEntity<List<Usuario>> getAllUsers(){
		
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
		
	}
	
	@PostMapping("/users")
	@ResponseBody
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario usuario){
		
		Usuario user = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuario, HttpStatus.CREATED);
		
	}
	
	
	@DeleteMapping("/deleteuser")
	public ResponseEntity<String> deleteUser(@RequestParam Long idUser){
		
		usuarioRepository.deleteById(idUser);
		
		return new ResponseEntity<String>("Deletado com sucesso", HttpStatus.OK);
		
	}
	
	@GetMapping("/buscarporid")
	public ResponseEntity<Usuario> getUserById(@RequestParam(name = "idUser") Long idUser){
		
		Usuario usuario = usuarioRepository.findById(idUser).get();
		
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<?> updateUser(@RequestBody Usuario usuario ){
		
		if(usuario.getId()==null) {
			return new ResponseEntity<String>("ID não informado",HttpStatus.BAD_REQUEST);
		}else {
			Usuario user = usuarioRepository.saveAndFlush(usuario);
			return new ResponseEntity<Usuario>(user, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/buscarpornome")
	public ResponseEntity<?> findByName(@RequestParam(name = "nome") String nome){
		
		List<Usuario> listaPorNome = usuarioRepository.findByName(nome.trim().toUpperCase());
		
		if(listaPorNome.size() != 0) {
			return new ResponseEntity<List<Usuario>>(listaPorNome, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Nenhum dado com esse nome", HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	
	
}
