package com.onebill.task.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

	private String message;
	private List<CustomerDetails> customers;
	private Set<String> errors;
	
	public Response(String message) {
		this.message = message;
	}
	
	public Response() {
	}
	
}
