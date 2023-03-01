/**
 * 
 */
package attackGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.xml.bind.JAXBException;

import metric.SoftwareAssessment;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import systemModel.Asset;
import systemModel.JobParam;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.fibexImporter.Fibex31toModel;
import systemModel.fibexImporter.Fibex31toModelBigTest;
import systemModel.importer.ZedisXlsx;
import systemModel.importer.ZedisXlsxTest;
import systemModel.subTypes.InputType;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import exploit.Exploit;
import exploit.PotentialExploit;

/**
 * @author Martin Salfer
 *
 */
public class AttackGraphTest {

	private AttackGraph ag;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500);
		AttackScenario scenario = new AttackScenario("Unit Testing Example", new SystemModel(), attackerProfile, new HashSet<Exploit>());
		ag = new AttackGraph("Unit Testing Example", scenario);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.ag = null;
	}

	/**
	 * Test method for {@link attackGraph.AttackGraph#AttackGraph()}.
	 */
	@Test
	public final void testAttackGraphCreation() {
		SystemModel sm = ag.getSystemModel();
		
		Software t1 = new Software("Browser");
		sm.addNode(t1);
		Access a = new Access("Website injection", t1, 10_000);
		assertFalse(ag.contains(a));
		ag.addAccessNode(a);
		assertTrue(ag.contains(a));

		ag.addAgNodeFromAccess(a, new PotentialExploit("Browser Exploit", t1), t1);
		assertTrue(ag.contains(t1));
		
		Asset v1 = new Asset("Web2.0", t1);
		assertFalse(ag.contains(v1));
		ag.addNodeExplToExpl(t1, new PotentialExploit("Attractor Extraction #1", v1), v1);
		assertTrue(ag.contains(v1));
		
		Software t2 = new Software("Payment Service");
		assertFalse(ag.contains(t2));
		ag.addNodeExplToExpl(t1, new PotentialExploit("Payment Exploit", t2), t2);
		assertTrue(ag.contains(t2));
		
		Asset v2 = new Asset("In-App Payment Financial", t2);
		assertFalse(ag.contains(v2));
		ag.addNodeExplToExpl(t2, new PotentialExploit("Payment Extraction", v2), v2);
		assertTrue(ag.contains(v2));
		
		Asset v3 = new Asset("In-App Payment Reputation", t2);
		assertFalse(ag.contains(v3));
		ag.addNodeExplToExpl(t2, new PotentialExploit("Payment Reputation", v3), v3);
		assertTrue(ag.contains(v3));
	}
	
	
	/**
	 * Test method for {@link attackGraph.AttackGraph#toString()}.
	 */
	@Test
	public final void testToString() {
		SystemModel sm = ag.getSystemModel();
		
		Software t1 = new Software("Browser");
		sm.addNode(t1);
		
		Access a = new Access("Website injection", t1, 10_000);
		assertFalse(ag.contains(a));
		ag.addAccessNode(a);
		assertTrue(ag.contains(a));

		ag.addAgNodeFromAccess(a, new PotentialExploit("Browser Exploit", t1), t1);
		assertTrue(ag.contains(t1));
		
		Asset v1 = new Asset("Web2.0", t1);
		sm.addNode(v1);
		assertFalse(ag.contains(v1));
		ag.addNodeExplToExpl(t1, new PotentialExploit("Attractor Extraction #1", v1), v1);
		assertTrue(ag.contains(v1));
		
		Software t2 = new Software("Payment Service");
		sm.addNode(t2);
		assertFalse(ag.contains(t2));
		ag.addNodeExplToExpl(t1, new PotentialExploit("Payment Exploit", t2), t2);
		assertTrue(ag.contains(t2));
		
		Asset v2 = new Asset("In-App Payment Financial", t2);
		assertFalse(ag.contains(v2));
		ag.addNodeExplToExpl(t2, new PotentialExploit("Payment Extraction", v2), v2);
		assertTrue(ag.contains(v2));
		
		Asset v3 = new Asset("In-App Payment Reputation", t2);
		assertFalse(ag.contains(v3));
		ag.addNodeExplToExpl(t2, new PotentialExploit("Payment Reputation", v3), v3);
		assertTrue(ag.contains(v3));
		
		String string = ag.toString();
		System.out.println(string);
		
		assertTrue(string != null);
		assertTrue(string.length() > 100);
		
		// TODO stabilize toString() and test its output here.
	}

	/**
	 * Find out and test how suitable these types are for Bayes networks.
	 */
	@Test
	public final void testBayesSuitability() {
		SystemModel sm = ag.getSystemModel();
		
		Software s0 = new Software("Browser");
		sm.addNode(s0);
		
		Access a = new Access("Website injection", s0, 10_000);
		assertFalse(ag.contains(a));
		ag.addAccessNode(a);
		assertTrue(ag.contains(a));

		ag.addAgNodeFromAccess(a, new PotentialExploit("Browser Exploit", s0), s0);
		assertTrue(ag.contains(s0));
		
		Asset v1 = new Asset("Web2.0", s0);
		sm.addNode(v1);
		assertFalse(ag.contains(v1));
		ag.addNodeExplToExpl(s0, new PotentialExploit("Attractor Extraction #1", v1), v1);
		assertTrue(ag.contains(v1));
		
		
		
		Software s1 = new Software("Inter1");
		sm.addNode(s1);
		assertFalse(ag.contains(s1));
		ag.addNodeExplToExpl(s0, new PotentialExploit("Payment Exploit", s1), s1);
		assertTrue(ag.contains(s1));
		
		Software s2 = new Software("Inter2");
		sm.addNode(s2);
		assertFalse(ag.contains(s2));
		ag.addNodeExplToExpl(s0, new PotentialExploit("Payment Exploit", s2), s2);
		assertTrue(ag.contains(s2));
		
		Software s3 = new Software("Inter3");
		sm.addNode(s3);
		assertFalse(ag.contains(s3));
		ag.addNodeExplToExpl(s0, new PotentialExploit("Payment Exploit", s3), s3);
		assertTrue(ag.contains(s3));
		
		
		
		Software s4 = new Software("Payment Service");
		sm.addNode(s4);
		assertFalse(ag.contains(s4));
		ag.addNodeExplToExpl(s1, new PotentialExploit("Payment Exploit", s4), s4);
		ag.addNodeExplToExpl(s2, new PotentialExploit("Payment Exploit", s4), s4);
		ag.addNodeExplToExpl(s3, new PotentialExploit("Payment Exploit", s4), s4);
		assertTrue(ag.contains(s4));
		
		Asset v2 = new Asset("In-App Payment Financial", s4);
		assertFalse(ag.contains(v2));
		ag.addNodeExplToExpl(s4, new PotentialExploit("Payment Extraction", v2), v2);
		assertTrue(ag.contains(v2));
		
		Asset v3 = new Asset("In-App Payment Reputation", s4);
		assertFalse(ag.contains(v3));
		ag.addNodeExplToExpl(s4, new PotentialExploit("Payment Reputation", v3), v3);
		assertTrue(ag.contains(v3));
		
		String string = ag.toString();
		System.out.println(string);
		
		assertTrue(string != null);
		assertTrue(string.length() > 100);
		
		// TODO stabilize toString() and test its output here.
	}

	
}
