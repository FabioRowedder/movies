package com.outsera.challenge.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.challenge.builder.MoviesOutDTOBuilder;
import com.outsera.challenge.builder.ProducerAwardsBOBuilder;
import com.outsera.challenge.builder.ProducerIntervalOutDTOBuilder;
import com.outsera.challenge.entity.Movie;
import com.outsera.challenge.out.MoviesOutDTO;
import com.outsera.challenge.out.ProducerIntervalOutDTO;
import com.outsera.challenge.repository.MovieRepository;
import com.outsera.challenge.service.bo.ProducerAwardsBO;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
    private MovieRepository movieRepository;

	private static final String AWARDS_URL_MAPPING = "/movies/producers";
	
	@Test // Cenário 1
	public void only1ProducerMinAndOnly1ProducerMaxTest() throws Exception {
		// given
		MoviesOutDTO moviesOutDTOExpected = buildMoviesOutDTOwith2SimpleProducers();
		
		List<Movie> movies = List.of(
				new Movie(1, 1990, "Title 1", "Studio 1", "Joel Silver", true),
				new Movie(2, 1991, "Title 2", "Studio 2", "Joel Silver", true),
				new Movie(3, 2002, "Title 3", "Studio 3", "Matthew Vaughn", true),
				new Movie(4, 2015, "Title 4", "Studio 4", "Matthew Vaughn", true),
				new Movie(5, 2018, "Title 5", "Studio 5", "Jhon Travolta", true),
				new Movie(6, 2022, "Title 6", "Studio 6", "Jhon Travolta", true)
		);
		
		when(movieRepository.findAllWinnersOrderedByYear()).thenReturn(movies);
		
		// when - then
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(moviesOutDTOExpected)));
	}

	@Test // Cenário 2
	public void twoProducersMinAndTwoProducersMaxTest() throws Exception {
		// given
		MoviesOutDTO moviesOutDTOExpected = buildNoviesOutDTOWith2ProducersMinAnd2ProducersMax();
		
		List<Movie> movies = List.of(
				new Movie(1, 1990, "Title 1", "Studio 1", "Joel Silver", true),
				new Movie(2, 1991, "Title 2", "Studio 2", "Joel Silver", true),
				new Movie(7, 1995, "Title 7", "Studio 7", "Silvester Stallone", true),
				new Movie(8, 1996, "Title 8", "Studio 8", "Silvester Stallone", true),
				new Movie(3, 2002, "Title 3", "Studio 3", "Matthew Vaughn", true),
				new Movie(9, 2006, "Title 9", "Studio 9", "Steven Perry", true),
				new Movie(4, 2015, "Title 4", "Studio 4", "Matthew Vaughn", true),
				new Movie(5, 2018, "Title 5", "Studio 5", "Jhon Travolta", true),
				new Movie(10, 2019, "Title 10", "Studio 10", "Steven Perry", true),
				new Movie(6, 2022, "Title 6", "Studio 6", "Jhon Travolta", true)
		);
		
		when(movieRepository.findAllWinnersOrderedByYear()).thenReturn(movies);
		
		// when - then
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(moviesOutDTOExpected)));
	}

	@Test // Cenário 3
	public void only1ProducerTest() throws Exception {
		// given
		MoviesOutDTO moviesOutDTOExpected = buildMoviesOutDTOWithSameProducerMinAndMax();
		
		List<Movie> movies = List.of(
				new Movie(1, 1990, "Title 1", "Studio 1", "Joel Silver", true),
				new Movie(2, 1991, "Title 2", "Studio 2", "Joel Silver", true)		
		);

		when(movieRepository.findAllWinnersOrderedByYear()).thenReturn(movies);
		
		// when - then
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(moviesOutDTOExpected)));
	}

	@Test // Cenário 4
	public void noProducersInDatabaseTest() throws Exception {
		// given
		List<Movie> movies = new ArrayList<>();
		
		when(movieRepository.findAllWinnersOrderedByYear()).thenReturn(movies);
		
		// when - then
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test // Cenário 5
	public void onlyOneMovieforEachProducerTest() throws Exception {
		// given
		List<Movie> movies = List.of(
				new Movie(1, 1990, "Title 1", "Studio 1", "Joel Silver", true),
				new Movie(5, 2018, "Title 5", "Studio 5", "Jhon Travolta", true)
		);
		
		when(movieRepository.findAllWinnersOrderedByYear()).thenReturn(movies);
		
		// when - then
		mvc.perform(get(AWARDS_URL_MAPPING).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	private MoviesOutDTO buildMoviesOutDTOWithSameProducerMinAndMax() {
		ProducerAwardsBO pa = ProducerAwardsBOBuilder.getBuilder()
				.producer("Joel Silver")
				.addYear(1990)
				.addYear(1991)
				.getInstance();
		
		ProducerIntervalOutDTO producerIntervalOutDTO = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(pa.getProducer())
				.interval(1)
				.previousWin(1990)
				.followingWin(1991)
				.getInstance();
		
		return MoviesOutDTOBuilder.getBuilder()
				.addMin(producerIntervalOutDTO)
				.addMax(producerIntervalOutDTO)
				.getInstance();
	}
	
	private MoviesOutDTO buildMoviesOutDTOwith2SimpleProducers() {
		ProducerAwardsBO minProducerAwardsBO = ProducerAwardsBOBuilder.getBuilder().producer("Joel Silver").addYear(1990).addYear(1991).getInstance();
		ProducerAwardsBO maxProducerAwardsBO = ProducerAwardsBOBuilder.getBuilder().producer("Matthew Vaughn").addYear(2002).addYear(2015).getInstance();
		
		ProducerIntervalOutDTO minProducerIntervalOutDTO = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(minProducerAwardsBO.getProducer())
				.interval(minProducerAwardsBO.getLowestInterval())
				.previousWin(1990)
				.followingWin(1991)
				.getInstance();
		
		ProducerIntervalOutDTO maxProducerIntervalOutDTO = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(maxProducerAwardsBO.getProducer())
				.interval(maxProducerAwardsBO.getLowestInterval())
				.previousWin(2002)
				.followingWin(2015)
				.getInstance();

		return MoviesOutDTOBuilder.getBuilder()
				.addMin(minProducerIntervalOutDTO)
				.addMax(maxProducerIntervalOutDTO)
				.getInstance();
	}
	
	private MoviesOutDTO buildNoviesOutDTOWith2ProducersMinAnd2ProducersMax() {
		ProducerAwardsBO minProducerAwardsBO = ProducerAwardsBOBuilder.getBuilder().producer("Joel Silver").addYear(1990).addYear(1991).getInstance();
		ProducerAwardsBO minProducerAwardsBO2 = ProducerAwardsBOBuilder.getBuilder().producer("Silvester Stallone").addYear(1995).addYear(1996).getInstance();
		ProducerAwardsBO maxProducerAwardsBO = ProducerAwardsBOBuilder.getBuilder().producer("Matthew Vaughn").addYear(2002).addYear(2015).getInstance();
		ProducerAwardsBO maxProducerAwardsBO2 = ProducerAwardsBOBuilder.getBuilder().producer("Steven Perry").addYear(2006).addYear(2019).getInstance();
		
		

		ProducerIntervalOutDTO minProducerIntervalOutDTO = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(minProducerAwardsBO.getProducer())
				.interval(minProducerAwardsBO.getLowestInterval())
				.previousWin(1990)
				.followingWin(1991)
				.getInstance();
		
		ProducerIntervalOutDTO minProducerIntervalOutDTO2 = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(minProducerAwardsBO2.getProducer())
				.interval(minProducerAwardsBO2.getLowestInterval())
				.previousWin(1995)
				.followingWin(1996)
				.getInstance();
		
		ProducerIntervalOutDTO maxProducerIntervalOutDTO = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(maxProducerAwardsBO.getProducer())
				.interval(maxProducerAwardsBO.getLowestInterval())
				.previousWin(2002)
				.followingWin(2015)
				.getInstance();
		
		ProducerIntervalOutDTO maxProducerIntervalOutDTO2 = ProducerIntervalOutDTOBuilder.getBuilder()
				.producer(maxProducerAwardsBO2.getProducer())
				.interval(maxProducerAwardsBO2.getLowestInterval())
				.previousWin(2006)
				.followingWin(2019)
				.getInstance();
		
		
		return MoviesOutDTOBuilder.getBuilder()
				.addMin(minProducerIntervalOutDTO)
				.addMin(minProducerIntervalOutDTO2)
				.addMax(maxProducerIntervalOutDTO)
				.addMax(maxProducerIntervalOutDTO2)
				.getInstance();
	}
}


