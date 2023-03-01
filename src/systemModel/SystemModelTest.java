/**
 * 
 */
package systemModel;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Salfer
 *
 */
public class SystemModelTest {

	SystemModel systemModel;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		systemModel = new SystemModel("Salfer Proposal Example");
		
		/**
		 * Create ECUs.
		 */
		ECU headUnit = new ECU("Head Unit");
		systemModel.addNode(headUnit);
		
		/**
		 * Create Software.
		 */
		Software browser = new Software("Browser");
		Software paymentService = new Software("Payment Service");
		Software kernel = new Software("Kernel");
		headUnit.addSoftware(browser);
		headUnit.addSoftware(paymentService);
		headUnit.addSoftware(kernel);
		
		/**
		 * Create Attractors.
		 */
		Asset web20Credentials = new Asset("Web 2.0 Credentials Financial", browser);
		Asset inAppPaymentReputation = new Asset("In-App Payment Reputation Attractor", paymentService);
		Asset inAppPaymentFinancial = new Asset("In-App Payment Financial Attractor", paymentService);
		browser.registerAsset(web20Credentials);
		paymentService.registerAsset(inAppPaymentReputation);
		paymentService.registerAsset(inAppPaymentFinancial);
		
		/** 
		 * Connect the Nodes.
		 */
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		browser.registerReadsWritesOn(dBus);
		paymentService.registerReadsWritesOn(dBus);
		
		
		
		HashSet<Software> tasks = new HashSet<Software>();
		tasks.add(browser);
		tasks.add(paymentService);
		tasks.add(kernel);
//		Capability ring0 = new Capability("Ring0 Control");
//		ring0.grantControlOver(tasks);

//		kernel.grant(ring0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		systemModel = null;
	}

	/**
	 * Test method for {@link systemModel.SystemModel#SystemModel()}.
	 */
	@Test
	public void testSystemModel() {
		System.out.println(systemModel.toString());
		org.junit.Assert.assertNotNull(systemModel);
	}


	
}
