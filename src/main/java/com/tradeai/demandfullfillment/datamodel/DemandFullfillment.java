package com.tradeai.demandfullfillment.datamodel;

import java.sql.Date;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter 

@Entity
@Table(name = "demand_fullfillment", schema = "demand")
public class DemandFullfillment {

	@Id
	@Column(name = "demand_fullfillment_id")
	private Integer demandFullfillId;

	@Column(name = "demand_id")
	private Integer demandId;

	@Column(name = "client_id")
	private String clientId;

	/// this is the source of fullfillment if good
	@Column(name = "source")
	private String source;
	
	@Column(name = "source_id")
	private Integer sourceId;

	// @Column (name = "request_time")
	// private Timestamp requestTime;

	@Column(name = "status")
	private String status;

	@Column(name = "security_id")
	private String securityId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "business_date")
	private Date businessDate;

	@Column(name = "client_probablity")
	private Double clientProbablity;

}
