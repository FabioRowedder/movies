package com.outsera.challenge.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.outsera.challenge.core.bean.MovieCsvBean;
import com.outsera.challenge.entity.Movie;

@Component
/**
 * Converter CSV bean -> entity
 */
public class MovieConverter {

	public Movie from (MovieCsvBean movieCsvBean) {
		return Movie.builder()
				.producers(movieCsvBean.getProducers())
				.studios(movieCsvBean.getStudios())
				.title(movieCsvBean.getTitle())
				.winner(StringUtils.isNotBlank(movieCsvBean.getWinner()) ? movieCsvBean.getWinner().equalsIgnoreCase("yes") : false)
				.year(movieCsvBean.getYear())
				.build();
	}
	
	public List<Movie> from (List<MovieCsvBean> beans) {
		List<Movie> movies = new ArrayList<>();
		
		for (MovieCsvBean bean : beans) {
			movies.add(from(bean));
		}
		
		return movies;
	}
}
