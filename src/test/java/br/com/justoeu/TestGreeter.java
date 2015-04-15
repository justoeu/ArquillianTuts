package br.com.justoeu;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.justoeu.domain.Greeter;
import br.com.justoeu.domain.Phrase;

@RunWith(Arquillian.class)
public class TestGreeter {

	@Inject
	Greeter greeter;
	
	
	@Deployment
	public static JavaArchive createDeployment(){
		
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
									.addClasses(Greeter.class, Phrase.class)
									.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		System.out.println(jar.toString(true));
		return jar;
	}
	
	@Test
    public void should_create_greeting() {
        Assert.assertEquals("Hello, Valmir!", greeter.createGreeting("Valmir") );
        greeter.greet(System.out, "Valmir");
    }
	
}