package com.tradeai.demandfullfillment.service;

import java.sql.Date;
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

import com.tradeai.demandfullfillment.datamodel.DemandFullfillment;
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
		
		List<DemandFullfillment> df = new ArrayList<>();
		
		Integer fullfillmentId = repository.getFullfillmentId();
		
		if (fullfillmentId == null) {
			fullfillmentId = 0; 
		}
		
		for (DemandFullfillmentDTO dto :demandFullfillment  ) {
			fullfillmentId = fullfillmentId + 1 ; 
			DemandFullfillment dfDBObj = new DemandFullfillment();
			dfDBObj.setDemandFullfillId(fullfillmentId );
			dfDBObj.setBusinessDate(Date.valueOf(dto.getBusinessDate()));
			dfDBObj.setSourceId(dto.getSourceId());
			dfDBObj.setSource(dto.getSourceOfDemandHeld());
			dfDBObj.setClientId(dto.getClientId());
			dfDBObj.setSecurityId(dto.getSecurityId());
			dfDBObj.setQuantity(dto.getQuantity());
			dfDBObj.setDemandId(dto.getDemandId());
			dfDBObj.setStatus(dto.getStatus());
			df.add(dfDBObj);
		}

		repository.saveAll(df);

		return demandFullfillment;
	}

	@Override
	public List<DemandInput> createDemandInput(List<DemandFullfillmentDTO> demandFullfillment) {

		List<DemandInput> demand = new ArrayList<DemandInput>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String clientId = null;
		String dateOfDemand = null;

		for (DemandFullfillmentDTO input : demandFullfillment) {

			DemandInput demandInput = new DemandInput();
			demandInput.setSecurityId(input.getSecurityId());
			demandInput.setClientId(input.getClientId());

			clientId = input.getClientId();
			dateOfDemand = input.getBusinessDate();

			demandInput.setDateOfDemand(dateOfDemand);
			demandInput.setClientDemandConversionPercentage(1.0d);
			demandInput.setSettlementDate(dateOfDemand);
			demandInput.setQuantity(input.getQuantity());

			demand.add(demandInput);
		}

		return demand;
	}

}
