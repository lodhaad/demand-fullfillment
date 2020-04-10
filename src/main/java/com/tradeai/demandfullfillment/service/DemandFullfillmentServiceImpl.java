package com.tradeai.demandfullfillment.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tradeai.demandfullfillment.dto.DemandFullfillmentDTO;
import com.tradeai.demandfullfillment.input.DemandInput;
import com.tradeai.demandfullfillment.input.DemandOutput;
import com.tradeai.demandfullfillment.input.InventoryOutput;
import com.tradeai.demandfullfillment.input.SupplyOutput;
import com.tradeai.demandfullfillment.repository.DemandFullfillmentRepository;

@Service
public class DemandFullfillmentServiceImpl implements DemandFullfillmentService {

	@Autowired
	private DemandFullfillmentRepository repository;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RestTemplate template;

	@Override
	public List<DemandFullfillmentDTO> fullfillDemand(List<DemandFullfillmentDTO> demandFullfillment) {

		List<DemandInput> demand = new ArrayList<DemandInput>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		

		for (DemandFullfillmentDTO input : demandFullfillment) {
			
			DemandInput demandInput = new DemandInput();
			demandInput.setSecurityId(input.getSecurityId());
			demandInput.setClientId(input.getClientId());
			demandInput.setDateOfDemand(format.format(input.getBusinessDate()));
			demandInput.setClientDemandConversionPercentage(1.0d);
			demandInput.setSettlementDate(format.format(input.getBusinessDate()));
			demandInput.setQuantity(input.getQuantity());
			
			demand.add(demandInput);
		}

		String theUrl = "http://localhost:81/demand/client/ananya/date/2019-01-03";

		HttpEntity<List<DemandInput>> requestEntity = new HttpEntity<>(demand);

		ResponseEntity<List<DemandOutput>> response = template.exchange(theUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<List<DemandOutput>>() {
				});
		
		
		System.out.println(response.getBody());

		/// save in demand and get the id's of demand and position
		/// get the demand and store it in the demand service
		/// get the demand Id and position id keep it
		
		String supplyUrl = "http://localhost:83/supply/security/TCS/date/2019-01-03";
		



		ResponseEntity<List<SupplyOutput>> supplyResponse = template.exchange(supplyUrl, HttpMethod.GET, null ,
				new ParameterizedTypeReference<List<SupplyOutput>>() {
				});
		
		
		System.out.println();
		
		List<SupplyOutput> supplies = supplyResponse.getBody();
		
		

		/// get the supply for the stock
		/// get the supplier supply probablity
		/// reduce the fill for the stock that we have blocked per previous demand
		/// sort by rate - lowest as first 
		

		String inventoryURL = "http://localhost:84/inventory/stock/TCS/date/2019-01-03";
		



		ResponseEntity<List<InventoryOutput>> inventoryResp = template.exchange(inventoryURL, HttpMethod.GET, null ,
				new ParameterizedTypeReference<List<InventoryOutput>>() {
				});
		
		
		List<InventoryOutput> inventory = inventoryResp.getBody();

		/// get the inventory for this stock
		/// reduce the usage for the stock blocked by previous demand

		//// get the client fill probablity and multiply with demand
		
		////get existing fills for inventory and supply 
		
		///caculate the actual position 
		
		//// create new supply inventory list 

		/// fill either inventory or supplier as used along with demand Id

		/// get the best rate postion
		
		///insert the fills into fill service 
		
		
		/// respond as GOOD NO GOOD status

		return null;
	}

}
