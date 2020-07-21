package com.onebill.task.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
@Table(name = "mobileNumbers_info")
public class MobileNumbers {
	
	@Id
	@GeneratedValue
	private int mobileId;
	
	@Column 
	private Long mobileNumber;
	
//	@MapsId
	
	
	@ManyToOne
	@JsonIgnore
	@Exclude
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	private CustomerDetails customer;
}
