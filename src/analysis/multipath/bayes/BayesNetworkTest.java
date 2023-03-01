/**
 * 
 */
package analysis.multipath.bayes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.Crawler;
import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
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

/**
 * @author Martin Salfer
 * @created 2016-04-07
 * @deprecated Use analysis.multipath.poincare instead!
 *
 */
@Deprecated
public class BayesNetworkTest {


	private static final String BAYES_DOT_OUTPUT_FILES = "output/V2Evo/graph";
//	private static final String BAYES_DOT_REFERENCE_FILES = "input/JUnitReferences/graph"; // TODO Testfälle mit bekannten Graphen vergleichen.
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the construction of correct Probability Networks.
	 * The system model will have a "multi-homed" node, where the use of the AttackVector class can be seen.
	 */
//	@Test
//	2017-01-02: Bayes network implementation is put on hold.
	public final void crawlingAndInferenceChainTest() {
		/* Configuration */
		final int amountOfIntermediarySoftware = 1;
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		final Software s1 = new Software("Browser");
		final Software s2 = new Software("Payment Service");
		final List<Software> intermediarySoftware = new ArrayList<Software>(amountOfIntermediarySoftware);
		for (int i = 1; i <= amountOfIntermediarySoftware; i++) {
			intermediarySoftware.add(new Software("IntermediarySoftware" + i));
		}
		sysModel.addNode(s1);
		sysModel.addNode(s2);
		for (Software s : intermediarySoftware) {
			sysModel.addNode(s);
		}
		
		/* Communication Definition */
		final CommunicationMedium dBusFront = new CommunicationMedium("dBusFrontend");
		final CommunicationMedium dBusBack = new CommunicationMedium("dBusBackend");
		s1.registerReadsWritesOn(dBusFront);
		s2.registerReadsWritesOn(dBusBack);
		for (Software s : intermediarySoftware) {
			s.registerReadsWritesOn(dBusFront);
			s.registerReadsWritesOn(dBusBack);
		}
		
		final Asset v1 = new Asset("Web2.0", s1);
		final Asset v2 = new Asset("In-App Payment Financial", s2);
		final Asset v3 = new Asset("In-App Payment Reputation", s2);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", s1, new Resources(10_000d, 0d));
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1, new Resources(10_000d, 0d));
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit #1", s2, new Resources(10_000d, 0d));
		final PotentialExploit exploit6 = new PotentialExploit("Payment Exploit #2", s2, new Resources(30_000d, 0d));
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction (hard)", v2, new Resources(100_000d, 0d));
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation (hard)", v3, new Resources(100_000d, 0d));
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);
		for (Software s : intermediarySoftware) {
			exploits.add(new PotentialExploit("Some easy Exploit", s, new Resources(1_000d, 0d)));
		}

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500, new Resources(300_000d, 0d));
		attackerProfile.addAccess(new Access("Phishing URL", s1, amountOfIntermediarySoftware));
		
		/* Analysis */
		final AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		final Crawler c = new Crawler(scenario);
		final AttackGraph ag = c.crawlAndGetAttackGraphOnBayes();
		final ProbabilisticNetwork pn1 = ag.getProbabilisticNetworkFor(v1);
		final ProbabilisticNetwork pn2 = ag.getProbabilisticNetworkFor(v2);
		final ProbabilisticNetwork pn3 = ag.getProbabilisticNetworkFor(v3);
		final ProbabilisticNetwork[] pns = {pn1, pn2, pn3};
		
		/* Test Assertions #1 */
		assertNotNull(ag);
		assertEquals("Node number missmatch",  3, pn1.getNodes().size());
		assertEquals("Node number missmatch",  5, pn2.getNodes().size());
		assertEquals("Node number missmatch",  5, pn3.getNodes().size());
		assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
		assertEquals("Edge number missmatch",  4, pn2.getEdges().size());
		assertEquals("Edge number missmatch",  4, pn3.getEdges().size());
		
		// Inference
		for (ProbabilisticNetwork pn : pns) {
			IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
			algo.setNetwork(pn);
			algo.run();
		}

		// Output for Debugging.
		//		System.out.println("Network: " + pn.getName());
