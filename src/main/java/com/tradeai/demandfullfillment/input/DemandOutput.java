package com.tradeai.demandfullfillment.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DemandOutput {
	
	private Integer batchId;

	private Integer postionId;

	private String clientId;

	private String securityId;

	private Long quantity;

	private String dateOfDemand;

	private String settlementDate;

	private Double clientDemandConversionPercentage;



}
