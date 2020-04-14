package com.tradeai.demandfullfillment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tradeai.demandfullfillment.datamodel.DemandFullfillment;

public interface DemandFullfillmentRepository extends CrudRepository<DemandFullfillment, Integer> {
	
	@Query("select max(demandFullfillId) from DemandFullfillment")
	public Integer getFullfillmentId();

}