//		for (Node n: pn.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
		
		/* Test Assertions #2 */
		Iterator<Node> it = pn1.getNodes().iterator();
//		assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertFalse(it.hasNext());
//		it = pn2.getNodes().iterator();
//		assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertFalse(it.hasNext());
//		it = pn2.getNodes().iterator();
//		assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//		assertFalse(it.hasNext());
		fail("Schließungs-Wahrscheinlichkeiten noch festigen, sobald die Wahrscheinlichkeitsfunktionen definiert sind."); // TODO
	}
	
	
	/**
	 * Test the construction of correct Probability Networks.
	 * The system model will have a "multi-homed" node, where the use of the AttackVector class can be seen.
	 */
//	@Test
//	2017-01-02: Bayes network implementation is put on hold.
	public void crawlingAndDotOutput0SwTest() {
		/* System model */
		final SystemModel sysModel = new SystemModel();
		final Software s1 = new Software("Sw");
		sysModel.addNode(s1);

		/* Communication Definition */
		final CommunicationMedium dBus = new CommunicationMedium("dBus");
		s1.registerReadsWritesOn(dBus);
		
		final Asset v1 = new Asset("Web2.0", s1);
		sysModel.addNode(v1);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", s1, new Resources(10_000d, 0d));
		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1, new Resources(10_000d, 0d));
		exploits.add(exploit1);
		exploits.add(exploit2);

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500, new Resources(300_000d, 0d));
		attackerProfile.addAccess(new Access("Phishing URL1", s1, 1));
		attackerProfile.addAccess(new Access("Phishing URL2", s1, 1));
		
		/* Analysis */
		final AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		final Crawler c = new Crawler(scenario);
		final AttackGraph ag = c.crawlAndGetAttackGraphOnBayes();
		
		/* Test Assertions*/
		assertNotNull(ag);
		
		final ProbabilisticNetwork pn1 = ag.getProbabilisticNetworkFor(v1);
		
		// Inference
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(pn1);
		algo.run();
		
		// Print
//		final ProbabilisticNetwork pn = pn1;
//		System.out.println("Network: " + pn.getName());
//		for (Node n: pn.getNodes()) {
//			ProbabilisticNode n2 = (ProbabilisticNode) n;
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println(Arrays.toString(n2.getProbabilityFunction().getValues()));
//			System.out.println();
//		}

		// Write to Files.
		BayesNetwork.writeToFile(pn1, BAYES_DOT_OUTPUT_FILES + "0Sw.gv", true);

		fail("Schließungs-Wahrscheinlichkeiten noch festigen, sobald die Wahrscheinlichkeitsfunktionen definiert sind."); // TODO
	}

	

