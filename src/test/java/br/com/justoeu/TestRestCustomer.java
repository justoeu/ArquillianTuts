package br.com.justoeu;

import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.justoeu.domain.Customer;
import br.com.justoeu.resource.CustomerResource;
import br.com.justoeu.resource.config.JaxRsActivator;
import br.com.justoeu.resource.impl.CustomerResourceImpl;
import br.com.justoeu.service.dto.CustomerDTO;

@RunWith(Arquillian.class)
public class TestRestCustomer {

	private static final String NAME = "Valmir";

	private static final GenericType<List<Customer>> CUSTOMER_LIST_TYPE = new GenericType<List<Customer>>() {};
	
	//@OverProtocol("Servlet 3.0")
	@Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "ArquillianTuts.war")
                .addClasses(CustomerResource.class, 
                			CustomerResourceImpl.class, 
                			Customer.class, 
                			CustomerDTO.class, 
                			JaxRsActivator.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

	@Inject
	CustomerResource customerResource;
	
	@Test
	public void testGetCustomers() throws Exception{
		List<Customer> listCustomers =  customerResource.getAllCustomers();
		assertCustomerList(listCustomers);
	}
	
	@RunAsClient
    @Test
    public void testGetAllClient(@ArquillianResource URL deploymentURL) throws Exception {
        WebTarget target = buildWebTarget(deploymentURL);
        List<Customer> customers = target.path("rest/customer").request(MediaType.APPLICATION_JSON).get(CUSTOMER_LIST_TYPE);
        Assert.assertTrue(customers.size() >= 2);
    }

	@RunAsClient
    @Test
    public void testPostBanClient(@ArquillianResource URL deploymentURL) throws Exception {
        WebTarget target = buildWebTarget(deploymentURL);
        Customer customer = target.path("rest/customer/ban/2")
        						  .request(MediaType.APPLICATION_JSON)
        						  .post(Entity.json(""),Customer.class);
        Assert.assertTrue(customer.isBanned());
    }

	@RunAsClient
    @Test
    public void testPostCreateClient(@ArquillianResource URL deploymentURL) throws Exception {
		Customer newCustomer = new Customer();
		newCustomer.setBanned(false);
		newCustomer.setId(3l);
		newCustomer.setName("Teste de Criacao");
		
        WebTarget target = buildWebTarget(deploymentURL);
        Customer customer = target.path("rest/customer")
        						  .request(MediaType.APPLICATION_JSON)
        						  .post(
        								 Entity.entity(newCustomer, MediaType.APPLICATION_JSON)
        								 ,Customer.class
        						   );
        Assert.assertTrue(customer.getName().equals("Teste de Criacao"));
    }
	
	@RunAsClient
    @Test
    public void testGetContactAsClient(@ArquillianResource URL deploymentURL) throws Exception {
        WebTarget target = buildWebTarget(deploymentURL);
        //List<Customer> customers = target.path("rest/customer/1").request(MediaType.APPLICATION_JSON).get(CUSTOMER_LIST_TYPE);
        Customer customer = target.path("rest/customer/1").request(MediaType.APPLICATION_JSON).get(Customer.class);
        Assert.assertNotNull(customer);
    }
	
	private static WebTarget buildWebTarget(URL deploymentURL) throws URISyntaxException {
        Client client = ClientBuilder.newClient();
        return client.target(deploymentURL.toURI());
    }

	
	private void assertCustomerList(List<Customer> customer) {
		boolean check = false;
		for (Customer current : customer) {
			if (!check && current.getName().contains(NAME)){
				check = true;
				System.out.println("Current Name: " + current.getName());
				continue;
			}
		}
		assertTrue(check);
    }
	
}