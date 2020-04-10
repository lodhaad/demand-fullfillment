package com.tradeai.demandfullfillment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tradeai.demandfullfillment.dto.DemandFullfillmentDTO;
import com.tradeai.demandfullfillment.input.DemandFullfillmentInput;
import com.tradeai.demandfullfillment.input.DemandInput;
import com.tradeai.demandfullfillment.input.DemandOutput;
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
	public void getDemandForFullFillment(@Valid @RequestBody List<DemandFullfillmentInput> listOfRequests,
			@PathVariable String clientId, @PathVariable String businessDate) throws ParseException {

		// convert to DTO
		// send to service
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(businessDate);
		

		List<DemandFullfillmentDTO> inputs = new ArrayList<>();

		for (DemandFullfillmentInput element: listOfRequests) {

			DemandFullfillmentDTO dto = new DemandFullfillmentDTO();
			dto.setBusinessDate(date);
			dto.setClientId(clientId);
			dto.setSecurityId(element.getSecurityId());
			dto.setQuantity(element.getQuantity());
			inputs.add(dto);

		}

		

		List<DemandFullfillmentDTO> output = service.fullfillDemand(inputs);
		


	}

}
