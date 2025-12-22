package com.outsera.challenge.builder;

import com.outsera.challenge.out.MoviesOutDTO;
import com.outsera.challenge.out.ProducerIntervalOutDTO;

public class MoviesOutDTOBuilder {

	private MoviesOutDTO instance;

	public static MoviesOutDTOBuilder getBuilder() {
		MoviesOutDTOBuilder builder = new MoviesOutDTOBuilder();
		builder.setInstance(new MoviesOutDTO());
		return builder;
	}
	
	public MoviesOutDTOBuilder addMin(ProducerIntervalOutDTO producerIntervalOutDTO) {
		this.instance.getMin().add(producerIntervalOutDTO);
		return this;
	}
	
	public MoviesOutDTOBuilder addMax(ProducerIntervalOutDTO producerIntervalOutDTO) {
		this.instance.getMax().add(producerIntervalOutDTO);
		return this;
	}
	
	private void setInstance(MoviesOutDTO instance) {
		this.instance = instance;
	}
	
	public MoviesOutDTO getInstance() {
		return this.instance;
	}
}
