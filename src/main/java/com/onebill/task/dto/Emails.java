package com.onebill.task.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString.Exclude;

@Data
@Entity
@Table(name = "emails_info")
public class Emails {
	
	@Id
	@GeneratedValue
	private int emailId;
	
	@Email
	@Column 
	private String email;
	
//	@MapsId
	@ManyToOne
	@JsonIgnore
	@Exclude
	@JoinColumn(name = "customerId", referencedColumnName = "customerId")
	private CustomerDetails customer;
}
