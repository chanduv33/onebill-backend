package com.onebill.task.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
@Table(name = "documents_info")
public class CustomerDocuments {
	
	@Id
	@GeneratedValue
	private int documentId;
	
	@Lob
	private byte[] addressProof;
	
	@Lob
	private byte[] identityProof;
	
	@Column
	private String addressProofFileName;
	
	@Column
	private String identityProofFileName;


	@OneToOne
	@JsonIgnore
	@Exclude
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	private CustomerDetails customer;
}
