package com.outsera.challenge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.outsera.challenge.entity.Movie;
import com.outsera.challenge.out.MoviesOutDTO;
import com.outsera.challenge.out.ProducerIntervalOutDTO;
import com.outsera.challenge.repository.MovieRepository;
import com.outsera.challenge.service.bo.AwardIntervalBO;
import com.outsera.challenge.service.bo.ProducerAwardsBO;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Value("${outsera.challenge.csv.producer.names.splitter.regex}")
	private String producerNamesSplitterRegex;
	
	/**
	 * Rotina principal para processamento e descoberta dos produtores desejados
	 * @return
	 */
	public MoviesOutDTO getAwardsIntervals() {
		MoviesOutDTO moviesOutDTO = new MoviesOutDTO();
		
		List<Movie> movies = movieRepository.findAllWinnersOrderedByYear();
		if (CollectionUtils.isEmpty(movies)) return moviesOutDTO;
		
		List<ProducerAwardsBO> producerAwardsList = extractGroupedProducers(movies);
		if (CollectionUtils.isNotEmpty(producerAwardsList)) {
			TreeMap<Integer, List<ProducerAwardsBO>> intervalsMap = mapMoviesByInterval(producerAwardsList);
			
			if (CollectionUtils.isNotEmpty(producerAwardsList)) {
				fillFastestProducer(moviesOutDTO, intervalsMap.firstEntry().getValue());
				fillSlowestProducer(moviesOutDTO, intervalsMap.lastEntry().getValue());
			}	
		}
		
		return moviesOutDTO;
	}

	/**
	 * Método que retorna um Map indexado pelos intervalos de recebimento de prêmios.
	 * @param producerAwardsList
	 * @return
	 */
	private TreeMap<Integer, List<ProducerAwardsBO>> mapMoviesByInterval(List<ProducerAwardsBO> producerAwardsList) {
		TreeMap<Integer, List<ProducerAwardsBO>> intervalsMap = new TreeMap<>();
		
		for (ProducerAwardsBO producerAwardsBO : producerAwardsList) {
			Integer interval = producerAwardsBO.getLowestInterval();
			
			List<ProducerAwardsBO> producers = null;
			
			if (!intervalsMap.containsKey(interval)) {
				producers = new ArrayList<>();
				intervalsMap.put(interval, producers);
			}
			
			intervalsMap.get(interval).add(producerAwardsBO);
		}
		return intervalsMap;
	}

	/**
	 *  Método que popula os dados dos produtores que menos demoraram para receber um prêmio.
	 *  Se mais de 1 produtor tiver o "mesmo melhor" intervalo entre prêmios, ambos os produtores serão retornados.
	 * @param moviesOutDTO
	 * @param producerAwardsList
	 */
	private void fillFastestProducer(MoviesOutDTO moviesOutDTO, List<ProducerAwardsBO> producerAwardsList) {
		Integer interval = producerAwardsList.get(0).getLowestInterval();
		
		for (ProducerAwardsBO producerAwardsBO : producerAwardsList) {
			for (AwardIntervalBO awardIntervalBO : producerAwardsBO.getYearIntervalsMappedByCount().get(interval)) {
				moviesOutDTO.getMin().add(new ProducerIntervalOutDTO(producerAwardsBO.getProducer(), 
																	 producerAwardsBO.getLowestInterval(), 
																	 awardIntervalBO.getPreviousWin(), 
																	 awardIntervalBO.getFollowingWin()));
			}
		}
	}
	
	/**
	 *  Método que popula os dados dos produtores que mais demoraram para receber um prêmio.
	 *  Se mais de 1 produtor tiver o "mesmo pior" intervalo entre prêmios, ambos os produtores serão retornados.
	 * @param moviesOutDTO
	 * @param producerAwardsList
	 */
	private void fillSlowestProducer(MoviesOutDTO moviesOutDTO, List<ProducerAwardsBO> producerAwardsList) {
		Integer interval = producerAwardsList.get(producerAwardsList.size() - 1).getLowestInterval();
		
		for (ProducerAwardsBO producerAwardsBO : producerAwardsList) {
			for (AwardIntervalBO awardIntervalBO : producerAwardsBO.getYearIntervalsMappedByCount().get(interval)) {
				moviesOutDTO.getMax().add(new ProducerIntervalOutDTO(producerAwardsBO.getProducer(), 
																	 producerAwardsBO.getLowestInterval(), 
																	 awardIntervalBO.getPreviousWin(), 
																	 awardIntervalBO.getFollowingWin()));
			}	
		}
	}

	/**
	 * Método que retorna uma lista com todos os produtores e respectivos intervalos de recebimento de prêmios, ordenada do menor ao maior intervalo entre prêmios.
	 * Os produtores serão considerados individualmente, mesmo que o filme tenha mais de 1 produtor.
	 * Produtores que receberam apenas 1 prêmio serão desconsiderados pois não é possível calcular o intervalo entre prêmios. 
	 * @param movies
	 * @return
	 */
	private List<ProducerAwardsBO> extractGroupedProducers(List<Movie> movies) {
		Map<String, ProducerAwardsBO> producerMoviesMap = new TreeMap<>();
		
		for (Movie movie : movies) {
			for (String producer : movie.getProducers().split(producerNamesSplitterRegex)) {
				String producerName = producer.trim();
				
				if (!producerMoviesMap.containsKey(producerName)) {
					producerMoviesMap.put(producerName, new ProducerAwardsBO(producerName));
				}
				
				producerMoviesMap.get(producerName).addYear(movie.getYear());
			}
		}
		
		List<ProducerAwardsBO> producerAwardsList = new ArrayList<>(producerMoviesMap.values());
		producerAwardsList.removeIf(p -> !p.hasLeast2Awards());
		
		if (CollectionUtils.isNotEmpty(producerAwardsList)) {
			Collections.sort(producerAwardsList);
		}
		
		return producerAwardsList;
	}
}
