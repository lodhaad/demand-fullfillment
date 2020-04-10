package com.tradeai.demandfullfillment.externalapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class SupplyOutput {
	
	private Integer supplyId;

	private Integer supplyGroupId;
	
	private String supplierId;
	
	private String supplyDate;
	
	private String securityCode;
	
	private Integer quantity;
	
	private Double rate;


}
