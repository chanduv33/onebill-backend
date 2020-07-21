package com.onebill.task.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
@Table(name = "address_info")
public class Addresses {
	
	@Id
	@GeneratedValue
	private int addressId;
	
	@Column 
	private String line1;
	
	@Column 
	private String line2;
	
	@Column 
	private String country;
	
	@Column 
	private String state;
	
	@Column 
	private String city;
	
	@Column 
	private int pincode;
	
//	@MapsId
	@ManyToOne
	@JsonIgnore
	@Exclude
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	private CustomerDetails customer;
}
