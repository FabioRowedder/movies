package com.outsera.challenge.out;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoviesOutDTO {

	@JsonProperty("min")
	private List<ProducerIntervalOutDTO> min = new ArrayList<>();
	
	@JsonProperty("max")
	private List<ProducerIntervalOutDTO> max = new ArrayList<>();
	
	@JsonIgnore
	public boolean isEmpty() {
		return CollectionUtils.isEmpty(min) && CollectionUtils.isEmpty(max);
	}
}
