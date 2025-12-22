package com.outsera.challenge.service.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AwardIntervalBO {
	
	private Integer previousWin;
	
	private Integer followingWin;
	
	public Integer getIntervalCount() {
		if (previousWin != null && followingWin != null) {
			return followingWin - previousWin;
		} else {
			return null;
		}
	}
}
