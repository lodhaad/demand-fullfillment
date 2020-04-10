package com.tradeai.demandfullfillment.dto;

import java.util.Date;
//import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class DemandFullfillmentDTO {

	private Integer demandFullfillId;

	private Integer demandId;

	private Integer positionId;

	private String clientId;

	private String sourceOfDemandHeld;

	private String status;

	private String securityId;

	private Integer quantity;

	private Date businessDate;

	private Double clientProbablity;

}
