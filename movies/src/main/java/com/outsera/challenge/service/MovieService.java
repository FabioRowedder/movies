package com.outsera.challenge.service;

import java.util.ArrayList;
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
		
		return extractGroupedProducers(movies);
	}

	/**
	 * Método que retorna uma lista com todos os produtores e respectivos intervalos de recebimento de prêmios, ordenada do menor ao maior intervalo entre prêmios.
	 * Os produtores serão considerados individualmente, mesmo que o filme tenha mais de 1 produtor.
	 * Produtores que receberam apenas 1 prêmio serão desconsiderados pois não é possível calcular o intervalo entre prêmios. 
	 * @param movies
	 * @return
	 */
	private MoviesOutDTO extractGroupedProducers(List<Movie> movies) {
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
		
		TreeMap<Integer, List<ProducerAwardsBO>> minIntervalProducerAwardsMap = mapMinIntervalProducers(producerAwardsList);
		TreeMap<Integer, List<ProducerAwardsBO>> maxIntervalProducerAwardsMap = mapMaxIntervalProducers(producerAwardsList);
		
		MoviesOutDTO output = new MoviesOutDTO();
		output.getMin().addAll(extractIntervalsProducers(minIntervalProducerAwardsMap.firstEntry().getValue()));
		output.getMax().addAll(extractIntervalsProducers(maxIntervalProducerAwardsMap.lastEntry().getValue()));
		
		return output;
	}

	/**
	 * Método que mapeia os intervalos de premiação dos produtores pelo menor intervalo
	 * @param producerAwardsList
	 * @return
	 */
	private TreeMap<Integer, List<ProducerAwardsBO>> mapMinIntervalProducers(List<ProducerAwardsBO> producerAwardsList) {
		TreeMap<Integer, List<ProducerAwardsBO>> minIntervalProducerAwardsMap = new TreeMap<>();
		for (ProducerAwardsBO producerAwardsBO : producerAwardsList) {
			ProducerAwardsBO minProducerAwardsBO = new ProducerAwardsBO();
			minProducerAwardsBO.setProducer(producerAwardsBO.getProducer());
			Integer lowestInterval = producerAwardsBO.getLowestInterval();
			minProducerAwardsBO.getYearIntervalsMappedByCount().put(lowestInterval, producerAwardsBO.getByInterval(lowestInterval));
			
			if (!minIntervalProducerAwardsMap.containsKey(lowestInterval)) {
				minIntervalProducerAwardsMap.put(lowestInterval, new ArrayList<>());
			}

			minIntervalProducerAwardsMap.get(lowestInterval).add(minProducerAwardsBO);
		}
		return minIntervalProducerAwardsMap;
	}

	/**
	 * Método que mapeia os intervalos de premiação dos produtores pelo maior intervalo
	 * @param producerAwardsList
	 * @return
	 */
	private TreeMap<Integer, List<ProducerAwardsBO>> mapMaxIntervalProducers(List<ProducerAwardsBO> producerAwardsList) {
		TreeMap<Integer, List<ProducerAwardsBO>> maxIntervalProducerAwardsMap = new TreeMap<>();
		for (ProducerAwardsBO producerAwardsBO : producerAwardsList) {
			ProducerAwardsBO maxProducerAwardsBO = new ProducerAwardsBO();
			maxProducerAwardsBO.setProducer(producerAwardsBO.getProducer());
			Integer highestInterval = producerAwardsBO.getHighestInterval();
			maxProducerAwardsBO.getYearIntervalsMappedByCount().put(highestInterval, producerAwardsBO.getByInterval(highestInterval));
			
			if (!maxIntervalProducerAwardsMap.containsKey(highestInterval)) {
				maxIntervalProducerAwardsMap.put(highestInterval, new ArrayList<>());
			}

			maxIntervalProducerAwardsMap.get(highestInterval).add(maxProducerAwardsBO);
		}
		return maxIntervalProducerAwardsMap;
	}

	/**
	 * Método que extrai os produtores e respectivos intervalos de premiação
	 * @param intervalProducerAwardsMap
	 * @return
	 */
	private List<ProducerIntervalOutDTO> extractIntervalsProducers(List<ProducerAwardsBO> intervalProducerAwardsMap) {
		List<ProducerIntervalOutDTO> producersIntervalOutDTO = new ArrayList<>();
		
		for (ProducerAwardsBO producerAwardsBO : intervalProducerAwardsMap) {
			for (List<AwardIntervalBO> awardIntervalsBO : producerAwardsBO.getYearIntervalsMappedByCount().values()) {
				for (AwardIntervalBO awardIntervalBO : awardIntervalsBO) {
					ProducerIntervalOutDTO pioDTO = new ProducerIntervalOutDTO();
					pioDTO.setProducer(producerAwardsBO.getProducer());
					pioDTO.setInterval(producerAwardsBO.getLowestInterval());
					pioDTO.setPreviousWin(awardIntervalBO.getPreviousWin());
					pioDTO.setFollowingWin(awardIntervalBO.getFollowingWin());
					producersIntervalOutDTO.add(pioDTO);	
				}
			} 
		}
		
		return producersIntervalOutDTO;
	}
}
