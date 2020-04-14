package com.tradeai.demandfullfillment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.tradeai.demandfullfillment.dto.DemandFullfillmentDTO;
import com.tradeai.demandfullfillment.dto.OverAllSupplyDTO;
import com.tradeai.demandfullfillment.externalapi.DemandInput;
import com.tradeai.demandfullfillment.externalapi.DemandOutput;
import com.tradeai.demandfullfillment.externalapi.InventoryOutput;
import com.tradeai.demandfullfillment.externalapi.SupplyOutput;
import com.tradeai.demandfullfillment.input.DemandFullfillmentInput;
import com.tradeai.demandfullfillment.output.DemandFullfillmentOutput;
import com.tradeai.demandfullfillment.service.DemandFullfillmentService;

@RestController
@RequestMapping("/demand-fullfillment")
public class DemandFullfillmentController {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private RestTemplate template;

	@Autowired
	private DemandFullfillmentService service;

	public void processTheFlow() {

	}

	@PostMapping("/client/{clientId}/date/{businessDate}")
	public ResponseEntity<List<DemandFullfillmentOutput>> getDemandForFullFillment(
			@Valid @RequestBody List<DemandFullfillmentInput> listOfRequests, @PathVariable String clientId,
			@PathVariable String businessDate) throws ParseException {




		List<DemandFullfillmentDTO> inputs = new ArrayList<>();
		
		
		List<DemandFullfillmentDTO> fullfillment = new ArrayList<>();
		
		
		

		for (DemandFullfillmentInput element : listOfRequests) {

			DemandFullfillmentDTO dto = new DemandFullfillmentDTO();
			dto.setBusinessDate(businessDate);
			dto.setClientId(clientId);
			dto.setSecurityId(element.getSecurityId());
			dto.setQuantity(element.getQuantity());
			inputs.add(dto);

		}

		List<DemandInput> demandInput = service.createDemandInput(inputs);

		List<DemandOutput> demandOutput = saveDemandViaRestService(demandInput, clientId, businessDate);
		
		
		
		
		
		for (DemandOutput output : demandOutput ) { 
			
			List<OverAllSupplyDTO> totalAvailablity = new ArrayList<>();
			
			List<SupplyOutput> supplies = getAllSuppliesForStockViaService(output.getSecurityId(), businessDate);
			
			/// get the best for this quantity

			supplies.forEach(supplyElementFromSupplier -> {

				OverAllSupplyDTO supplyelement = new OverAllSupplyDTO();
				supplyelement.setSecurityId(supplyElementFromSupplier.getSecurityCode());
				supplyelement.setQuantiyAvailable(supplyElementFromSupplier.getQuantity());
				supplyelement.setRate(supplyElementFromSupplier.getRate());
				supplyelement.setSource("E");
				supplyelement.setSourceId(supplyElementFromSupplier.getSupplyId());
				totalAvailablity.add(supplyelement);

			});
			
			
			
			List<InventoryOutput> inventory = getInventoryViaRest(output.getSecurityId(), businessDate);
			
			inventory.forEach(inventoryElement -> {

				OverAllSupplyDTO supplyelement = new OverAllSupplyDTO();
				supplyelement.setSecurityId(inventoryElement.getStockId());
				supplyelement.setQuantiyAvailable(inventoryElement.getQuantity());
				supplyelement.setRate(inventoryElement.getRateCharged());
				supplyelement.setSource("I");
				supplyelement.setSourceId(inventoryElement.getInventoryId());
				totalAvailablity.add(supplyelement);

			});
			
			///sort by rate 
			
			
			List<OverAllSupplyDTO> sortedList = totalAvailablity.stream()
					.sorted(Comparator.comparingDouble(OverAllSupplyDTO::getRate))
					.collect(Collectors.toList());
			
			
			
			
			////remove the one that is used
			
			

			Integer totalQuantity = totalAvailablity.stream().collect(Collectors.summingInt(OverAllSupplyDTO::getQuantiyAvailable));
			
			Integer quantityToFullfill = output.getQuantity();
			
			
			if (quantityToFullfill  < totalQuantity ) {
				
				
				///// logic needs to be build from here 
				
				//// doesnt seem right 
				
				/////we need to make sure this is running 
				
				////deploy in cloud 
				
				
				////using variables 
				
				

				Integer quantiy = quantityToFullfill;

				for (OverAllSupplyDTO totalAvailal : sortedList) {

					if (quantiy > totalAvailal.getQuantiyAvailable()) {

						DemandFullfillmentDTO demandFullfillDTO = new DemandFullfillmentDTO();

						demandFullfillDTO.setDemandId(output.getDemandId());
						demandFullfillDTO.setClientId(clientId);
						demandFullfillDTO.setSecurityId(output.getSecurityId());
						demandFullfillDTO.setSourceId(totalAvailal.getSourceId());
						demandFullfillDTO.setBusinessDate(output.getDateOfDemand());
						demandFullfillDTO.setQuantity(totalAvailal.getQuantiyAvailable());
						demandFullfillDTO.setSourceOfDemandHeld(totalAvailal.getSource());
						demandFullfillDTO.setStatus("Good");

						quantiy = quantiy - quantityToFullfill;
						fullfillment.add(demandFullfillDTO);

					} else {

						DemandFullfillmentDTO demandFullfillDTO = new DemandFullfillmentDTO();

						demandFullfillDTO.setDemandId(output.getDemandId());
						demandFullfillDTO.setClientId(clientId);
						demandFullfillDTO.setSourceId(totalAvailal.getSourceId());
						demandFullfillDTO.setSecurityId(output.getSecurityId());
						demandFullfillDTO.setBusinessDate(output.getDateOfDemand());
						demandFullfillDTO.setQuantity( quantiy);
						demandFullfillDTO.setSourceOfDemandHeld(totalAvailal.getSource());
						demandFullfillDTO.setStatus("Good");

						///quantiy = quantityToFullfill - quantiy;
						fullfillment.add(demandFullfillDTO);

						break;

					}

				}

			}

			else {

				DemandFullfillmentDTO demandFullfillDTO = new DemandFullfillmentDTO();
				
				demandFullfillDTO.setDemandId(output.getDemandId());
				demandFullfillDTO.setClientId(clientId);


				demandFullfillDTO.setSecurityId(output.getSecurityId());
				demandFullfillDTO.setBusinessDate(output.getDateOfDemand());
				demandFullfillDTO.setQuantity(output.getQuantity());
				demandFullfillDTO.setStatus("Not Good");
				fullfillment.add(demandFullfillDTO);

				
				

			}


			
			
		}
		
		
		
		
		
		

		List<DemandFullfillmentDTO> output = service.fullfillDemand(fullfillment);
		
		List<DemandFullfillmentOutput> responseFullfillment = new ArrayList<>();
		
		for (DemandFullfillmentDTO dto : output) {
			
			DemandFullfillmentOutput outputObj = new DemandFullfillmentOutput();
			outputObj.setDemandId(dto.getDemandId());
			outputObj.setClientId(dto.getClientId());
			outputObj.setSecurityId(dto.getSecurityId());
			outputObj.setQuantity(dto.getQuantity());
			outputObj.setStatus(dto.getStatus());
			responseFullfillment.add(outputObj);
			
		}

		

		return new ResponseEntity<List<DemandFullfillmentOutput>>(responseFullfillment, HttpStatus.OK);

	}

