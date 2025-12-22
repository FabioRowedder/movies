package com.outsera.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.outsera.challenge.out.MoviesOutDTO;
import com.outsera.challenge.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@GetMapping("/producers")
	public ResponseEntity<MoviesOutDTO> getPrizeRanges() {
		MoviesOutDTO moviesOutDTO = movieService.getAwardsIntervals();
		
		if (!moviesOutDTO.isEmpty()) {
			return ResponseEntity.ok(moviesOutDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
