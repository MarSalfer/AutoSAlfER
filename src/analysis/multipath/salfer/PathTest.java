package analysis.multipath.salfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.PriorityQueue;

import org.junit.Before;
import org.junit.Test;

import analysis.Crawlable;
import analysis.CrawlableGraphNode;
import analysis.CrawledAsset;
import analysis.comparator.PathBiggestFirstComparator;
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

/**
 * See Model Calculation in "Multipfad-Testrechnung.xlsx"
 * 
 * @author Martin Salfer
 * @created 22.02.2017 15:25:45
 *
 */
public class PathTest {

private HashSet<Exploit> exploits = new HashSet<Exploit>();
private PotentialExploit exploitC;
private Asset aa;
private Asset ab;
private Asset ac;
private Asset ad;
private PotentialExploit exploitB;
private PotentialExploit exploitA;
private PotentialExploit exploitD;
private AttackScenario scenario;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		/* System model */
		final SystemModel sysModel = new SystemModel();
		final Software swa = new Software("A", 0.5f);
		final Software swb = new Software("B", 0.5f);
		final Software swc = new Software("C", 0.5f);
		final Software swd = new Software("D", 0.5f);
		sysModel.addNode(swa);
		sysModel.addNode(swb);
		sysModel.addNode(swc);
		sysModel.addNode(swd);

		/* Communication Definition */
		/* A can send to B and C. B and C can send to D. */
		final CommunicationMedium atoBC = new CommunicationMedium("AzuBC");
		final CommunicationMedium bcToD = new CommunicationMedium("BCzuD");
		swa.registerWritesTo(atoBC);
		swb.registerWritesTo(bcToD);
		swc.registerWritesTo(bcToD);
		swb.registerReadsFrom(atoBC);
		swc.registerReadsFrom(atoBC);
		swd.registerReadsFrom(bcToD);

		aa = new Asset("ZielA", swa);
		ab = new Asset("ZielB", swb);
		ac = new Asset("ZielC", swc);
		ad = new Asset("ZielD", swd);
		sysModel.addNode(aa);
		sysModel.addNode(ab);
		sysModel.addNode(ac);
		sysModel.addNode(ad);
		
		exploitA = new PotentialExploit("ExploitA", swa, new Resources(1_000d, 200d));
		exploitB = new PotentialExploit("ExploitB", swb, new Resources(1_000d, 200d));
		exploitC = new PotentialExploit("ExploitC", swc, new Resources(1_000d, 200d));
		exploitD = new PotentialExploit("ExploitD", swd, new Resources(1_000d, 200d));
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitC);
		exploits.add(exploitD);

		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Attacker", 1, new Resources(3_000d, 1_000d));
		attackerProfile.addAccess(new Access("AccessA", swa, 1));
		
		scenario = new AttackScenario("PathTestScenario", sysModel, attackerProfile, exploits);
	}


	/**
	 * Test method for {@link analysis.multipath.salfer.Path#Path(types.Resources, double, java.util.HashSet, Crawlable)}.
	 */
	@Test
	public final void testPath() {
		final ArrayList<CrawlableGraphNode> visitedNodes = new ArrayList<CrawlableGraphNode>();
		final ArrayList<Exploit> usedExploits = new ArrayList<Exploit>();
		final CrawledAsset position = new CrawledAsset(scenario, aa);
		visitedNodes.add(position);
		final double vulnLikelihood = 0.5d;
		Path a = new Path(new Resources(1_000d,0d), vulnLikelihood, visitedNodes, usedExploits);
		Path b = new Path(new Resources(2_000d,0d), vulnLikelihood, visitedNodes, usedExploits);
		Path c = new Path(new Resources(3_000d,0d), vulnLikelihood, visitedNodes, usedExploits);
		Path d = new Path(new Resources(4_000d,0d), vulnLikelihood, visitedNodes, usedExploits);
		Path e = new Path(new Resources(5_000d,0d), vulnLikelihood, visitedNodes, usedExploits);
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		assertNotNull(d);
		assertNotNull(e);
		assertEquals(position, a.getPosition());
		assertTrue(a.hasVisited(position));
		assertEquals(vulnLikelihood, a.getVulnerabilityProbability(), 0.001d);

		PriorityQueue<Path> queueBiggestFirst = new PriorityQueue<Path>(new PathBiggestFirstComparator());
		queueBiggestFirst.add(d);
		queueBiggestFirst.add(a);
		queueBiggestFirst.add(c);
		queueBiggestFirst.add(e);
		queueBiggestFirst.add(b);
		assertEquals(e, queueBiggestFirst.poll());
		assertEquals(d, queueBiggestFirst.poll());
		assertEquals(c, queueBiggestFirst.poll());
		assertEquals(b, queueBiggestFirst.poll());
		assertEquals(a, queueBiggestFirst.poll());
		assertEquals(null, queueBiggestFirst.poll());
		
		PriorityQueue<Path> queueSmallestFirst = new PriorityQueue<Path>();
		queueSmallestFirst.add(d);
		queueSmallestFirst.add(a);
		queueSmallestFirst.add(c);
		queueSmallestFirst.add(e);
		queueSmallestFirst.add(b);
		assertEquals(a, queueSmallestFirst.poll());
		assertEquals(b, queueSmallestFirst.poll());
		assertEquals(c, queueSmallestFirst.poll());
		assertEquals(d, queueSmallestFirst.poll());
		assertEquals(e, queueSmallestFirst.poll());
		assertEquals(null, queueSmallestFirst.poll());

		ListIterator<CrawlableGraphNode> i = a.getNodesIteratorAtEnd();
		assertFalse(i.hasNext());
		assertTrue(i.hasPrevious());
		Crawlable cReturned = i.previous();
		assertEquals(position, cReturned);
		assertTrue(i.hasNext());
		assertFalse(i.hasPrevious());
	}
	
}
