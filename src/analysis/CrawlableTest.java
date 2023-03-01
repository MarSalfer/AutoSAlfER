/**
 * 
 */
package analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import analysis.comparator.CostSoFarComparator;
import analysis.comparator.ToStringComparator;
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

/**
 * @author Martin Salfer
 * @created 2016-04-02
 */
public class CrawlableTest {

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
	private PotentialExploit exploit7;
	private PotentialExploit exploit8;

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
		exploit7 = new PotentialExploit("Unrelated Exploit2", s3, new Resources(20_000d, 0d));
		exploit8 = new PotentialExploit("Unrelated Exploit3", s3, new Resources(20_000d, 0d));
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

	/**
	 * Test method for {@link analysis.Crawlable#addNewExploitOrigin(analysis.Crawlable, exploit.Exploit, float)}.
	 * This test also tests the correct result of the compareTo method of the nested class AttackVector.
	 */
	@Test
	public final void testAddNewExploitOrigin() {
		final float newPathCostToHere = 123f;
		final float newPathCostToHereBigger = 456f;
		
		assertNull(c1.getUsedExploit());
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertNull(c2.getUsedExploit());
		assertNull(c2.getAttackPredecessor());
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c2.getMinCostMeanTilHere(), 0.1f);
		
		c2.addNewExploitOrigin(c1, exploit1, newPathCostToHere); // This call must change the following results

		assertNull(c1.getUsedExploit());
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());
		assertEquals("Mismatch", newPathCostToHere, c2.getMinCostMeanTilHere(), 0.1f);

		c2.addNewExploitOrigin(c1, exploit3, newPathCostToHere); // This call must not change the following return values.

		assertNull(c1.getUsedExploit());
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());
		assertEquals("Mismatch", newPathCostToHere, c2.getMinCostMeanTilHere(), 0.1f);

		c2.addNewExploitOrigin(c1, exploit6, newPathCostToHereBigger); // This call must not change the following return values.
		
		assertNull(c1.getUsedExploit());
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertEquals("Mismatch", newPathCostToHere, c2.getMinCostMeanTilHere(), 0.1f);
	}
	
