/**
 * 
 */
package analysis.multipath.bayes;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import analysis.Analyzer;
import analysis.Crawler;
import types.Resources;
import unbbayes.prs.Edge;
import unbbayes.prs.Node;
import unbbayes.prs.bn.JunctionTreeAlgorithm;
import unbbayes.prs.bn.PotentialTable;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.prs.exception.InvalidParentException;
import unbbayes.util.extension.bn.inference.IInferenceAlgorithm;

/**
 * @deprecated Use analysis.multipath.accumulator instead!
 * @author Martin Salfer
 * @created 17.04.2016 12:48:26
 *
 */
@Deprecated
public class BayesNetworkUnbbayesTest {

	private static final float IMPOSSIBLE = 0f;
	private static final float ALWAYS = 1f;
	private static int NodeCounter = 0;
	
	/**
	 * Example graph for testing the capabilities of UnBBayes inference.
	 * This main method is useful for summing up the time effort for all tests.
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		BayesNetworkUnbbayesTest b = new BayesNetworkUnbbayesTest();
		b.testChain();
		b.testY();
		b.testFascis();
		b.testFascisFunnel();
		b.testPseudoEcuNetwork();
		System.out.println("Fertig nach " + String.valueOf((System.currentTimeMillis()-startTime)/1000.0).replace(".", ",") + " Sekunden.");
	}

	
	@Test
	@Ignore // Fehlerhaft, setVAlue() anstatt addValueAt()!!!
	public void testY() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");

		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		node.setName("AAA-Übernahme");
		node.setDescription("SG AAA wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		net.addNode(node);
		aaa = node;
		
		node = new ProbabilisticNode();
		node.setName("BBB-Übernahme");
		node.setDescription("SG BBB wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		net.addNode(node);
		bbb = node;
		
		node = new ProbabilisticNode();
		node.setName("CCC-Übernahme");
		node.setDescription("SG CCC wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		table.addValueAt(2, 0.1f);
		table.addValueAt(3, 0.9f);
		table.addValueAt(4, 0.1f);
		table.addValueAt(5, 0.9f);
		table.addValueAt(6, 0f);
		table.addValueAt(7, 1f);
		net.addNode(node);
		ccc = node;
		
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}

		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}

	@Test
	@Ignore // Fehlerhaft, setVAlue() anstatt addValueAt()!!!
	public void testChain() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");

		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		node.setName("AAA-Übernahme");
		node.setDescription("SG AAA wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		net.addNode(node);
		aaa = node;
		
		node = new ProbabilisticNode();
		node.setName("BBB-Übernahme");
		node.setDescription("SG BBB wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		table.addValueAt(2, 0f);
		table.addValueAt(3, 1f);
		net.addNode(node);
		bbb = node;
		
		node = new ProbabilisticNode();
		node.setName("CCC-Übernahme");
		node.setDescription("SG CCC wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, 0.1f);
		table.addValueAt(1, 0.9f);
		table.addValueAt(2, 0f);
		table.addValueAt(3, 1f);
		net.addNode(node);
		ccc = node;
		
		try {
			net.addEdge(new Edge(aaa, bbb));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}

		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}
	

	@Test
	public void testFascis() {
		int fascisSize = 10;
		final float successProbability = 0.1f;
		final float always = 1f;
		final float successProbabilityNegated = always-successProbability;
		final float impossible = 0f;
		
		
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");

		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		node.setName("AAA-Übernahme");
		node.setDescription("SG AAA wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, successProbability);
		table.addValueAt(1, successProbabilityNegated);
		net.addNode(node);
		aaa = node;

		Set<ProbabilisticNode> fasces = new HashSet<ProbabilisticNode>(fascisSize+1);
		for (int i=0 ; i<fascisSize ; i++) {
			node = new ProbabilisticNode();
			node.setName("Fasces-" + (i+1) + "-Übernahme");
			node.setDescription("SG " + (i+1) + " wird erfolgreich gehackt.");
			node.appendState("Gehackt");
			node.appendState("Gehalten");
			table = node.getProbabilityFunction();
			table.addVariable(node);
			table.addValueAt(0, successProbability);
			table.addValueAt(1, successProbabilityNegated);
			table.addValueAt(2, impossible);
			table.addValueAt(3, always);
			net.addNode(node);
			fasces.add(node);
		}
		
		node = new ProbabilisticNode();
		node.setName("CCC-Übernahme");
		node.setDescription("SG CCC wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		int tableAdress = 0 ;
		while (tableAdress < (2*Math.pow(2, fascisSize))-2) {
			table.addValueAt(tableAdress++, successProbability);
			table.addValueAt(tableAdress++, successProbabilityNegated);
		}
		table.addValueAt(tableAdress++, impossible);
		table.addValueAt(tableAdress, always);
		assertEquals("Mismatch", 2*Math.pow(2, fascisSize)-1, tableAdress, 0.01d); // TODO recheck formula.
		net.addNode(node);
		ccc = node;
		
		try {
			for (ProbabilisticNode n: fasces) {
				net.addEdge(new Edge(aaa, n));
				net.addEdge(new Edge(n, ccc));
			}
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}

		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}	
	}


	@Test
	public void testFascisFunnel() {
		int fascisSize = 1_000;
		final float successProbability = 0.1f;
		final float always = 1f;
		final float successProbabilityNegated = always-successProbability;
		final float impossible = 0f;
		
		
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");

		ProbabilisticNode aaa;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		aaa = new ProbabilisticNode();
		aaa.setName("AAA-Übernahme");
		aaa.setDescription("SG AAA wird erfolgreich gehackt.");
		aaa.appendState("Gehackt");
		aaa.appendState("Gehalten");
		table = aaa.getProbabilityFunction();
		table.addVariable(aaa);
		table.addValueAt(0, successProbability);
		table.addValueAt(1, successProbabilityNegated);
		net.addNode(aaa);

		
		Set<ProbabilisticNode> fasces = generateInputNodes(fascisSize, successProbability, always, successProbabilityNegated, impossible, net, aaa, "node");
		Map<ProbabilisticNode, Double> fascesMap = new HashMap<ProbabilisticNode, Double>();
		for (ProbabilisticNode e : fasces) {
			fascesMap.put(e, 0.1d);
		}
		ProbabilisticNode last = Crawler.funnelToOneNodeBayes(net, fascesMap);
		
		ccc = new ProbabilisticNode();
		ccc.setName("CCC-Übernahme");
		ccc.setDescription("SG CCC wird erfolgreich gehackt.");
		ccc.appendState("Gehackt");
		ccc.appendState("Gehalten");
		table = ccc.getProbabilityFunction();
		table.addVariable(ccc);
		table.addValueAt(0, successProbability);
		table.addValueAt(1, successProbabilityNegated);
		table.addValueAt(2, impossible);
		table.addValueAt(3, always);
		net.addNode(ccc);
		
		try {
				net.addEdge(new Edge(last, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}

		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}
	
	

	@Test
	public void testPseudoEcuNetwork() {
		final int ecuAmountWave1 = 10; //1_000;
		final int ecuAmountWave2 = 90; // 4_000;
		final int connectionsPerECU = 500; // 10_000;
		final float successProbability = 0.1f;
		final float successProbabilityNegated = Crawler.ALWAYS-successProbability;
		
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		ProbabilisticNode aaa;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		/*
		 * AAA is the first node and the access into the network.
		 */
		aaa = new ProbabilisticNode();
		aaa.setName("AAA-Übernahme");
		aaa.setDescription("SG AAA wird erfolgreich gehackt.");
		aaa.appendState("Gehackt");
		aaa.appendState("Gehalten");
		table = aaa.getProbabilityFunction();
		table.addVariable(aaa);
		table.setValue(0, successProbability);
		table.setValue(1, successProbabilityNegated);
		net.addNode(aaa);

