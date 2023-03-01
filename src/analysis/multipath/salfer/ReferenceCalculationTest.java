/**
 * 
 */
package analysis.multipath.salfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import analysis.Analyzer;
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

/**
 * See Calculation in "P3Salfer-Referenzrechnung.xlsx"
 * 
 * @author Martin Salfer
 * @created 26.02.2017 22:29:46
 *
 */
public class ReferenceCalculationTest {

	private AttackGraph ag;
	private HashSet<Exploit> exploits;
	private PotentialExploit exploitA;
	private PotentialExploit exploitB;
	private PotentialExploit exploitC;
	private PotentialExploit exploitCAsym;
	private PotentialExploit exploitD;
	private PotentialExploit exploitE;
	private PotentialExploit exploitF;
	private PotentialExploit exploitG;
	private Asset asset;
	private AttackScenario scenario;
	private SystemModel sysModel;
	private AttackerProfile attackerProfile;
	private Exploit exploitAsset;
	private Software swa;
	private Software swb;
	private Software swc;
	private Software swd;
	private Software swe;
	private Software swf;
	private Software swg;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sysModel = new SystemModel();
		swa = new Software("A", 0.5f);
		swb = new Software("B", 0.5f);
		swc = new Software("C", 0.5f);
		swd = new Software("D", 0.5f);
		swe = new Software("E", 0.5f);
		swf = new Software("F", 0.5f);
		swg = new Software("G", 0.5f);
		sysModel.addNode(swa);
		sysModel.addNode(swb);
		sysModel.addNode(swc);
		sysModel.addNode(swd);
		sysModel.addNode(swe);
		sysModel.addNode(swf);
		sysModel.addNode(swg);

		/* Communication Definition */
		/* A can send to B and C. B and C can send to D. */
		final CommunicationMedium atoBC = new CommunicationMedium("AzuBC");
		final CommunicationMedium bcToD = new CommunicationMedium("BCzuD");
		final CommunicationMedium cToE = new CommunicationMedium("CzuE");
		final CommunicationMedium eToFG = new CommunicationMedium("EzuFG");
		swa.registerWritesTo(atoBC);
		swb.registerWritesTo(bcToD);
		swc.registerWritesTo(bcToD);
		swc.registerWritesTo(cToE);
		swe.registerWritesTo(eToFG);
		swb.registerReadsFrom(atoBC);
		swc.registerReadsFrom(atoBC);
		swd.registerReadsFrom(bcToD);
		swe.registerReadsFrom(cToE);
		swf.registerReadsFrom(eToFG);
		swg.registerReadsFrom(eToFG);

		asset = new Asset("ZielD", swd);
		sysModel.addNode(asset);
		
		exploitA = new PotentialExploit("ExploitA", swa, new Resources(1_000d, 200d));
		exploitB = new PotentialExploit("ExploitB", swb, new Resources(1_000d, 200d));
		exploitC = new PotentialExploit("ExploitC", swc, new Resources(1_000d, 200d));
		exploitCAsym = new PotentialExploit("ExploitCAsym", swc, new Resources(3_000d, 200d));
		exploitD = new PotentialExploit("ExploitD", swd, new Resources(1_000d, 200d));
		exploitE = new PotentialExploit("ExploitE", swe, new Resources(1_000d, 200d));
		exploitF = new PotentialExploit("ExploitF", swf, new Resources(1_000d, 200d));
		exploitG = new PotentialExploit("ExploitG", swg, new Resources(1_000d, 200d));
		exploitAsset = new PotentialExploit("Extraction", asset, new Resources(0d,0d));
		putExploitsForSymmetricScenario();

		attackerProfile = new AttackerProfile("RefCalcAttacker", 1, new Resources(3_000d, 1_000d));
		attackerProfile.addAccess(new Access("AccessToA", swa, 1));
		
		recrawl("ReferenceCalculationSymmetric");
		
