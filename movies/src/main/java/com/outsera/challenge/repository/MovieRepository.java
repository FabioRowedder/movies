package com.outsera.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.outsera.challenge.entity.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

	@Query("SELECT m FROM Movie m where m.winner = true order by year")
	List<Movie> findAllWinnersOrderedByYear();
	
}