		/* Wave 1
		 * These are ECUs that are directly accessible from the access node AAA. 
		 */
		Set<ProbabilisticNode> ecuWave1 = generateInputNodes(ecuAmountWave1, successProbability, Crawler.ALWAYS, successProbabilityNegated, Crawler.IMPOSSIBLE, net, aaa, "EcuW1");
		final Map<ProbabilisticNode, Double> ecuWave1Map = new HashMap<ProbabilisticNode, Double>();
		for (ProbabilisticNode e : ecuWave1) {
			ecuWave1Map.put(e, 0.1d);
		}
		ProbabilisticNode ecuWave1Last = Crawler.funnelToOneNodeBayes(net, ecuWave1Map);
		Set<ProbabilisticNode> comWave1 = generateInputNodes(connectionsPerECU, successProbability, Crawler.ALWAYS, successProbabilityNegated, Crawler.IMPOSSIBLE, net, ecuWave1Last, "COM");
		final Map<ProbabilisticNode, Double> comWave1Map = new HashMap<ProbabilisticNode, Double>();
		for (ProbabilisticNode e : comWave1) {
			comWave1Map.put(e, 0.1d);
		}
		ProbabilisticNode comWave1Last = Crawler.funnelToOneNodeBayes(net, comWave1Map);
		Set<ProbabilisticNode> ecuWave2 = generateInputNodes(ecuAmountWave2, successProbability, Crawler.ALWAYS, successProbabilityNegated, Crawler.IMPOSSIBLE, net, comWave1Last, "EcuW2");
		final Map<ProbabilisticNode, Double> ecuWave2Map = new HashMap<ProbabilisticNode, Double>();
		for (ProbabilisticNode e : ecuWave2) {
			ecuWave2Map.put(e, 0.1d);
		}
		ProbabilisticNode ecuWave2Last = Crawler.funnelToOneNodeBayes(net, ecuWave2Map);
		
		
		ccc = new ProbabilisticNode();
		ccc.setName("CCC-Übernahme");
		ccc.setDescription("SG CCC wird erfolgreich gehackt.");
		ccc.appendState("Gehackt");
		ccc.appendState("Gehalten");
		table = ccc.getProbabilityFunction();
		table.addVariable(ccc);
		table.setValue(0, successProbability);
		table.setValue(1, successProbabilityNegated);
		table.addValueAt(2, Crawler.IMPOSSIBLE);
		table.addValueAt(3, Crawler.ALWAYS);
		net.addNode(ccc);
		