		/* Test Assertions*/
		assertNotNull(ag);
	}

	/**
	 * @param scenarioName
	 */
	private void recrawl(final String scenarioName) {
		scenario = new AttackScenario(scenarioName, sysModel, attackerProfile, exploits);
		ag = new Crawler(scenario).crawlAndGetAttackGraphOnSalfer(false);
	}

	private void putExploitsForSymmetricScenario() {
		exploits = new HashSet<Exploit>();
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitC);
		exploits.add(exploitD);
		exploits.add(exploitE);
		exploits.add(exploitF);
		exploits.add(exploitG);
		exploits.add(exploitAsset);
	}

	private void putExploitsForBOnlyPath() {
		exploits = new HashSet<Exploit>();
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitD);
		exploits.add(exploitE);
		exploits.add(exploitF);
		exploits.add(exploitG);
		exploits.add(exploitAsset);
	}
	
	private void putExploitsForAsymmetricViaCOnlyScenario() {
		exploits = new HashSet<Exploit>();
		exploits.add(exploitA);
		exploits.add(exploitCAsym);
		exploits.add(exploitD);
		exploits.add(exploitE);
		exploits.add(exploitF);
		exploits.add(exploitG);
		exploits.add(exploitAsset);
	}

	private void putExploitsForAsymmetricScenario() {
		exploits = new HashSet<Exploit>();
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitCAsym);
		exploits.add(exploitD);
		exploits.add(exploitE);
		exploits.add(exploitF);
		exploits.add(exploitG);
		exploits.add(exploitAsset);
	}

	/**
	 * Costs on targets.
	 * Model calculation:
	 *  		 µ1			sigma1		µ2		sigma2
	 *  A		 1.000 € 	 200 € 	 1.000 € 	 200 €
	 *  B		 2.000 € 	 283 € 	 2.000 € 	 282,8427125 €
	 *  C		 2.000 € 	 283 € 	 4.000 € 	 282,8427125 €
	 *  DviaB	 3.000 € 	 346 € 	 3.000 € 	 346,4101615 €
	 *  DviaC	 3.000 € 	 346 € 	 5.000 € 	 346,4101615 € 
	 *  D (opt)	 3.000 € 	 346 € 	 3.000 € 	 346 € 
	 *  D (suc)	 3.000 € 	 346 € 	 4.000 € 	 346 € 
	 *  All		 3.500 € 	 374 € 	 4.333 € 	 346 €
	 *  E	 	 3.000 € 	 346 € 	 5.000 € 	 346 €
	 *  F		 4.000 € 	 400 € 	 -			 -
	 *  G	 	 4.000 € 	 400 € 	 -		 	 - 
	 *  
	 * Note: Node E, F and G yield null in the tests as these nodes are not part 
	 * of the attack graph due to their non-contribution to asset compromise.

	 */
	@Test
	public final void testCostCalculation() {
		Resources costTillAsset;
		
		/* symmetric scenario */
		costTillAsset = ag.getCostForOptimalPathTo(swa);
		assertEquals(1_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  200, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swb);
		assertEquals(2_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swc);
		assertEquals(2_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swd);
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForPathsThrough(swd);
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForAllPaths();
		assertEquals(3_500, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  374, costTillAsset.getMoneyStandardDeviation(), 1d);
		assertNull(ag.getCostForOptimalPathTo(swe));
		assertNull(ag.getCostForOptimalPathTo(swf));
		assertNull(ag.getCostForOptimalPathTo(swg));
		
		
		/* asymmetric scenario */
		putExploitsForAsymmetricScenario();
		recrawl("ReferenceCalculationAsymmetricViaBoth");
		costTillAsset = ag.getCostForOptimalPathTo(swa);
		assertEquals(1_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  200, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swb);
		assertEquals(2_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swc);
		assertEquals(4_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swd);
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForPathsThrough(swd);
		assertEquals(4_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForAllPaths();
		assertEquals(4_333, costTillAsset.getMoneyExpectedValue(), 1d);
		assertEquals(  346, costTillAsset.getMoneyStandardDeviation(), 1d);
		assertNull(ag.getCostForOptimalPathTo(swe));
		assertNull(ag.getCostForOptimalPathTo(swf));
		assertNull(ag.getCostForOptimalPathTo(swg));
		
		/* asymmetric scenario via B only */
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationAsymmetricViaB");
		costTillAsset = ag.getCostForOptimalPathTo(swa);
		assertEquals(1_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  200, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swb);
		assertEquals(2_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swc);
		assertNull(costTillAsset);
		costTillAsset = ag.getCostForOptimalPathTo(swd);
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForPathsThrough(swd);
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForAllPaths();
		assertEquals(3_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346, costTillAsset.getMoneyStandardDeviation(), 1d);
		assertNull(ag.getCostForOptimalPathTo(swe));
		assertNull(ag.getCostForOptimalPathTo(swf));
		assertNull(ag.getCostForOptimalPathTo(swg));


		/* asymmetric scenario via C only */ 
		putExploitsForAsymmetricViaCOnlyScenario();
		recrawl("ReferenceCalculationAsymmetricViaC");
		costTillAsset = ag.getCostForOptimalPathTo(swa);
		assertEquals(1_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  200, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swb);
		assertNull(costTillAsset);
		costTillAsset = ag.getCostForOptimalPathTo(swc);
		assertEquals(4_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  282.8427125, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostForOptimalPathTo(swd);
		assertEquals(5_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForPathsThrough(swd);
		assertEquals(5_000, costTillAsset.getMoneyExpectedValue(), 0.1d);
		assertEquals(  346.4101615, costTillAsset.getMoneyStandardDeviation(), 0.1d);
		costTillAsset = ag.getCostExpectedValueForAllPaths();
		assertEquals(5_000, costTillAsset.getMoneyExpectedValue(), 0.1d); // ad-hoc calculation.
		assertEquals(  346, costTillAsset.getMoneyStandardDeviation(), 1d); // ad-hoc calculation.
		assertNull(ag.getCostForOptimalPathTo(swe));
		assertNull(ag.getCostForOptimalPathTo(swf));
		assertNull(ag.getCostForOptimalPathTo(swg));
	}


	/**
	 * Test the affordability for attacking the targets.
	 * Model calculation:
	 *			p			p
	 * A		97,507%		97,507%
	 * B		83,204%		83,204%
	 * C		83,204%		16,796%
	 * DviaB	50,000%		50,000%
	 * DviaC	50,000%		2,939%
	 * D (opt)	50,000%		50,000%
	 * D (suc)	50,000%		17,235%
	 * All		31,979%		10,386%
	 * E		50,000%		2,939%
	 * F		17,658%		-
	 * G		17,658%		-
	 * 
	 * Note: Node E, F and G yield 0 in the tests as these nodes are not part 
	 * of the attack graph due to their non-contribution to successful paths.
	 */
	@Test
	public final void testAffordability() {
		// symmetric scenario both
		assertEquals(0.97507, ag.getAffordabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.97507, ag.getAffordabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swg),0.00001d);	
		assertEquals(0.31979, ag.getAffordabilityProbabilityMeanForAllPaths(),0.00001d);

		// symmetric scenario via B
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationSymmetricViaB");
		assertEquals(0.97507, ag.getAffordabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.97507, ag.getAffordabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityMeanForAllPaths(),0.00001d); // ad-hoc calculation

		//asymmetric scenario both
		putExploitsForAsymmetricScenario();
		recrawl("ReferenceCalculationAsymmetricViaBoth");
		assertEquals(0.97507, ag.getAffordabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.16796, ag.getAffordabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.97507, ag.getAffordabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.16796, ag.getAffordabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.17235, ag.getAffordabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.10386, ag.getAffordabilityProbabilityMeanForAllPaths(),0.00001d);
		
		// asymmetric scenario via B only
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationAsymmetricViaB");
		assertEquals(0.97507, ag.getAffordabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.97507, ag.getAffordabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.83204, ag.getAffordabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.50000, ag.getAffordabilityProbabilityMeanForAllPaths(),0.00001d); // ad-hoc calculation.
		
		// asymmetric scenario via C only
		putExploitsForAsymmetricViaCOnlyScenario();
		recrawl("ReferenceCalculationAsymmetricViaC");
		assertEquals(0.97507, ag.getAffordabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.16796, ag.getAffordabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.02939, ag.getAffordabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.97507, ag.getAffordabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.16796, ag.getAffordabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.02939, ag.getAffordabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getAffordabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.02939, ag.getAffordabilityProbabilityMeanForAllPaths(),0.00001d); // ad-hoc calculation.
	}
	
	/**
	 * Test the vulnerability probability calculation.
	 * Model calculation:
	 * 				p			p
	 * A			50,000%		50,000%
	 * B			25,000%		25,000%
	 * C			25,000%		25,000%
	 * DviaB		12,500%		12,500%
	 * DviaC		12,500%		12,500%
	 * D (opt)		12,500%		12,500%
	 * D (suc)		12,500%		12,500%
	 * All			9,375%		12,500%
	 * E			12,500%		12,500%
	 * F			6,250%		6,250%
	 * G			6,250%		6,250%
	 * 
	 * Note: Node E, F and G yield 0 in the tests as these nodes are not part 
	 * of the attack graph due to their non-contribution to successful paths.
	 */
	@Test
	public final void testVulnerabilityCalculation() {
		// symmetric scenario
		assertEquals(0.50000, ag.getVulnerabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.50000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.09375, ag.getVulnerabilityProbabilityMeanForAllPaths(),0.00001d);

		// asymmetric scenario
		putExploitsForAsymmetricScenario();
		recrawl("ReferenceCalculationAsymmetricViaBoth");
		assertEquals(0.50000, ag.getVulnerabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.50000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForAllPaths(),0.00001d);

		// asymmetric scenario via B only
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationAsymmetricViaB");
		assertEquals(0.50000, ag.getVulnerabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.     , ag.getVulnerabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.50000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.     , ag.getVulnerabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForAllPaths(),0.00001d);
		
		// asymmetric scenario via C only
		putExploitsForAsymmetricViaCOnlyScenario();
		recrawl("ReferenceCalculationAsymmetricViaC");
		assertEquals(0.50000, ag.getVulnerabilityProbabilityForOptimalPathTo(swa),0.00001d);
		assertEquals(0.     , ag.getVulnerabilityProbabilityForOptimalPathTo(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityForOptimalPathTo(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityForOptimalPathTo(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityForOptimalPathTo(swg),0.00001d);
		assertEquals(0.50000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swa),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swb),0.00001d);
		assertEquals(0.25000, ag.getVulnerabilityProbabilityMeanForPathsThrough(swc),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForPathsThrough(swd),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swe),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swf),0.00001d);
		assertEquals(0      , ag.getVulnerabilityProbabilityMeanForPathsThrough(swg),0.00001d);
		assertEquals(0.12500, ag.getVulnerabilityProbabilityMeanForAllPaths(),0.00001d); // ad-hoc calc.: 1xD, 1xE
	}
	
	/**
	 * Test the attackability probability calculation.
	 * Model calculation:
	 * 				p			p
	 * A			48,753%		48,753%
	 * B			20,801%		20,801%
	 * C			20,801%		4,199%
	 * DviaB		6,250%		6,250%
	 * DviaC		6,250%		0,367%
	 * D (opt)		6,250%		6,250%
	 * D (suc)		6,250%		2,154%
	 * All	 		2,998%		1,298%
	 * E			6,250%		0,367%
	 * F			1,104%		0,017%
	 * G			1,104%		0,017%
	 * 
	 * Note: Node E, F and G yield 0 in the tests as these nodes are not part 
	 * of the attack graph due to their non-contribution to successful paths.
	 */
	@Test
	public final void testAttackabilityCalculation() {
		// symmetric scenario both
		assertEquals(0.48753, ag.getAttackabilityProbabilityForOptimalPathTo(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityForOptimalPathTo(swb), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityForOptimalPathTo(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityForOptimalPathTo(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swg), 0.000_01d);
		assertEquals(0.48753, ag.getAttackabilityProbabilityMeanForPathsThrough(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityMeanForPathsThrough(swb), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityMeanForPathsThrough(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityMeanForPathsThrough(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swg), 0.000_01d);
		assertEquals(0.02998, ag.getAttackabilityProbabilityMeanForAllPaths(), 0.000_01d);


		// symmetric scenario only B
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationSymmetricViaB");
		assertEquals(0.48753, ag.getAttackabilityProbabilityForOptimalPathTo(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityForOptimalPathTo(swb), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityForOptimalPathTo(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swg), 0.000_01d);
		assertEquals(0.48753, ag.getAttackabilityProbabilityMeanForPathsThrough(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityMeanForPathsThrough(swb), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityMeanForPathsThrough(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityMeanForPathsThrough(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swg), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityMeanForAllPaths(), 0.000_01d);

		// asymmetric scenario
		putExploitsForAsymmetricScenario();
		recrawl("ReferenceCalculationAsymmetricViaBoth");
		assertEquals(0.48753, ag.getAttackabilityProbabilityForOptimalPathTo(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityForOptimalPathTo(swb), 0.000_01d);
		assertEquals(0.04199, ag.getAttackabilityProbabilityForOptimalPathTo(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityForOptimalPathTo(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityForOptimalPathTo(swg), 0.000_01d);
		assertEquals(0.48753, ag.getAttackabilityProbabilityMeanForPathsThrough(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityMeanForPathsThrough(swb), 0.000_01d);
		assertEquals(0.04199, ag.getAttackabilityProbabilityMeanForPathsThrough(swc), 0.000_01d);
		assertEquals(0.02154, ag.getAttackabilityProbabilityMeanForPathsThrough(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swg), 0.000_01d);
		assertEquals(0.01298, ag.getAttackabilityProbabilityMeanForAllPaths(), 0.000_01d);
		
		// asymmetric scenario via B only
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationAsymmetricViaB");
		assertEquals(0.48753, ag.getAttackabilityProbabilityForOptimalPathTo(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityForOptimalPathTo(swb), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityForOptimalPathTo(swd), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swe), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swf), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swg), 0.000_01d);
		assertEquals(0.48753, ag.getAttackabilityProbabilityMeanForPathsThrough(swa), 0.000_01d);
		assertEquals(0.20801, ag.getAttackabilityProbabilityMeanForPathsThrough(swb), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityMeanForPathsThrough(swc), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityMeanForPathsThrough(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swg), 0.000_01d);
		assertEquals(0.06250, ag.getAttackabilityProbabilityMeanForAllPaths(), 0.000_01d); // ad-hoc calc.: 1xD

		// asymmetric scenario via C only
		putExploitsForAsymmetricViaCOnlyScenario();
		recrawl("ReferenceCalculationAsymmetricViaC");
		assertEquals(0.48753, ag.getAttackabilityProbabilityForOptimalPathTo(swa), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swb), 0.000_01d);
		assertEquals(0.04199, ag.getAttackabilityProbabilityForOptimalPathTo(swc), 0.000_01d);
		assertEquals(0.00367, ag.getAttackabilityProbabilityForOptimalPathTo(swd), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swe), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swf), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityForOptimalPathTo(swg), 0.000_01d);
		assertEquals(0.48753, ag.getAttackabilityProbabilityMeanForPathsThrough(swa), 0.000_01d);
		assertEquals(0.     , ag.getAttackabilityProbabilityMeanForPathsThrough(swb), 0.000_01d);
		assertEquals(0.04199, ag.getAttackabilityProbabilityMeanForPathsThrough(swc), 0.000_01d);
		assertEquals(0.00367, ag.getAttackabilityProbabilityMeanForPathsThrough(swd), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swe), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swf), 0.000_01d);
		assertEquals(0      , ag.getAttackabilityProbabilityMeanForPathsThrough(swg), 0.000_01d);
		assertEquals(0.00367, ag.getAttackabilityProbabilityMeanForAllPaths(), 0.000_01d); // ad-hoc calc.: 1xD, 1xE
	}
	
	
	@Test
	public final void testNumberOfPaths() {
		assertEquals(4, ag.getNumberOfConsideredPaths());
		assertEquals(2, ag.getNumberOfSuccessfulPaths(asset));
		assertEquals(2, ag.getAmountOfRecordedPathExpansionsIn(asset));
		assertEquals(1, ag.getAmountOfRecordedPathExpansionsIn(swa));
		assertEquals(1, ag.getAmountOfRecordedPathExpansionsIn(swb));
		assertEquals(1, ag.getAmountOfRecordedPathExpansionsIn(swc));
		assertEquals(2, ag.getAmountOfRecordedPathExpansionsIn(swd));
		assertEquals(0, ag.getAmountOfRecordedPathExpansionsIn(swe));
		assertEquals(0, ag.getAmountOfRecordedPathExpansionsIn(swf));
		assertEquals(0, ag.getAmountOfRecordedPathExpansionsIn(swg));
	}
	
	/**
	 * Test the raise through Poincaré-Sylvester combination.
	 * Model calculations:
	 * 					relative	absolute	relative	absolute
	 * vulnProb			87,500%		10,938% 	87,500% 	10,938%
	 * affordability	50,000%		25,000%		2,939%		1,470%
	 * attackability	93,750% 	5,859%		5,511%		0,344%
	 * 
	 * @deprecated The Poincaré-Sylvester combination is in my stochastic model not regarded useful any more.
	 */
	@Test
	@Ignore
	@Deprecated
	public final void testMultiPathProbabilityRaise(){
		// symmetric scenario
		double vulnProbMulti =   ag.getVulnerabilityProbabilityMeanForPathsThrough(asset);
		double attackProbMulti = ag.getOverallAttackabilityProbabilityForAtLeastOnePathsTo(asset);
		double affordProbMulti = ag.getAffordabilityProbabilityMeanForPathsThrough(asset);
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationSymmetricViaB");
		double vulnProbSingle = ag.getVulnerabilityProbabilityMeanForPathsThrough(asset);
		double attackProbSingle = ag.getOverallAttackabilityProbabilityForAtLeastOnePathsTo(asset);
		double affordProbSingle = ag.getAffordabilityProbabilityMeanForPathsThrough(asset);
		
		assertEquals(0.87500, vulnProbMulti/vulnProbSingle - 1, 0.000_01d);
		assertEquals(0.10938, vulnProbMulti - vulnProbSingle, 0.000_01d);
		assertEquals(0.93750, attackProbMulti/attackProbSingle - 1, 0.000_01d);
		assertEquals(0.05859, attackProbMulti - attackProbSingle, 0.000_01d);
		assertEquals(0.50000, affordProbMulti/affordProbSingle - 1, 0.000_01d);
		assertEquals(0.25000, affordProbMulti - affordProbSingle, 0.000_01d);

		// asymmetric scenario
		putExploitsForBOnlyPath();
		recrawl("ReferenceCalculationAsymmetricViaB");
		vulnProbSingle = ag.getVulnerabilityProbabilityMeanForPathsThrough(asset);
		attackProbSingle = ag.getOverallAttackabilityProbabilityForAtLeastOnePathsTo(asset);
		affordProbSingle = ag.getAffordabilityProbabilityMeanForPathsThrough(asset);
		putExploitsForAsymmetricScenario();
		recrawl("ReferenceCalculationAsymmetricViaBoth");
		vulnProbMulti = ag.getVulnerabilityProbabilityMeanForPathsThrough(asset);
		attackProbMulti = ag.getOverallAttackabilityProbabilityForAtLeastOnePathsTo(asset);
		affordProbMulti = ag.getAffordabilityProbabilityMeanForPathsThrough(asset);
		
		assertEquals(0.87500, vulnProbMulti/vulnProbSingle - 1, 0.000_01d);
		assertEquals(0.10938, vulnProbMulti - vulnProbSingle, 0.000_01d);
		assertEquals(0.05511, attackProbMulti/attackProbSingle - 1, 0.000_01d);
		assertEquals(0.00344, attackProbMulti - attackProbSingle, 0.000_01d);
		assertEquals(0.02939, affordProbMulti/affordProbSingle - 1, 0.000_01d);
		assertEquals(0.01470, affordProbMulti - affordProbSingle, 0.000_01d);
	}
	
	public static void main(String... args) {
		new ReferenceCalculationTest().probabilityAccumulatorDiscrepancyDissimilarTest();
	}

	/**
	 * The difference between the calculation methods for up to 1'000 iterations is below 1E-14.
	 * Ergo, the calculation precision is good.
	 */
	@Test
	public final void probabilityAccumulatorDiscrepancyTest() {
		System.out.println("Probability accumualtor discrepancy test:");
		double affordabilityAccumulator = 0d;
		Resources cost = new Resources(5_000d, 1_000d);
		Resources budget = new Resources(3_000d, 1_000d);
		int i = 1;
		final double directGauß = Analyzer.calculateAttackerAffordability(budget, cost);
		for ( ; i<=1_000 ; i++) {
			final double probabilityOfPositiveOutcome = directGauß;
			affordabilityAccumulator += probabilityOfPositiveOutcome;
		}
		i--;
		final double accDivided = affordabilityAccumulator/i;
		System.out.println("/i=" + accDivided);
		System.out.println("DirectGauß: " + directGauß);
		System.out.println("Diff: " + (accDivided - directGauß));
		assertEquals(directGauß, accDivided, 1e-14);
	}
	
	/**
	 * The difference between the calculation methods for only a few iteration becomes huge: around 5 percent points.
	 * TODO: Find out the cause and consider in stochastic model appropriately.
	 */
	@Test
	@Ignore
	public final void probabilityAccumulatorDiscrepancyDissimilarTest() {
		System.out.println("Probability accumualtor discrepancy dissimilar test:");
		double affordabilityAccumulator = 0d;
		final double costDev = 1_000d;
		final double cost1Mean = 3_000d;
		final double cost2Mean = 5_000d;
		Resources cost1 = new Resources(cost1Mean, costDev);
		Resources cost2 = new Resources(cost2Mean, costDev);
		Resources budget = new Resources(3_000d, 1_000d);
		int i = 1;
		final int cost1Amount = 10;
		for ( ; i<=cost1Amount ; i++) {
			final double probabilityOfPositiveOutcome = Analyzer.calculateAttackerAffordability(budget, cost1);
			affordabilityAccumulator += probabilityOfPositiveOutcome;
		}
		i--;
		for ( ; i<=cost1Amount*2 ; i++) {
			final double probabilityOfPositiveOutcome = Analyzer.calculateAttackerAffordability(budget, cost2);
			affordabilityAccumulator += probabilityOfPositiveOutcome;
		}
		i--;
		Resources costMean = new Resources((cost1Mean+cost2Mean)/2, costDev);
		final double directGauß = Analyzer.calculateAttackerAffordability(budget, costMean);
		final double accDivided = affordabilityAccumulator/i;
		System.out.println("/i=" + accDivided);
		System.out.println("DirectGauß: " + directGauß);
		System.out.println("Diff: " + (accDivided - directGauß));
		assertEquals(directGauß, accDivided, 1e-13);
	}

}
