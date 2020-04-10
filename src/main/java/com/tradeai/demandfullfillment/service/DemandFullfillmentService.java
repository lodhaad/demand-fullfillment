package com.tradeai.demandfullfillment.service;

import java.util.List;

import com.tradeai.demandfullfillment.dto.DemandFullfillmentDTO;



public interface DemandFullfillmentService {
	
	public List<DemandFullfillmentDTO> fullfillDemand(List<DemandFullfillmentDTO> demandFullfillment);

}
