package com.outsera.challenge.builder;

import com.outsera.challenge.out.ProducerIntervalOutDTO;

public class ProducerIntervalOutDTOBuilder {

	private ProducerIntervalOutDTO instance;

	public static ProducerIntervalOutDTOBuilder getBuilder() {
		ProducerIntervalOutDTOBuilder builder = new ProducerIntervalOutDTOBuilder();
		builder.setInstance(new ProducerIntervalOutDTO());
		return builder;
	}
	
	private void setInstance(ProducerIntervalOutDTO instance) {
		this.instance = instance;
	}
	
	public ProducerIntervalOutDTOBuilder producer(String producer) {
		this.instance.setProducer(producer);
		return this;
	}
	
	public ProducerIntervalOutDTOBuilder interval(Integer interval) {
		this.instance.setInterval(interval);
		return this;
	}
	
	public ProducerIntervalOutDTOBuilder previousWin(Integer previousWin) {
		this.instance.setPreviousWin(previousWin);
		return this;
	}
	
	public ProducerIntervalOutDTOBuilder followingWin(Integer followingWin) {
		this.instance.setFollowingWin(followingWin);
		return this;
	}
	
	public ProducerIntervalOutDTO getInstance() {
		return this.instance;
	}
}
