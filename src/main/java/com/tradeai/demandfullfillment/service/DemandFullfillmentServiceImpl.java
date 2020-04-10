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
import com.tradeai.demandfullfillment.dto.OverAllSupplyDTO;
import com.tradeai.demandfullfillment.externalapi.DemandInput;
import com.tradeai.demandfullfillment.externalapi.DemandOutput;
import com.tradeai.demandfullfillment.externalapi.InventoryOutput;
import com.tradeai.demandfullfillment.externalapi.SupplyOutput;
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
		
		List<DemandFullfillmentDTO> fullfillment = new ArrayList<>();

		List<DemandInput> demand = new ArrayList<DemandInput>();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		String clientId = null;
		String dateOfDemand = null;
		

		for (DemandFullfillmentDTO input : demandFullfillment) {
			
			DemandInput demandInput = new DemandInput();
			demandInput.setSecurityId(input.getSecurityId());
			demandInput.setClientId(input.getClientId());
			
			clientId = input.getClientId(); 
			dateOfDemand = format.format(input.getBusinessDate());
			
			demandInput.setDateOfDemand(format.format(input.getBusinessDate()));
			demandInput.setClientDemandConversionPercentage(1.0d);
			demandInput.setSettlementDate(format.format(input.getBusinessDate()));
			demandInput.setQuantity(input.getQuantity());
			
			demand.add(demandInput);
		}
		
		
		

		String theUrl = "http://localhost:81/demand/client/"+clientId+"/date/"+dateOfDemand;

		HttpEntity<List<DemandInput>> requestEntity = new HttpEntity<>(demand);

		ResponseEntity<List<DemandOutput>> response = template.exchange(theUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<List<DemandOutput>>() {
				});
		
		
		List<DemandOutput> demandoutput = response.getBody();



		
		
		demandoutput.forEach(element -> {
			
			
			/// get the supply for the stock
			/// get the supplier supply probablity
			/// reduce the fill for the stock that we have blocked per previous demand
			/// sort by rate - lowest as first 
			
			
			String supplyUrl = "http://localhost:83/supply/security/" + element.getSecurityId()+"/date/"
					+ element.getDateOfDemand();
			
			ResponseEntity<List<SupplyOutput>> supplyResponse = template.exchange(supplyUrl, HttpMethod.GET, null ,
					new ParameterizedTypeReference<List<SupplyOutput>>() {
					});
			
			
			System.out.println();
			
			List<SupplyOutput> supplies = supplyResponse.getBody();
			
			/// get the inventory for this stock
			
			String inventoryURL = "http://localhost:84/inventory/stock/" + element.getSecurityId() +
					"/date/"+element.getDateOfDemand();
			



			ResponseEntity<List<InventoryOutput>> inventoryResp = template.exchange(inventoryURL, HttpMethod.GET, null ,
					new ParameterizedTypeReference<List<InventoryOutput>>() {
					});
			
			
			List<InventoryOutput> inventory = inventoryResp.getBody();
			
			
			List<OverAllSupplyDTO> totalAvailablity = new ArrayList<>();
			
			inventory.forEach(inventoryElement -> {
				
				OverAllSupplyDTO supplyelement = new OverAllSupplyDTO();
				supplyelement.setSecurityId(inventoryElement.getStockId());
				supplyelement.setQuantiyAvailable(inventoryElement.getQuantity());
				supplyelement.setRate(inventoryElement.getRateCharged());
				supplyelement.setSource("I");
				supplyelement.setSourceId(inventoryElement.getInventoryId());
				totalAvailablity.add(supplyelement);
				
			});
			
			///get the best for this quantity 
			
			supplies.forEach(supplyElementFromSupplier -> {
				
				OverAllSupplyDTO supplyelement = new OverAllSupplyDTO();
				supplyelement.setSecurityId(supplyElementFromSupplier.getSecurityCode());
				supplyelement.setQuantiyAvailable(supplyElementFromSupplier.getQuantity());
				supplyelement.setRate(supplyElementFromSupplier.getRate());
				supplyelement.setSource("E");
				supplyelement.setSourceId(supplyElementFromSupplier.getSupplyId());
				totalAvailablity.add(supplyelement);
				
			});
			
			
			DemandFullfillmentDTO demandFullfillDTO = new DemandFullfillmentDTO();
			
			demandFullfillDTO.setDemandId(element.getPostionId());
			
			if (totalAvailablity.size() > 0 ) {
				demandFullfillDTO.setStatus("Good");
			}
			
			else {
				demandFullfillDTO.setStatus("Not Good");
			}
			
			///get the cliant probablity 
			
			/// put a fill for this 
			
			//create the list of status
			
			fullfillment.add(demandFullfillDTO);
			
			
		});
		
		
		



		
		




		
		
		/// respond as GOOD NO GOOD status as list of Demand

		return fullfillment;
	}

}
