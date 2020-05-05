package com.tradeai.demandfullfillment.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tradeai.demandfullfillment.datamodel.DemandFullfillment;

public interface DemandFullfillmentRepository extends CrudRepository<DemandFullfillment, Integer> {
	
	@Query("select max(demandFullfillId) from DemandFullfillment")
	public Integer getFullfillmentId();
	
	public DemandFullfillment findByClientIdAndSecurityIdAndBusinessDateAndStatus(String clientId, String SecId, Date settlementDate,String status) ;

	public DemandFullfillment findByClientIdAndSecurityIdAndStatus(String clientId, String SecId, String status) ;
}