//	@Test
//	2017-01-02: Bayes network implementation is put on hold.
	public void crawlingAndDotOutput1to3SwTest() {
		for (int amountOfIntermediarySoftware : new int[]{1, 2, 3}) {
			/* System model */
			final SystemModel sysModel = new SystemModel();
			final Software s1 = new Software("Browser");
			final Software s2 = new Software("Payment Service");
			final List<Software> intermediarySoftware = new ArrayList<Software>(amountOfIntermediarySoftware);
			for (int i = 1; i <= amountOfIntermediarySoftware; i++) {
				intermediarySoftware.add(new Software("IntermediarySoftware" + i));
			}
			sysModel.addNode(s1);
			sysModel.addNode(s2);
			for (Software s : intermediarySoftware) {
				sysModel.addNode(s);
			}
			
			/* Communication Definition */
			final CommunicationMedium dBusFront = new CommunicationMedium("dBusFrontend");
			final CommunicationMedium dBusBack = new CommunicationMedium("dBusBackend");
			s1.registerReadsWritesOn(dBusFront);
			s2.registerReadsWritesOn(dBusBack);
			for (Software s : intermediarySoftware) {
				s.registerReadsWritesOn(dBusFront);
				s.registerReadsWritesOn(dBusBack);
			}
			
			final Asset v1 = new Asset("Web2.0", s1);
			final Asset v2 = new Asset("In-App Payment Financial", s2);
			final Asset v3 = new Asset("In-App Payment Reputation", s2);
			sysModel.addNode(v1);
			sysModel.addNode(v2);
			sysModel.addNode(v3);
			
			/* Exploit database. */
			final HashSet<Exploit> exploits = new HashSet<Exploit>();
			final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", s1, new Resources(10_000d, 0d));
			final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1, new Resources(10_000d, 0d));
			final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit #1", s2, new Resources(10_000d, 0d));
			final PotentialExploit exploit6 = new PotentialExploit("Payment Exploit #2", s2, new Resources(30_000d, 0d));
			final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction (hard)", v2, new Resources(100_000d, 0d));
			final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation (hard)", v3, new Resources(100_000d, 0d));
			exploits.add(exploit1);
			exploits.add(exploit2);
			exploits.add(exploit3);
			exploits.add(exploit4);
			exploits.add(exploit5);
			exploits.add(exploit6);
			for (Software s : intermediarySoftware) {
				exploits.add(new PotentialExploit("Some easy Exploit", s, new Resources(1_000d, 0d)));
			}
			
			/* Attacker Profile. */
			final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500, new Resources(300_000d, 0d));
			attackerProfile.addAccess(new Access("Phishing URL", s1, amountOfIntermediarySoftware));
			
			/* Analysis */
			final AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
			final Crawler c = new Crawler(scenario);
			final AttackGraph ag = c.crawlAndGetAttackGraphOnBayes();
			
			/* Test Assertions*/
			assertNotNull(ag);
			final ProbabilisticNetwork pn1 = ag.getProbabilisticNetworkFor(v1);
			final ProbabilisticNetwork pn2 = ag.getProbabilisticNetworkFor(v2);
			final ProbabilisticNetwork pn3 = ag.getProbabilisticNetworkFor(v3);
			final ProbabilisticNetwork[] pns = {pn1, pn2, pn3};
			if (amountOfIntermediarySoftware == 1) {
				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
				assertEquals("Node number missmatch",  5, pn2.getNodes().size());
				assertEquals("Node number missmatch",  5, pn3.getNodes().size());
				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
				assertEquals("Edge number missmatch",  4, pn2.getEdges().size());
				assertEquals("Edge number missmatch",  4, pn3.getEdges().size());
			}
			if (amountOfIntermediarySoftware == 2) {
				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
				assertEquals("Node number missmatch",  8, pn2.getNodes().size());
				assertEquals("Node number missmatch",  8, pn3.getNodes().size());
				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
				assertEquals("Edge number missmatch",  9, pn2.getEdges().size());
				assertEquals("Edge number missmatch",  9, pn3.getEdges().size());
			}
			if (amountOfIntermediarySoftware == 3) {
				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
				assertEquals("Node number missmatch",  12, pn2.getNodes().size());
				assertEquals("Node number missmatch",  12, pn3.getNodes().size());
				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
				assertEquals("Edge number missmatch",  16, pn2.getEdges().size());
				assertEquals("Edge number missmatch",  16, pn3.getEdges().size());
			}
//			Iterator<Node> it = pn1.getNodes().iterator();
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
//			it = pn2.getNodes().iterator();
//			assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
//			it = pn2.getNodes().iterator();
//			assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
			
//			// Printing for Debugging
//			for (ProbabilisticNetwork pn : pns) {
//				System.out.println(amountOfIntermediarySoftware);
//				System.out.println("Nodes" + pn.getNodes().size());
//				System.out.println("Edges" + pn.getEdges().size());
//			}
			
			// Inference
			for (ProbabilisticNetwork pn : pns) {
				IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
				algo.setNetwork(pn);
				algo.run();
			}
			
			// Write to Files.
			BayesNetwork.writeToFile(pn1, BAYES_DOT_OUTPUT_FILES + amountOfIntermediarySoftware + "SwAndAsset1.gv", true); 
			BayesNetwork.writeToFile(pn2, BAYES_DOT_OUTPUT_FILES + amountOfIntermediarySoftware + "SwAndAsset2.gv", true); 
			BayesNetwork.writeToFile(pn3, BAYES_DOT_OUTPUT_FILES + amountOfIntermediarySoftware + "SwAndAsset3.gv", true); 
