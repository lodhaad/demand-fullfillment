package com.tradeai.demandfullfillment.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class DemandFullfillmentDTO {

	
	private Integer fullfillmentId;
	
	
	private Integer sourceId;
	
	private String sourceType;
		
	/// for which demand
	private Integer demandId;


	private String clientId;

	private String sourceOfDemandHeld;

	private String status;

	private String securityId;

	private Integer quantity;

	private Date businessDate;

	private Double clientProbablity;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public void setBusinessDate(String busienssDate) throws ParseException {
		
		businessDate = sdf.parse(busienssDate);
		
	}
	
	public String getBusinessDate() {
		return sdf.format(businessDate);
	}

}
