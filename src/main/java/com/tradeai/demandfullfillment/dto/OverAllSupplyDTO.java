package com.tradeai.demandfullfillment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OverAllSupplyDTO {
	
	private String securityId;
	private Integer quantiyAvailable;
	private String source;
	private Integer sourceId;
	private Double rate;

}
