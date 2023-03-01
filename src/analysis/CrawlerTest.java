package analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import attackerProfile.Motive;
import exploit.Exploit;
import exploit.PotentialExploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;
import unbbayes.prs.Node;
import unbbayes.prs.bn.JunctionTreeAlgorithm;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.util.extension.bn.inference.IInferenceAlgorithm;

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)

public class CrawlerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		System.gc();
	}

	@Test
	public final void testRunPhase1Crawl() {
		final SystemModel sysModel = new SystemModel();
		final AttackerProfile attackerProfile = new AttackerProfile();
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		
		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);

		Software t1 = new Software("Browser");
		Software t2 = new Software("Payment Service");
		Software t3 = new Software("Unrelated Service");
		sysModel.addNode(t1);
		sysModel.addNode(t2);
		sysModel.addNode(t3);
		
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		sysModel.addNode(dBus);
		t1.registerReadsWritesOn(dBus);
		t2.registerReadsWritesOn(dBus);
		t3.registerReadsWritesOn(dBus);
		
		
		Asset v1 = new Asset("Web2.0", t1);
		Asset v2 = new Asset("In-App Payment Financial", t2);
		Asset v3 = new Asset("In-App Payment Reputation", t2);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", t1);
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1);
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", t2);
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2);
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3);
		final PotentialExploit exploit6 = new PotentialExploit("Unrelated Exploit", t3);
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);

		
		assertEquals(Software.UNSET, (int)t1.getHopsToAssets());
		assertEquals(Software.UNSET, (int)t2.getHopsToAssets());
		assertEquals(Software.UNSET, (int)t3.getHopsToAssets());

		Crawler c = new Crawler(scenario);
		c.runPhase1Crawl();
		
		assertEquals(1, (int)t1.getHopsToAssets());
		assertEquals(1, (int)t2.getHopsToAssets());
		assertEquals(2, (int)t3.getHopsToAssets());
	}
	
	
	@Test
	public final void testCrawlAndGetAttackGraph() {
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Software t1 = new Software("Browser");
		Software t2 = new Software("Payment Service");
		Software t3 = new Software("Unrelated Service");
		sysModel.addNode(t1);
		sysModel.addNode(t2);
		sysModel.addNode(t3);
		
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		t1.registerReadsWritesOn(dBus);
		t2.registerReadsWritesOn(dBus);
		t3.registerReadsWritesOn(dBus);
		sysModel.addNode(dBus);
		
		Asset v1 = new Asset("Web2.0", t1);
		Asset v2 = new Asset("In-App Payment Financial", t2);
		Asset v3 = new Asset("In-App Payment Reputation", t2);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", t1);
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1);
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", t2);
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2);
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3);
		final PotentialExploit exploit6 = new PotentialExploit("Unrelated Exploit", t3);
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500);
		attackerProfile.addAccess(new Access("Phishing URL", t1, 10_000));
		final Motive m1 = new Motive("Financial", 2.0f);
		final Motive m2 = new Motive("Reputation", 0.1f);
		m1.desires(v1);
		m1.desires(v2);
		m2.desires(v3);
		attackerProfile.addMotivation(m1);
		attackerProfile.addMotivation(m2);
		
		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		AttackGraph ag = c.crawlAndGetAttackForest();
		
		Assert.assertNotNull(ag);
		
		assertEquals(6, ag.getNumberOfNodes());
		assertEquals(5, ag.getNumberOfEdges());
		
		System.out.println(ag);
	}
	

		/**
		 * Stress test of the crawler with a huge system model, which contains much dead end software.
		 */
		@Test
		public final void testCrawlAndGetAttackGraphWithHugeSystemModelMeshScenario_2_000() {
			testCrawlAndGetAttackGraphWithHugeSystemModelMeshScenario(2_000);
		}

		

		/**
		 * Stress test of the crawler with a huge system model, which contains much dead end software.
		 */
		@Test
		public final void testCrawlAndGetAttackGraphWithHugeSystemModelChainScenario_3_000() {
			testCrawlAndGetAttackGraphWithHugeSystemModelChainScenario(3_000);
		}

		
		
		


		
		
	/**
	 * Stress test of the crawler with a huge system model, which contains much dead end software.
	 * @param amountOfUselessSoftwareDeadEndNodes How many Software nodes are connected to the mesh?
	 */
	public static final void testCrawlAndGetAttackGraphWithHugeSystemModelMeshScenario(int amountOfUselessSoftwareDeadEndNodes) {

		/* Configuration */
		final int amountOfUselessSoftware = amountOfUselessSoftwareDeadEndNodes;
		
		
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Software t1 = new Software("Browser");
		Software t2 = new Software("Payment Service");
		Software t3 = new Software("Unrelated Service");
		List<Software> uselessSoftware = new ArrayList<Software>(amountOfUselessSoftware);
		for (int i = 1; i <= amountOfUselessSoftware; i++) {
			uselessSoftware.add(new Software("Useless Software"));
		}
		
		sysModel.addNode(t1);
		sysModel.addNode(t2);
		sysModel.addNode(t3);
		for (Software s : uselessSoftware) {
			sysModel.addNode(s);
		}
		
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		t1.registerReadsWritesOn(dBus);
		t2.registerReadsWritesOn(dBus);
		t3.registerReadsWritesOn(dBus);
		for (Software s : uselessSoftware) {
			s.registerReadsWritesOn(dBus);
		}
		
		Asset v1 = new Asset("Web2.0", t1);
		Asset v2 = new Asset("In-App Payment Financial", t2);
		Asset v3 = new Asset("In-App Payment Reputation", t2);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", t1);
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1);
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", t2);
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2);
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3);
		final PotentialExploit exploit6 = new PotentialExploit("Unrelated Exploit", t3);
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);
		for (Software s : uselessSoftware) {
			exploits.add(new PotentialExploit("Some Random Exploit", s));
		}

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500);
		attackerProfile.addAccess(new Access("Phishing URL", t1, amountOfUselessSoftware));
		final Motive m1 = new Motive("Financial", 2.0f);
		final Motive m2 = new Motive("Reputation", 0.1f);
		m1.desires(v1);
		m1.desires(v2);
		m2.desires(v3);
		attackerProfile.addMotivation(m1);
		attackerProfile.addMotivation(m2);
		
		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		AttackGraph ag = c.crawlAndGetAttackForest();
		
		Assert.assertNotNull(ag);
	}
	
	
	

	
	/**
	 * Stress test of the crawler with a huge system model, which contains much dead end software.
	 * @param amountOfUselessSoftwareInBetween2 Number of Software nodes between access and Asset in a chain
	 */
	public static final void testCrawlAndGetAttackGraphWithHugeSystemModelChainScenario(int amountOfUselessSoftwareInBetween2) {

		/* Configuration */
		final int amountOfUselessSoftwareInBetween = amountOfUselessSoftwareInBetween2; // total number of system nodes is three times higher, as each sw also has a capability and a com node. 
		
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Software sw1 = new Software("Browser");
		Software swTerminal = new Software("Token Store");
		List<Software> swInBetween = new ArrayList<Software>(amountOfUselessSoftwareInBetween);
		for (int i = 1; i <= amountOfUselessSoftwareInBetween; i++) {
			swInBetween.add(new Software("Intermediate Software"));
		}
		
		sysModel.addNode(sw1);
		sysModel.addNode(swTerminal);
		for (Software s : swInBetween) {
			sysModel.addNode(s);
		}
		
		CommunicationMedium dBus1 = new CommunicationMedium("dBusStart");
		CommunicationMedium dBusTerminal = new CommunicationMedium("dBusTerminal");
		sw1.registerReadsWritesOn(dBus1);
		swTerminal.registerReadsWritesOn(dBusTerminal);
		Software lastSw = sw1;
		for (Software sw : swInBetween) {
			CommunicationMedium dBusIntermediate = new CommunicationMedium("dBusIntermediate");
			lastSw.registerReadsWritesOn(dBusIntermediate);
			sw.registerReadsWritesOn(dBusIntermediate);
			lastSw = sw;
		}
		lastSw.registerReadsWritesOn(dBusTerminal);
		
		
		Asset v1 = new Asset("Web2.0", sw1);
		Asset vTerminal = new Asset("Terminal Attractor", swTerminal);
		sysModel.addNode(v1);
		sysModel.addNode(vTerminal);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		exploits.add(new PotentialExploit("Browser Exploit", sw1));
		exploits.add(new PotentialExploit("Attractor Extraction #1", v1));
		exploits.add(new PotentialExploit("Terminal Attractor Exploit", vTerminal));
		exploits.add(new PotentialExploit("Terminal Software Exploit", swTerminal));
		for (Software s : swInBetween) {
			exploits.add(new PotentialExploit("Some Random Exploit", s));
		}

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500);
		attackerProfile.addAccess(new Access("Phishing URL", sw1, amountOfUselessSoftwareInBetween));
		final Motive m1 = new Motive("Financial", 2.0f);
		final Motive m2 = new Motive("Reputation", 0.1f);
		m1.desires(v1);
		m2.desires(vTerminal);
		attackerProfile.addMotivation(m1);
		attackerProfile.addMotivation(m2);
		
		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		AttackGraph ag = c.crawlAndGetAttackForest();
		
		Assert.assertNotNull(ag);
	}
	
	
	
	
	



	/**
	 * Stress test of the crawler with a huge system model, which contains much dead end software.
	 * The topology is a big star with long arms that are dead ends.
	 */
	@Test
	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_4_000() {
		testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(20, 200);
	}

	