		try {
				net.addEdge(new Edge(ecuWave2Last, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}

		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		final int funnelNodeCounter = Crawler.getFunnelNodeCounter();
		final int i = ecuAmountWave1+ecuAmountWave2;
		System.out.println("created and interfered " + funnelNodeCounter + " funnel nodes and " + i + " ECU nodes, in sum: " + (funnelNodeCounter+i));
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}	
	}



	/**
	 * Batch Creation of Input Nodes 
	 * 
	 * @param fascisSize
	 * @param successProbability
	 * @param always
	 * @param successProbabilityNegated
	 * @param impossible
	 * @param net
	 * @param aaa
	 * @param nodeName TODO
	 * @return
	 */
	private Set<ProbabilisticNode> generateInputNodes(int fascisSize, final float successProbability,
			final float always, final float successProbabilityNegated, final float impossible, ProbabilisticNetwork net,
			ProbabilisticNode aaa, String nodeName) {
		Set<ProbabilisticNode> fasces = new HashSet<ProbabilisticNode>(fascisSize+1);
		for (int i=0 ; i<fascisSize ; i++) {
			ProbabilisticNode node = createNode(successProbability, always, successProbabilityNegated, impossible,
					net, fasces, i, nodeName);
			try {
					net.addEdge(new Edge(aaa, node));
			} catch (InvalidParentException e) {
				e.printStackTrace();
			}
		}
		return fasces;
	}

	
	/**
	 * Batch Creation of Nodes.
	 * Deprecated as the set of inputNodes are attached without broadening the input variables. Will result in erroneous networks.
	 * @param fascisSize
	 * @param successProbability
	 * @param always
	 * @param successProbabilityNegated
	 * @param impossible
	 * @param net
	 * @param inputNodes Theses nodes are the input for every single batch created node
	 * @return
	 * @deprecated
	 */
	private Set<ProbabilisticNode> generateInputNodes(int fascisSize, final float successProbability,
			final float always, final float successProbabilityNegated, final float impossible, ProbabilisticNetwork net,
			Set<ProbabilisticNode> inputNodes) {
		Set<ProbabilisticNode> fasces = new HashSet<ProbabilisticNode>(fascisSize+1);
		for (int i=0 ; i<fascisSize ; i++) {
			ProbabilisticNode node = createNode(successProbability, always, successProbabilityNegated, impossible,
					net, fasces, i, "");
			try {
					for (ProbabilisticNode n: inputNodes) {
						net.addEdge(new Edge(n, node));
					}
			} catch (InvalidParentException e) {
				e.printStackTrace();
			}
		}
		return fasces;
	}
	/**
	 * Create a single node.
	 * @param successProbability
	 * @param always
	 * @param successProbabilityNegated
	 * @param impossible
	 * @param net
	 * @param fasces
	 * @param i
	 * @param nodeName TODO
	 * @return
	 */
	private ProbabilisticNode createNode(final float successProbability, final float always,
			final float successProbabilityNegated, final float impossible, ProbabilisticNetwork net,
			Set<ProbabilisticNode> fasces, int i, String nodeName) {
		PotentialTable table;
		ProbabilisticNode node = new ProbabilisticNode();
		node.setName(nodeName + "-" + NodeCounter++ + (i+1) + "-Übernahme");
//		node.setDescription("SG " + (i+1) + " wird erfolgreich gehackt.");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.addValueAt(0, successProbability);
		table.addValueAt(1, successProbabilityNegated);
		table.addValueAt(2, impossible);
		table.addValueAt(3, always);
		net.addNode(node);
		fasces.add(node);
		return node;
	}

	
	@Test
	public void probabilityChainTest() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		final float aHackability = 0.1f;
		final float bHackability = 0.1f;
		final float cHackability = 0.1f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability); // if A.
		table.setValue(1, 1f - bHackability); // if A.
		try {
			net.addEdge(new Edge(aaa, bbb));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(2, IMPOSSIBLE); // if not A.
		table.setValue(3, ALWAYS); // if not A.
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, cHackability); // if B
		table.setValue(1, 1f - cHackability); // if B.
		try {
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(2, IMPOSSIBLE); // if not B.
		table.setValue(3, ALWAYS); // if not B.
		net.addNode(node);
		

		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Print for Debugging
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}

		// Test
		assertEquals("AAA hacked mismatch", 0.1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 0.01d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.001d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
	}

	/**
	 * Test whether the budget can be used for the likelihood. FAILED!
	 * Test by comparing dissimilar calculations with a likelihood chain.
	 * 
	 * Here: Single edge exploit cost compared to the total budget.
	 * Documentation of not working math. 
	 * A correct total affordability probability is NOT merely the multiplication of its parts.
	 *  (single cost to overall budget).
	 *  This shows that Bayes networks are not suitable for this application!
	 */
	@Test
	@Ignore 
	public void probabilityChainBudgetLikelihoodTest1() {
		final Resources exploit1Res = new Resources(1_000, 1_000d);
		final Resources exploit2Res = new Resources(2_000, 1_000d);
		final Resources cost = exploit1Res.addWith(exploit2Res);
		
		final Resources budget = new Resources(3_000, 1_000d);
		final Resources balanceTotal = budget.substractBy(cost);
		final Resources balanceE1 = budget.substractBy(exploit1Res);
		final Resources balanceE2 = budget.substractBy(exploit2Res);
		
		final double exploit1Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE1.getMoneyExpectedValue(), balanceE1.getMoneyStandardDeviation());
		final double exploit2Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE2.getMoneyExpectedValue(), balanceE2.getMoneyStandardDeviation());
		final double balanceProbabilty = Analyzer.probabilityOfPositiveOutcome(balanceTotal.getMoneyExpectedValue(), balanceTotal.getMoneyStandardDeviation());

		System.out.println("exp1P: " + exploit1Pro);
		System.out.println("exp2P: " + exploit2Pro);
		System.out.println("exp1 * exp2: " + exploit1Pro*exploit2Pro);
		System.out.println("balP: " + balanceProbabilty);
	}
	
	/**
	 * Test whether the budget can be used for the likelihood.
	 * Test by comparing dissimilar calculations with a likelihood chain.
	 * Here: Accumulated edge cost compared to the total budget by difference.
	 */
	@Test
	@Ignore // Documentation of not working math. A correct total affordability probability is NOT solved with an accumulator.
	public void probabilityChainBudgetLikelihoodTest2() {
		final Resources exploit1Res = new Resources(1_000, 1_000d);
		final Resources exploit2Res = new Resources(2_000, 1_000d);
		final Resources cost = exploit1Res.addWith(exploit2Res);
		
		final Resources budget = new Resources(3_000, 1_000d);
		final Resources balanceTotal = budget.substractBy(cost);
		final Resources balanceE1 = budget.substractBy(exploit1Res);
		final Resources balanceE2 = balanceE1.substractBy(exploit2Res);
		
		final double exploit1Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE1.getMoneyExpectedValue(), balanceE1.getMoneyStandardDeviation());
		final double exploit2Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE2.getMoneyExpectedValue(), balanceE2.getMoneyStandardDeviation());
		final double balanceProbabilty = Analyzer.probabilityOfPositiveOutcome(balanceTotal.getMoneyExpectedValue(), balanceTotal.getMoneyStandardDeviation());

		System.out.println("exp1P: " + exploit1Pro);
		System.out.println("exp2P: " + exploit2Pro);
		System.out.println("exp1 * exp2: " + exploit1Pro*exploit2Pro);
		System.out.println("balP: " + balanceProbabilty);
	}
	
	/**
	 * Test whether the budget can be used for the likelihood.
	 * Test by comparing dissimilar calculations with a likelihood chain.
	 * Here: Accumulated edge cost compared to the total budget by division.
	 */
	@Test
	public void probabilityChainBudgetLikelihoodDivisionTest() {
		final Resources exploit1Res = new Resources(1_000, 1_000d);
		final Resources exploit2Res = new Resources(2_000, 1_000d);
		final Resources cost = exploit1Res.addWith(exploit2Res);
		
		final Resources budget = new Resources(3_000, 1_000d);
		final Resources balanceTotal = budget.substractBy(cost);
		final Resources balanceE1 = budget.substractBy(exploit1Res);
		final Resources balanceE2 = exploit2Res.substractBy(exploit2Res);
		
		final double exploit1Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE1.getMoneyExpectedValue(), balanceE1.getMoneyStandardDeviation());
		final double exploit2Pro =  Analyzer.probabilityOfPositiveOutcome(balanceE2.getMoneyExpectedValue(), balanceE2.getMoneyStandardDeviation());
		final double balanceProbabilty = Analyzer.probabilityOfPositiveOutcome(balanceTotal.getMoneyExpectedValue(), balanceTotal.getMoneyStandardDeviation());

		System.out.println("exp1P: " + exploit1Pro);
		System.out.println("exp2P: " + exploit2Pro);
		System.out.println("exp1 * exp2: " + exploit1Pro*exploit2Pro);
		System.out.println("balP: " + balanceProbabilty);

	
		final Resources balanceE1B = budget.substractBy(exploit1Res);
		final Resources balanceE1E = budget.substractBy(exploit1Res);

	
	}
	
	
	
	
	
	@Test
	public void simplePairTest1() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		final float aHackability = 0.1f;
		final float bHackability = 0.1f;
		final float cHackability = 0.1f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, cHackability);
		table.setValue(1, 1f - cHackability);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table.setValue(0, cHackability);
		table.setValue(1, 1f- cHackability);
		table.setValue(2, cHackability);
		table.setValue(3, 1f - cHackability);
		table.setValue(4, cHackability);
		table.setValue(5, 1f - cHackability);
		table.setValue(6, IMPOSSIBLE); // not A, not B.
		table.setValue(7, ALWAYS); // not A, not B.
		net.addNode(node);
		

		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Test
		assertEquals("AAA hacked mismatch", 0.1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 0.1d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.019d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}

	@Test
	public void simplePairTest2() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		final float aHackability = 0.1f;
		final float bHackability = 0.01f;
		final float cHackability = 0.1f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, cHackability);
		table.setValue(1, 1f - cHackability);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table.setValue(0, cHackability);
		table.setValue(1, 1f- cHackability);
		table.setValue(2, cHackability);
		table.setValue(3, 1f - cHackability);
		table.setValue(4, cHackability);
		table.setValue(5, 1f - cHackability);
		table.setValue(6, IMPOSSIBLE); // not A, not B.
		table.setValue(7, ALWAYS); // not A, not B.
		net.addNode(node);

		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Test
		assertEquals("AAA hacked mismatch", 0.1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 0.01d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.0109d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}
	

	@Test
	public void exploitPairTest() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		// Config
		final float aHackability = 1f;
		final float bHackability = 1f;
		final float exploitViaA = 0.1f;
		final float exploitViaB = 0.1f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		float a = exploitViaA;
		float b = exploitViaB;
		table.setValue(0, (float)(a + b - a * b)); // A or B
		table.setValue(1, (float)(1d - (a + b - a * b))); // A or B
		table.setValue(2, (float)(a)); // A, not B.
		table.setValue(3, (float)(1d - a)); // A, not B.
		table.setValue(4, (float)(b)); // not A, but B.
		table.setValue(5, (float)(1d - b)); // not A, but B.
		table.setValue(6, IMPOSSIBLE);
		table.setValue(7, ALWAYS);
		net.addNode(node);
		

		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Test
		assertEquals("AAA hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.19d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}
	

	@Test
	public void exploitPairTest2() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		// Config
		final float aHackability = 1f;
		final float bHackability = 1f;
		final float exploitViaA = 0.1f;
		final float exploitViaB = 0.01f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		float a = exploitViaA;
		float b = exploitViaB;
		// A and B
		table.setValue(0, (float)(a + b - a * b));
		table.setValue(1, (float)(1d - (a + b - a * b)));
		// only A, not B
		table.setValue(2, (float)(a));
		table.setValue(3, (float)(1d - a));
		// only B, not A
		table.setValue(4, (float)(b));
		table.setValue(5, (float)(1d - b));
		// neither A or B
		table.setValue(6, IMPOSSIBLE);
		table.setValue(7, ALWAYS);
		net.addNode(node);
		

		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Test
		assertEquals("AAA hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.109d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
//		System.out.println("Network: " + net.getName());
//		for (Node n: net.getNodes()) {
//			System.out.println(n);
//			for (int i=0 ; i<n.getStatesSize() ; i++) {
//				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
//			}
//			System.out.println();
//		}
	}
	
	@Test
	@Ignore // Approximative Inferenz funktioniert nicht
	public void approximateInferenceTest() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("SG-Netzwerk");
		
		// Config
		final float aHackability = 0.1f;
		final float bHackability = 0.1f;
		final float exploitViaA = 0.1f;
		final float exploitViaB = 0.01f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		float a = exploitViaA;
		float b = exploitViaB;
		// A and B
		table.setValue(0, (float)(a + b - a * b));
		table.setValue(1, (float)(1d - (a + b - a * b)));
		// only A, not B
		table.setValue(2, (float)(a));
		table.setValue(3, (float)(1d - a));
		// only B, not A
		table.setValue(4, (float)(b));
		table.setValue(5, (float)(1d - b));
		// neither A or B
		table.setValue(6, IMPOSSIBLE);
		table.setValue(7, ALWAYS);
		net.addNode(node);
		

		// Inference.
//		LikelihoodWeightingSampling algoApproximate = new LikelihoodWeightingSampling();
//		algoApproximate.start(net, 1_000);
//		final float[] fullStatesSetWeight = algoApproximate.getFullStatesSetWeight();
//		System.out.println(fullStatesSetWeight);
		

////		lw = new LikelihoodWeightingSampling();
//		// Register this long task as observer of the sampling task to get a better approximation of the total progress
//		lw.registerObserver(this);
////		long init = System.currentTimeMillis();
//		lw.start(net, sampleSize);
////		long end = System.currentTimeMillis();
////		System.out.println("Time elapsed for sampling: " + (float)(end-init)/1000);
////		init = System.currentTimeMillis();
//		sampleMatrix = lw.getSampledStatesMatrix();
//		sampleWeight = lw.getFullStatesSetWeight();
////		end = System.currentTimeMillis();
////		System.out.println("Time elapsed for matrix: " + (float)(end-init)/1000);
////		System.out.println();
////		algoApproximate.
		
		// Test
//		assertEquals("AAA hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
//		assertEquals("BBB hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
//		assertEquals("CCC hacked mismatch", 0.109d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		
		System.out.println("Network: " + net.getName());
		for (Node n: net.getNodes()) {
			System.out.println(n);
			for (int i=0 ; i<n.getStatesSize() ; i++) {
				System.out.println(n.getStateAt(i) + ": " + ((ProbabilisticNode)n).getMarginalAt(i));
			}
			System.out.println();
		}
	}
	
	
	
	
	@Test
	public void exploitPairWithFollowerTest() {
		ProbabilisticNetwork net = new ProbabilisticNetwork("BayesBasic-Test-Netzwerk");
		
		// Config
		final float aHackability = 1f;
		final float bHackability = 1f;
		final float hackability = 0.1f;
		final float exploitViaA = 0.1f;
		final float exploitViaB = 0.1f;
		
		ProbabilisticNode node;
		ProbabilisticNode aaa;
		ProbabilisticNode bbb;
		ProbabilisticNode ccc, ddd, eee;
		PotentialTable table;
		
		node = new ProbabilisticNode();
		aaa = node;
		node.setName("AAA");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, aHackability);
		table.setValue(1, 1f - aHackability);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		bbb = node;
		node.setName("BBB");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		table.setValue(0, bHackability);
		table.setValue(1, 1f - bHackability);
		net.addNode(node);
		
		
		node = new ProbabilisticNode();
		ccc = node;
		node.setName("CCC");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(aaa, ccc));
			net.addEdge(new Edge(bbb, ccc));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		float a = exploitViaA;
		float b = exploitViaB;
		// A and B
		table.setValue(0, (float)(a + b - a * b));
		table.setValue(1, (float)(1d - (a + b - a * b)));
		// only A, not B
		table.setValue(2, (float)(a));
		table.setValue(3, (float)(1d - a));
		// only B, not A
		table.setValue(4, (float)(b));
		table.setValue(5, (float)(1d - b));
		// neither A or B
		table.setValue(6, IMPOSSIBLE);
		table.setValue(7, ALWAYS);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		ddd = node;
		node.setName("DDD");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(ccc, ddd));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table.setValue(0, hackability);
		table.setValue(1, 1f - hackability);
		table.setValue(2, IMPOSSIBLE);
		table.setValue(3, ALWAYS);
		net.addNode(node);
		
		node = new ProbabilisticNode();
		eee = node;
		node.setName("EEE");
		node.appendState("Gehackt");
		node.appendState("Gehalten");
		table = node.getProbabilityFunction();
		table.addVariable(node);
		try {
			net.addEdge(new Edge(ddd, eee));
		} catch (InvalidParentException e) {
			e.printStackTrace();
		}
		table.setValue(0, hackability);
		table.setValue(1, 1f - hackability);
		table.setValue(2, IMPOSSIBLE);
		table.setValue(3, ALWAYS);
		net.addNode(node);
		
		
		// Inference.
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(net);
		algo.run();
		
		// Test
		assertEquals("AAA hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("AAA")).getMarginalAt(0), 0.00001d);
		assertEquals("BBB hacked mismatch", 1d, ((ProbabilisticNode)net.getNode("BBB")).getMarginalAt(0), 0.00001d);
		assertEquals("CCC hacked mismatch", 0.19d, ((ProbabilisticNode)net.getNode("CCC")).getMarginalAt(0), 0.00001d);
		assertEquals("DDD hacked mismatch", 0.019d, ((ProbabilisticNode)net.getNode("DDD")).getMarginalAt(0), 0.00001d);
		assertEquals("EEE hacked mismatch", 0.0019d, ((ProbabilisticNode)net.getNode("EEE")).getMarginalAt(0), 0.00001d);

		// Print
//		final ProbabilisticNetwork pn = net;
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
	}
}