//	/**
//	 */
//	@Test
//	public final void testCompareTo() { // Traf die falsche compareTo()! Hätte die der AttackVector-Klasse treffen müssen.
//		System.out.println(c1.getCostForPathSoFar());
//		System.out.println(c2.getCostForPathSoFar());
//		System.out.println(c1.compareTo(c2));
//		System.out.println();
//
//		c1.addNewExploitOrigin(c3, exploit1, 300f); // This call must change the following results
//		System.out.println(c1.getCostForPathSoFar());
//		System.out.println(c2.getCostForPathSoFar());
//		System.out.println(c1.compareTo(c2));
//		System.out.println();
//
//		c2.addNewExploitOrigin(c3, exploit1, 400f); // This call must change the following results
//		System.out.println(c1.getCostForPathSoFar());
//		System.out.println(c2.getCostForPathSoFar());
//		System.out.println(c1.compareTo(c2));
//		System.out.println();
//
//		c2.addNewExploitOrigin(c3, exploit1, 200f); // This call must change the following results
//		System.out.println(c1.getCostForPathSoFar());
//		System.out.println(c2.getCostForPathSoFar());
//		System.out.println(c1.compareTo(c2));
//		System.out.println();
//
//		c1.addNewExploitOrigin(c3, exploit1, 100f); // This call must change the following results
//		System.out.println(c1.getCostForPathSoFar());
//		System.out.println(c2.getCostForPathSoFar());
//		System.out.println(c1.compareTo(c2));
//		System.out.println();
//	}

	/**
	 * Test method for {@link analysis.Crawlable#getMinCostMeanTilHere()}.
	 */
	@Test
	public final void testGetCostForPathSoFar() {
		final float newPathCostToHere = 123f;
		final float newPathCostToHereBigger = 456f;
		
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c2.getMinCostMeanTilHere(), 0.1f);
		
		c2.addNewExploitOrigin(c1, exploit1, newPathCostToHereBigger); // This call must change return values.
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertEquals("Mismatch", newPathCostToHereBigger, c2.getMinCostMeanTilHere(), 0.1f);

		c2.addNewExploitOrigin(c1, exploit3, newPathCostToHere); // This call must change return values.
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertEquals("Mismatch", newPathCostToHere, c2.getMinCostMeanTilHere(), 0.1f);

		c2.addNewExploitOrigin(c1, exploit6, newPathCostToHere); // This call must not change return values.
		assertEquals("Mismatch", Float.POSITIVE_INFINITY, c1.getMinCostMeanTilHere(), 0.1f);
		assertEquals("Mismatch", newPathCostToHere, c2.getMinCostMeanTilHere(), 0.1f);
	}

	
	/**
	 * Test the Exception when adding duplicate attack vectors.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testDuplicateVectorsException() {
		c2.addNewExploitOrigin(c1, exploit1, 1d);
		c2.addNewExploitOrigin(c1, exploit1, 2d);
	}
	
	
	/**
	 * Test method for {@link analysis.Crawlable#getAttackPredecessor()}.
	 */
	@Test
	public final void testGetAttackPredecessor() {
		final float newPathCostToHere = 123f;
		final float newPathCostToHereBigger = 456f;
		
		assertNull(c1.getUsedExploit());
		assertNull(c1.getAttackPredecessor());
		assertNull(c2.getAttackPredecessor());
		
		c2.addNewExploitOrigin(c1, exploit1, newPathCostToHere); // This call must change the following results
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());

		c2.addNewExploitOrigin(c1, exploit3, newPathCostToHere); // This call must not change the following return values.
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());

		c2.addNewExploitOrigin(c1, exploit6, newPathCostToHereBigger); // This call must not change the following return values.
		assertNull(c1.getAttackPredecessor());
		assertEquals("Mismatch", c1, c2.getAttackPredecessor());
	}

	/**
	 * Test method for {@link analysis.Crawlable#getUsedExploit()}.
	 */
	@Test
	public final void testGetUsedExploit() {
		final float newPathCostToHere = 123f;
		final float newPathCostToHereBigger = 456f;
		
		assertNull(c1.getUsedExploit());
		assertNull(c2.getUsedExploit());
		
		c2.addNewExploitOrigin(c1, exploit1, newPathCostToHere); // This call must change the following results
		assertNull(c1.getUsedExploit());
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());

		c2.addNewExploitOrigin(c1, exploit3, newPathCostToHere); // This call must not change the following return values.
		assertNull(c1.getUsedExploit());
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());
		
		c2.addNewExploitOrigin(c1, exploit6, newPathCostToHereBigger); // This call must not change the following return values.
		assertNull(c1.getUsedExploit());
		assertEquals("Mismatch", exploit1, c2.getUsedExploit());
	}
	
	
	
	/**
	 * Test method for {@link analysis.Crawlable#getAllExploitsAgainstTarget()}.
	 */
	@Test
	public final void testgetAllExploitsAgainstTarget() {
		System.out.println(c1.getAllExploitsAgainstTarget(c1));
		System.out.println(c1.getAllExploitsAgainstTarget(c2));
		System.out.println(c1.getAllExploitsAgainstTarget(c3));
		
		assertEquals("Mismatch", 1, c1.getAllExploitsAgainstTarget(c2).size());
		assertEquals("Mismatch", 5, c2.getAllExploitsAgainstTarget(c3).size());
		assertEquals("Mismatch", 1, c3.getAllExploitsAgainstTarget(c2).size());
	}


	/**
	 * Test the correct use of the attackVector class inside of Crawable objects.
	 * The system model will have a "multi-homed" node, where the use of the AttackVector class can be seen.
	 * AttackVectors have been set deprecated and will not be updated any more.
	 */
	@Test
	@Ignore
	public final void testAttackVectorSet() {
		Assert.assertNotNull(c1.getAttackVectorsAndCosts());
		Assert.assertNotNull(c2.getAttackVectorsAndCosts());
		Assert.assertNotNull(c3.getAttackVectorsAndCosts());

		c2.addNewExploitOrigin(c1, exploit1, 1d);
		c3.addNewExploitOrigin(c2, exploit1, 6d);
		c3.addNewExploitOrigin(c2, exploit3, 6d);
		c3.addNewExploitOrigin(c2, exploit6, 6d);
		c3.addNewExploitOrigin(c2, exploit7, 6d);
		
		Assert.assertNotNull(c1.getAttackVectorsAndCosts());
		Assert.assertNotNull(c2.getAttackVectorsAndCosts());
		Assert.assertNotNull(c3.getAttackVectorsAndCosts());
		Assert.assertNotNull(c2.getAttackVectorsAndCosts());
		assertEquals("Mismatch!",  0, c1.getAttackVectorsAndCosts().size());
		assertEquals("Mismatch!",  1, c2.getAttackVectorsAndCosts().size());
		assertEquals("Mismatch!",  4, c3.getAttackVectorsAndCosts().size());
	}
	
	public static void main(String... args) {
		LinkedList<Double> l = new LinkedList<Double>();
		System.out.println(l);
	}
}
	
