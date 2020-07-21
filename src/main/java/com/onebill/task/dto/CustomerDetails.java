package com.onebill.task.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;


import lombok.Data;

@Data
@Entity
@Table(name = "customer_info")
public class CustomerDetails {
	
	@Id
	@GeneratedValue
	@Column
	private int customerId;
	
	@NotBlank(message = "FirstName Sld not be null")
	@Column
	private String firstName;
	
	@NotBlank(message = "LastName Sld not be null")
	@Column
	private String lastName;
	
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate dateOfBirth ;
	
	@Column
	private String status;
	
	@OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
	private List<Emails> emails = new ArrayList<Emails>();
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Addresses> addresses = new ArrayList<Addresses>();
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<MobileNumbers> phoneNumbers = new ArrayList<MobileNumbers>();
	
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private CustomerDocuments documents;
	
	public LocalDate getDateOfBirth() {
	    return this.dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
