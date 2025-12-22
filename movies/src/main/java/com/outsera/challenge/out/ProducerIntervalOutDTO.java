package com.outsera.challenge.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerIntervalOutDTO {

	@JsonProperty("producer")
	private String producer;
	
	@JsonProperty("interval")
	private Integer interval;
	
	@JsonProperty("previousWin")
	private Integer previousWin;
	
	@JsonProperty("followingWin")
	private Integer followingWin;
	
}