//	/**
//	 * Test the Crawler with a reasonably small star for debugging.
//	 */
//	@Test
//	public final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario_4() {
//		testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(20, 3);
//	}

	
	
	

	/**
	 * Stress test of the crawler with a huge system model, which contains much dead end software.
	 * @param amountOfUselessSoftwareInBetweenAtEachArm Number of Software nodes between access and Asset in a chain
	 */
	public static final void testCrawlAndGetAttackGraphWithHugeSystemModelStarScenario(int numberOfArms, int amountOfUselessSoftwareInBetweenAtEachArm) {

		if (numberOfArms < 1) {
			throw new IllegalArgumentException("The Star scenario must have at least one arm.");
		}
		if (amountOfUselessSoftwareInBetweenAtEachArm < 1) {
			throw new IllegalArgumentException("The Star scenario must have at least one software node in each arm.");
		}
		
		/* Configuration */
		final int amountOfUselessSoftwareInBetween = amountOfUselessSoftwareInBetweenAtEachArm; // total number of system nodes is three times higher, as each sw also has a capability and a com node. 
		
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Software sw1 = new Software("InitialSoftware");
		Software swTerminal = new Software("TerminalSoftware");
		List<List<Software>> swArms =new ArrayList<List<Software>>(numberOfArms);
		for (int i = 1; i <= numberOfArms; i++) {
			swArms.add(new ArrayList<Software>(amountOfUselessSoftwareInBetween));
		}

		for (List<Software> swOnArm : swArms) {
			for (int i = 1; i <= amountOfUselessSoftwareInBetween; i++) {
				swOnArm.add(new Software("Intermediate Software"+i));
			}
		}
		
		sysModel.addNode(sw1);
		sysModel.addNode(swTerminal);
		for (List<Software> swOnArm : swArms) {
			for (Software s : swOnArm) {
				sysModel.addNode(s);
			}
		}
		
		CommunicationMedium dBus1 = new CommunicationMedium("dBusStart");
		CommunicationMedium dBusTerminal = new CommunicationMedium("dBusTerminal");
		sw1.registerReadsWritesOn(dBus1);
		swTerminal.registerReadsWritesOn(dBusTerminal);
		Software lastSw = sw1;

		for (List<Software> swOnArm : swArms) { 		// each arm is to be connected to the start Com Medium.
			swOnArm.get(0).registerReadsWritesOn(dBus1);
		}
		for (List<Software> swOnArm : swArms) { // each arm is a separate chain with many in between nodes.
			for (Software sw : swOnArm) {
				CommunicationMedium dBusIntermediate = new CommunicationMedium("dBusIntermediate");
				lastSw.registerReadsWritesOn(dBusIntermediate);
				sw.registerReadsWritesOn(dBusIntermediate);
				lastSw = sw;
			}
		}
		lastSw.registerReadsWritesOn(dBusTerminal);
		
		
//		Attractor v1 = new Attractor("Web2.0", sw1);
		Asset vTerminal = new Asset("Terminal Attractor", swTerminal);
//		sysModel.addNode(v1);
		sysModel.addNode(vTerminal);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		exploits.add(new PotentialExploit("Browser Exploit", sw1));
//		exploits.add(new PotentialExploit("Attractor Extraction #1", v1));
		exploits.add(new PotentialExploit("Terminal Attractor Exploit", vTerminal));
		exploits.add(new PotentialExploit("Terminal Software Exploit", swTerminal));
		for (List<Software> swOnArm : swArms) {
			for (Software s : swOnArm) {
				exploits.add(new PotentialExploit("Some Random Exploit", s));
			}
		}

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500);
		attackerProfile.addAccess(new Access("Phishing URL", sw1, amountOfUselessSoftwareInBetween));
		final Motive m1 = new Motive("Financial", 2.0f);
		final Motive m2 = new Motive("Reputation", 0.1f);
//		m1.desires(v1);
		m2.desires(vTerminal);
		attackerProfile.addMotivation(m1);
		attackerProfile.addMotivation(m2);
		
		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		AttackGraph ag = c.crawlAndGetAttackForest();
		
		Assert.assertNotNull(ag);
	}
	

	
}
