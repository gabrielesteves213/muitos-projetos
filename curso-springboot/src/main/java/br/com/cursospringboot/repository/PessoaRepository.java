package br.com.cursospringboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cursospringboot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query(value="select p from Pessoa p where upper(trim(p.nome)) like %?1%")
	List<Pessoa> findByNome(String nome);
	
	@Query(value="select p from Pessoa p where p.nome like %?1% and p.sexo = ?2 ")
	List<Pessoa> findByNomeAndSexo(String nome, String sexo);

	@Query(value="select p from Pessoa p where p.sexo = ?1")
	List<Pessoa> findBySexo(String sexo);
	
	default Page<Pessoa> findPessoaByNome(String nome, Pageable pageable){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher(nome, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		Page<Pessoa > pessoas = findAll(example, pageable);
		
		return pessoas;
		
	}
	
	default Page<Pessoa> findPessoaBySexo(String sexo, Pageable pageable){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(sexo);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher(sexo, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		Page<Pessoa > pessoas = findAll(example, pageable);
		
		return pessoas;
		
	}
	
	default Page<Pessoa> findPessoaByNomeAndSexo(String nome,String sexo, Pageable pageable){
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher(nome, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()).withMatcher(sexo, GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		Page<Pessoa > pessoas = findAll(example, pageable);
		
		return pessoas;
		
	}
	
	
	
}