//
//		// Compare with Reference Files.
////		fileCounter = 1;
////		for (ProbabilisticNetwork pn : pns) {
////			try {
////				final String graphInDot = BayesNetwork.getGraphInDot(pn, false);
////				System.out.println(graphInDot);
////				BufferedReader reader = new BufferedReader(new FileReader(BAYES_DOT_REFERENCE_FILES + fileCounter++ + ".gv"));
////				for (String s : graphInDot.split("\n")) {
////					if (!reader.readLine().equals(s)) {
////						// TODO Knotennamen springen unvorhersehbar. Prüfung muss flexibler werden! 
////						fail("Lines not matching: " + s);
////					}
////				}
////				reader.close();
////			} catch (IOException e) {
////				e.printStackTrace();
////			} finally {
////			}
////		}
//	}
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO Testfall für bis zu zwei Eingangsvariablen.
			// TODO Testfall für deutlich büber zwei Eingangsvariablen (also mit TRichter).
			fail("Not fully implemented, yet"); // TODO Inferenzergebnisse stabilisieren und definieren.
		}
	}

//	@Test
//	2017-01-02: Bayes network implementation is put on hold.
	public void crawlingAndDotOutputMeshTest() {
		for (int amountOfMeshedSoftware : new int[]{5}) {
			/* System model */
			final SystemModel sysModel = new SystemModel();
			final Software s1 = new Software("Browser");
			final Software s2 = new Software("Payment Service");
			final List<Software> intermediarySoftware = new ArrayList<Software>(amountOfMeshedSoftware);
			for (int i = 1; i <= amountOfMeshedSoftware; i++) {
				intermediarySoftware.add(new Software("IntermediarySoftware" + i));
			}
			sysModel.addNode(s1);
			sysModel.addNode(s2);
			for (Software s : intermediarySoftware) {
				sysModel.addNode(s);
			}
			
			/* Communication Definition */
			final CommunicationMedium dBus = new CommunicationMedium("dBus");
			s1.registerReadsWritesOn(dBus);
			s2.registerReadsWritesOn(dBus);
			for (Software s : intermediarySoftware) {
				s.registerReadsWritesOn(dBus);
			}
			
			final Asset v1 = new Asset("Web2.0", s1);
			final Asset v2 = new Asset("In-App Payment Financial", s2);
			final Asset v3 = new Asset("In-App Payment Reputation", s2);
			sysModel.addNode(v1);
			sysModel.addNode(v2);
			sysModel.addNode(v3);
			
			/* Exploit database. */
			final HashSet<Exploit> exploits = new HashSet<Exploit>();
			final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", s1, new Resources(10_000d, 0d));
			final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1, new Resources(10_000d, 0d));
			final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit #1", s2, new Resources(10_000d, 0d));
			final PotentialExploit exploit6 = new PotentialExploit("Payment Exploit #2", s2, new Resources(30_000d, 0d));
			final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction (hard)", v2, new Resources(100_000d, 0d));
			final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation (hard)", v3, new Resources(100_000d, 0d));
			exploits.add(exploit1);
			exploits.add(exploit2);
			exploits.add(exploit3);
			exploits.add(exploit4);
			exploits.add(exploit5);
			exploits.add(exploit6);
			for (Software s : intermediarySoftware) {
				exploits.add(new PotentialExploit("Intermediary", s, new Resources(1_000d, 0d)));
			}
			
			/* Attacker Profile. */
			final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 500, new Resources(300_000d, 0d));
			attackerProfile.addAccess(new Access("Phishing URL", s1, amountOfMeshedSoftware));
			
			/* Analysis */
			final AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
			final Crawler c = new Crawler(scenario);
			final AttackGraph ag = c.crawlAndGetAttackGraphOnBayes();
			
			/* Test Assertions*/
//			assertNotNull(ag);
			final ProbabilisticNetwork pn1 = ag.getProbabilisticNetworkFor(v1);
			final ProbabilisticNetwork pn2 = ag.getProbabilisticNetworkFor(v2);
			final ProbabilisticNetwork pn3 = ag.getProbabilisticNetworkFor(v3);
			final ProbabilisticNetwork[] pns = {pn1, pn2, pn3};
