package br.com.movieapp.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.movieapp.model.Movie;
import br.com.movieapp.repository.MovieRepository;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	
	@Transactional
	public List<Movie> getAllMovies(){
		List<Movie> movieList = new ArrayList<Movie>();
		movieRepository.findAll().forEach(movieList::add);
		return movieList;
		
	}
	
	@Transactional
	public Movie saveMovie(Movie movie) {
		return movieRepository.save(movie);
	}


}
