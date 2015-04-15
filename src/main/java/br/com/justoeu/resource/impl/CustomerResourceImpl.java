package br.com.justoeu.resource.impl;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import br.com.justoeu.domain.Customer;
import br.com.justoeu.resource.CustomerResource;
import br.com.justoeu.service.dto.CustomerDTO;

public class CustomerResourceImpl implements CustomerResource {

    private static final boolean ADD_MORE_ELEMENTS = true;
    
	@Context
    private HttpServletRequest httpServletRequest;

    @Inject
    private CustomerDTO customerDTO;
    
    @Override
    public Customer createCustomer(Customer customer){
    	customerDTO.setCustomer(customer);
        return customer;
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return findAllCustomers(ADD_MORE_ELEMENTS);
    }

    @Override
    public Customer getCustomerById(@PathParam("id") long id) {
        return findCustomerById(id);
    }

    @Override
    public Customer banCustomer(long id) {
        final Customer customer = findCustomerById(id);
        if (null == customer) {
            return null;
        }
        customer.setBanned(true);
        return customer;
    }

	private List<Customer> findAllCustomers(boolean add) {
        List<Customer> customers = customerDTO.getCustomers();
        if (add){
	        customers.add(new Customer(customers.size()+1, "JustSystems - " + nextId()));
	        customers.add(new Customer(customers.size()+1, "Valmir - " + nextId()));
        }        
        return customers;
    }

    private Customer findCustomerById(long id) {
        for (Customer customer : findAllCustomers(!ADD_MORE_ELEMENTS)) {
            if (id == customer.getId()) {
                return customer;
            }
        }
        return null;
    }

    private long nextId() {
        long newValue = new Random().nextLong();
        return newValue;
    }
}