//			if (amountOfMeshedSoftware == 1) {
//				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
//				assertEquals("Node number missmatch",  5, pn2.getNodes().size());
//				assertEquals("Node number missmatch",  5, pn3.getNodes().size());
//				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
//				assertEquals("Edge number missmatch",  4, pn2.getEdges().size());
//				assertEquals("Edge number missmatch",  4, pn3.getEdges().size());
//			}
//			if (amountOfMeshedSoftware == 2) {
//				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
//				assertEquals("Node number missmatch",  8, pn2.getNodes().size());
//				assertEquals("Node number missmatch",  8, pn3.getNodes().size());
//				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
//				assertEquals("Edge number missmatch",  9, pn2.getEdges().size());
//				assertEquals("Edge number missmatch",  9, pn3.getEdges().size());
//			}
//			if (amountOfMeshedSoftware == 3) {
//				assertEquals("Node number missmatch",  3, pn1.getNodes().size());
//				assertEquals("Node number missmatch",  12, pn2.getNodes().size());
//				assertEquals("Node number missmatch",  12, pn3.getNodes().size());
//				assertEquals("Edge number missmatch",  2, pn1.getEdges().size());
//				assertEquals("Edge number missmatch",  16, pn2.getEdges().size());
//				assertEquals("Edge number missmatch",  16, pn3.getEdges().size());
//			}
//			Iterator<Node> it = pn1.getNodes().iterator();
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
//			it = pn2.getNodes().iterator();
//			assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
//			it = pn2.getNodes().iterator();
//			assertEquals("Asset Missmatch", 1E-05d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("PaySw Missmatch", 1E-04d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("IntSw Missmatch", 1E-03d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("BrwSw Missmatch", 1E-02d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertEquals("AccNo Missmatch", 1E-01d, ((ProbabilisticNode)it.next()).getMarginalAt(0), 0.00001d);
//			assertFalse(it.hasNext());
			
//			// Printing for Debugging
//			for (ProbabilisticNetwork pn : pns) {
//				System.out.println(amountOfIntermediarySoftware);
//				System.out.println("Nodes" + pn.getNodes().size());
//				System.out.println("Edges" + pn.getEdges().size());
//			}
			
			// Inference
			for (ProbabilisticNetwork pn : pns) {
				IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
				algo.setNetwork(pn);
				algo.run();
			}
			
			// Write to Files.
//			BayesNetwork.writeToFile(pn1, BAYES_DOT_OUTPUT_FILES + amountOfMeshedSoftware + "Mesh1.gv", true);
			BayesNetwork.writeToFile(pn2, BAYES_DOT_OUTPUT_FILES + amountOfMeshedSoftware + "Mesh2.gv", true);
//			BayesNetwork.writeToFile(pn3, BAYES_DOT_OUTPUT_FILES + amountOfMeshedSoftware + "SwAndAsset3.gv", true);
//
//		// Compare with Reference Files.
////		fileCounter = 1;
////		for (ProbabilisticNetwork pn : pns) {
////			try {
////				final String graphInDot = BayesNetwork.getGraphInDot(pn, false);
////				System.out.println(graphInDot);
////				BufferedReader reader = new BufferedReader(new FileReader(BAYES_DOT_REFERENCE_FILES + fileCounter++ + ".gv"));
////				for (String s : graphInDot.split("\n")) {
////					if (!reader.readLine().equals(s)) {
////						// TODO Knotennamen springen unvorhersehbar. Prüfung muss flexibler werden! 
////						fail("Lines not matching: " + s);
////					}
////				}
////				reader.close();
////			} catch (IOException e) {
////				e.printStackTrace();
////			} finally {
////			}
////		}
//	}
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO assertEqual("", 123, bn1.getOverallCost().getMean()); // implicit inference.
			// TODO assertEqual("", 123, bn1.getOverallCost().getStdDev()); // implicit inference.
			// TODO assertEqual("", 5, bn.getNumberOfNodesReal();
			// TODO assertEqual("", 100, bn.getNumberOfNodesIncludingFunnels());
			
			// TODO Testfall für bis zu zwei Eingangsvariablen.
			// TODO Testfall für deutlich büber zwei Eingangsvariablen (also mit TRichter).
			fail("Not fully implemented, yet"); // TODO Inferenzergebnisse stabilisieren und definieren.
		}
	}
	
}
