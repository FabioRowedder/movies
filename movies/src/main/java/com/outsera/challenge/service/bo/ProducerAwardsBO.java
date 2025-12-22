package com.outsera.challenge.service.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ProducerAwardsBO implements Comparable<ProducerAwardsBO> {
	
	private String producer;

	private TreeMap<Integer, List<AwardIntervalBO>> yearIntervalsMappedByCount = new TreeMap<>();
	
	private Integer lastYearWin;

	public ProducerAwardsBO() { }
	
	public ProducerAwardsBO(String producer) {
		super();
		this.producer = producer;
	}
	
	public boolean hasLeast2Awards() {
		return yearIntervalsMappedByCount.size() > 0;
	}
	
	public void addYear(Integer year) {
		AwardIntervalBO awardIntervalBO = null;
		
		if (this.lastYearWin == null) {
			this.lastYearWin = year;
		} else {
			awardIntervalBO = new AwardIntervalBO(lastYearWin, year);
			this.lastYearWin = year;
			storeInterval(awardIntervalBO);
		}
	}
	
	public Integer getLowestInterval() {
		return yearIntervalsMappedByCount.firstKey();
	}
	
	private void storeInterval(AwardIntervalBO awardIntervalBO) {
		Integer intervalCount = awardIntervalBO.getIntervalCount();

		if (intervalCount != null) {
			List<AwardIntervalBO> awardsList = null;
			
			if (!yearIntervalsMappedByCount.containsKey(intervalCount)) {
				awardsList = new ArrayList<>();
				yearIntervalsMappedByCount.put(intervalCount, awardsList);
			} else {
				awardsList = yearIntervalsMappedByCount.get(intervalCount);
			}
			
			awardsList.add(awardIntervalBO);
		}
	}
	
	@Override
	public int compareTo(ProducerAwardsBO other) {
		return this.getLowestInterval() - other.getLowestInterval();
	}
}
