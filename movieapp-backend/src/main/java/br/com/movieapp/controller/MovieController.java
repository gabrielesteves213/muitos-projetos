package br.com.movieapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.movieapp.model.Movie;
import br.com.movieapp.service.MovieService;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/api")
public class MovieController {
	
	@Autowired
	private MovieService movieService;

	@GetMapping("/movies")
	private ResponseEntity<?> getAllMovies(){
		try {
			return new ResponseEntity<>(movieService.getAllMovies(),HttpStatus.OK);
		} catch (Exception e) {
			return errorResponse();
		}
	}
		
	
	
	@PostMapping("/movies")
	private ResponseEntity<?> saveNewMovie(@RequestBody Movie movie){
		System.out.println(movie.getTitulo());
		try {
			return new ResponseEntity<>(movieService.saveMovie(movie),HttpStatus.OK);
		} catch (Exception e) {
			return errorResponse();
		}
	}
	
	
	
	private ResponseEntity<?> errorResponse(){
		return new ResponseEntity<>("Algo deu errado", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
