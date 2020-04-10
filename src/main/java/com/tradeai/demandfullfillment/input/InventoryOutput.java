package com.tradeai.demandfullfillment.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InventoryOutput {
	
	private Integer inventoryId;	
	private String stockId;
	private Double rateCharged;
	private Integer quantity; 
	private String businessDate;
	private String sourceDesk;
	private Integer fillId;

}
