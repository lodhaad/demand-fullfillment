package com.tradeai.demandfullfillment.externalapi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DemandOutput {
	
	private Integer demandId;

	private Integer batchId;

	private String clientId;

	private String securityId;

	private Integer quantity;

	private String dateOfDemand;

	private String settlementDate;

	private Double clientDemandConversionPercentage;





}
