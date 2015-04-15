package br.com.justoeu.service.dto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import br.com.justoeu.domain.Customer;

@ApplicationScoped
public class CustomerDTO {

	public static List<Customer> customers;
	
	@PostConstruct
	public void initialize(){
		customers = new ArrayList<Customer>();
	}

	// gets and setters
	public List<Customer> getCustomers() { return customers; }
	public void setCustomer(Customer customer) { 
		customers.add(customer); 
	}
	
	public void setCustomers(List<Customer> customers){
		customers.addAll(customers);
	}
}