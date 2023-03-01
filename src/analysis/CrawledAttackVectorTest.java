/**
 * 
 */
package analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import attackGraph.AttackScenario;
import attackerProfile.AttackerProfile;
import exploit.Exploit;
import exploit.PotentialExploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;

/**
 * @author Martin Salfer
 * @created 2016-04-09
 *
 */
public class CrawledAttackVectorTest {
	private SystemModel sysModel;
	private AttackerProfile attackerProfile;
	private HashSet<Exploit> exploits;
	private AttackScenario scenario;
	private Software s1;
	private Software s2;
	private Software s3;
	private PotentialExploit exploit1;
	private PotentialExploit exploit3;
	private PotentialExploit exploit6;
	private Crawlable c1;
	private Crawlable c2;
	private Crawlable c3;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sysModel = new SystemModel();
		attackerProfile = new AttackerProfile();
		exploits = new HashSet<Exploit>();
		
		scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);

		s1 = new Software("Browser");
		s2 = new Software("Payment Service");
		s3 = new Software("Unrelated Service");
		sysModel.addNode(s1);
		sysModel.addNode(s2);
		sysModel.addNode(s3);
		
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		sysModel.addNode(dBus);
		s1.registerReadsWritesOn(dBus);
		s2.registerReadsWritesOn(dBus);
		s3.registerReadsWritesOn(dBus);
		
		
		Asset v1 = new Asset("Web2.0", s1);
		Asset v2 = new Asset("In-App Payment Financial", s2);
		Asset v3 = new Asset("In-App Payment Reputation", s2);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		
		exploit1 = new PotentialExploit("Browser Exploit", s1, new Resources(10_000d, 0d));
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1, new Resources(10_000d, 0d));
		exploit3 = new PotentialExploit("Payment Exploit", s2, new Resources(50_000d, 0d));
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2, new Resources(10_000d, 0d));
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3, new Resources(10_000d, 0d));
		exploit6 = new PotentialExploit("Unrelated Exploit1", s3, new Resources(20_000d, 0d));
		final PotentialExploit exploit7 = new PotentialExploit("Unrelated Exploit2", s3, new Resources(20_000d, 0d));
		final PotentialExploit exploit8 = new PotentialExploit("Unrelated Exploit3", s3, new Resources(20_000d, 0d));
		final PotentialExploit exploit9 = new PotentialExploit("Unrelated Exploit4", s3, new Resources(20_000d, 0d));
		final PotentialExploit exploit10 = new PotentialExploit("Unrelated Exploit5", s3, new Resources(20_000d, 0d));
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);
		exploits.add(exploit7);
		exploits.add(exploit8);
		exploits.add(exploit9);
		exploits.add(exploit10);
		
		c1 = new CrawledSoftware(scenario, s1);
		c2 = new CrawledSoftware(scenario, s2);
		c3 = new CrawledSoftware(scenario, s3);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		sysModel = null;
		attackerProfile = null;
		exploits = null;
		scenario= null;
		s1 = null;
		s2 = null;
		s3 = null;
		exploit1 = null;
		c1 = null;
		c2 = null;
		c3 = null;
	}
	
	
	@Test
	public final void testEquals() {
		CrawledAttackVector cav1 = new CrawledAttackVector(c1, exploit1);
		CrawledAttackVector cav2 = new CrawledAttackVector(c1, exploit1);
		CrawledAttackVector cav3 = new CrawledAttackVector(c1, exploit1);
		
		assertTrue(cav2.equals(cav1));
		assertTrue(cav3.equals(cav1));
		assertTrue(cav1.equals(cav2));
		assertFalse(cav1.equals(null));
		assertFalse(cav1.equals(c1));
		
		
		Set<CrawledAttackVector> set = new HashSet<CrawledAttackVector>(3);
		set.add(cav1);
		set.add(cav2);
		set.add(cav3);
		assertEquals(1, set.size());
	}

}
