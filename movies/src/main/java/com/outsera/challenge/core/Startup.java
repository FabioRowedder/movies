package com.outsera.challenge.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBean;
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
	
	@PostConstruct
	public void init() {
		try {
			List<MovieCsvBean> beans = getCsvBeans();
			persistRecords(beans);
			log.info("CSV file loaded.");
		} catch (Exception e) {
			log.error("Generic error on CSV loading: " + e.getMessage());
		}
	}
	
	private List<MovieCsvBean> getCsvBeans() throws IOException {
        Reader reader = new InputStreamReader(
        		new ClassPathResource("data/Movielist.csv").getInputStream(), StandardCharsets.UTF_8);

        CsvToBean<MovieCsvBean> csvToBean = new CsvToBeanBuilder<MovieCsvBean>(reader)
            .withType(MovieCsvBean.class)
            .withSeparator(';')
            .withIgnoreLeadingWhiteSpace(true)
            .build();

        return csvToBean.parse();
	}
	
	private void persistRecords(List<MovieCsvBean> beans) {
		movieRepository.saveAll(movieConverter.from(beans));
	}
}
