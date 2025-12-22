package com.outsera.challenge.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBeanBuilder;
import com.outsera.challenge.converter.MovieConverter;
import com.outsera.challenge.core.bean.MovieCsvBean;
import com.outsera.challenge.repository.MovieRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
/**
 * Classe responsável por fazer a leitura dos dados no CSV e persistir no banco.
 */
public class Startup {
	
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieConverter movieConverter;
	
	@Value("${outsera.challenge.csv.location}")
	private String csvLocation;
	
	@PostConstruct
	public void init() {
		try {
			List<MovieCsvBean> beans = getCsvBeans();
			persistRecords(beans);
			log.info("CSV file loaded.");
		} catch (FileNotFoundException fnfe) {
			log.error("CSV file not found on " + csvLocation);
		} catch (Exception e) {
			log.error("Generic error: " + e.getMessage());
		}
	}
	
	private void persistRecords(List<MovieCsvBean> beans) {
		movieRepository.saveAll(movieConverter.from(beans));
	}
	
	private List<MovieCsvBean> getCsvBeans() throws FileNotFoundException {
		return new CsvToBeanBuilder<MovieCsvBean>(
				new FileReader(csvLocation))
				.withType(MovieCsvBean.class)
				.withSeparator(';')
				.build()
				.parse();	
	}
}