	private List<DemandOutput> saveDemandViaRestService(List<DemandInput> demandInput, String clientId,
			String businessDate) {

		String theUrl = "http://localhost:81/demand/client/" + clientId + "/date/" + businessDate;

		HttpEntity<List<DemandInput>> requestEntity = new HttpEntity<>(demandInput);

		ResponseEntity<List<DemandOutput>> response = template.exchange(theUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<List<DemandOutput>>() {
				});

		List<DemandOutput> demandoutput = response.getBody();

		return demandoutput;

	}

	private List<SupplyOutput> getAllSuppliesForStockViaService(String secId, String dateOfDemand) {

		String supplyUrl = "http://localhost:83/supply/security/" + secId + "/date/" + dateOfDemand;

		ResponseEntity<List<SupplyOutput>> supplyResponse = template.exchange(supplyUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SupplyOutput>>() {
				});

		System.out.println();

		List<SupplyOutput> supplies = supplyResponse.getBody();

		return supplies;
	}

	private List<InventoryOutput> getInventoryViaRest(String secId, String dateOfDemand) {

		String inventoryURL = "http://localhost:84/inventory/stock/" + secId + "/date/" + dateOfDemand;

		ResponseEntity<List<InventoryOutput>> inventoryResp = template.exchange(inventoryURL, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<InventoryOutput>>() {
				});

		List<InventoryOutput> inventory = inventoryResp.getBody();

		return inventory;
	}

}
