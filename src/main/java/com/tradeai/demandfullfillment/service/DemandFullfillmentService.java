package com.tradeai.demandfullfillment.service;

import java.text.ParseException;
import java.util.List;

import com.tradeai.demandfullfillment.dto.DemandFullfillmentDTO;
import com.tradeai.demandfullfillment.externalapi.DemandInput;



public interface DemandFullfillmentService {
	
	public List<DemandFullfillmentDTO> fullfillDemand(List<DemandFullfillmentDTO> demandFullfillment);
	
	public List<DemandInput> createDemandInput(List<DemandFullfillmentDTO> demandFullfillment);
	
	public DemandFullfillmentDTO getFullfilment (String securityId, String clientId, String date) throws ParseException ; 

}
