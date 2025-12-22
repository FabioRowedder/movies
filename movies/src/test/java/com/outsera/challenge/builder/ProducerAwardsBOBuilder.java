package com.outsera.challenge.builder;

import com.outsera.challenge.service.bo.ProducerAwardsBO;

public class ProducerAwardsBOBuilder {

	private ProducerAwardsBO instance;
	
//	private static ProducerAwardsBOBuilder builder;
	
	public static ProducerAwardsBOBuilder getBuilder() {
		ProducerAwardsBOBuilder builder = new ProducerAwardsBOBuilder();
		builder.setInstance(new ProducerAwardsBO());
		return builder;
	}
	
	private void setInstance(ProducerAwardsBO instance) {
		this.instance = instance;
	}
	
	public ProducerAwardsBOBuilder producer(String producer) {
		instance.setProducer(producer);
		return this;
	}
	
	public ProducerAwardsBOBuilder addYear(Integer year) {
		instance.addYear(year);
		return this;
	}
	
	public ProducerAwardsBO getInstance() {
		return instance;
	}
	
}
