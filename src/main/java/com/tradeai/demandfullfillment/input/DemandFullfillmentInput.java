package com.tradeai.demandfullfillment.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandFullfillmentInput {

	private String clientId;

	private String securityId;

	private Integer quantity;

	private String businessDate;

}
