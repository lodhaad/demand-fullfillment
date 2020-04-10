package com.tradeai.demandfullfillment.externalapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class DemandInput {
	

	private String clientId;

	private String securityId;

	private Integer quantity;

	private String dateOfDemand;

	private String settlementDate;

	private Double clientDemandConversionPercentage;

}
