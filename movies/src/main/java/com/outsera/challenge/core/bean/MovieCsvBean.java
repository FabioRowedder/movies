package com.outsera.challenge.core.bean;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Classe de mapeamento do arquivo CSV
 */
public class MovieCsvBean {

	@CsvBindByName
	private Integer year;
	
	@CsvBindByName
	private String title;
	
	@CsvBindByName
	private String studios;
	
	@CsvBindByName
	private String producers;
	
	@CsvBindByName
	private String winner;
	
}
