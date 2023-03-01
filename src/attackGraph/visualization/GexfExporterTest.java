/**
 * 
 */
package attackGraph.visualization;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;

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

/**
 * @author Martin Salfer
 * @created 13.03.2017 17:13:49
 *
 */
public class GexfExporterTest {

	private static final String SAMPLE_FILE_SYMMETRIC  = "output/test/GexfExporterTest-Symmetric.gexf";
	private static final String SAMPLE_FILE_ASYMMETRIC = "output/test/GexfExporterTest-Asymmetric.gexf";

	private AttackGraph ag;
	private HashSet<Exploit> exploits = new HashSet<Exploit>();
	private PotentialExploit exploitCAsym;
	private PotentialExploit exploitCSym;
	private Asset ad;
	private Crawler c;
	private PotentialExploit exploitB;
	private PotentialExploit exploitA;
	private PotentialExploit exploitD;
	private AttackScenario scenario;
	private SystemModel sysModel;
	private AttackerProfile attackerProfile;
	private Exploit exploitAD;
	private Software swa;
	private Software swb;
	private Software swc;
	private Software swd;


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

		ad = new Asset("ZielD", swd);
		sysModel.addNode(ad);
		
		exploitA = new PotentialExploit("ExploitA", swa, new Resources(1_000d, 200d));
		exploitB = new PotentialExploit("ExploitB", swb, new Resources(1_000d, 200d));
		exploitCSym = new PotentialExploit("ExploitC", swc, new Resources(1_000d, 200d));
		exploitCAsym = new PotentialExploit("ExploitCAsym", swc, new Resources(3_000d, 200d));
		exploitD = new PotentialExploit("ExploitD", swd, new Resources(1_000d, 200d));
		exploitAD = new PotentialExploit("ExtractionAD", ad, new Resources(0d,0d));
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitCSym);
		exploits.add(exploitD);
		exploits.add(exploitAD);

		attackerProfile = new AttackerProfile("Attacker", 1, new Resources(3_000d, 1_000d));
		attackerProfile.addAccess(new Access("AccessA", swa, 1));
		
		scenario = new AttackScenario("AccumulatorTestScenarioSymmetric", sysModel, attackerProfile, exploits);
		c = new Crawler(scenario);
		ag = c.crawlAndGetAttackGraphOnSalfer(false);
		
	}


	private void recrawlForAsymmetricScenario() {
		exploits = new HashSet<Exploit>();
		exploits.add(exploitA);
		exploits.add(exploitB);
		exploits.add(exploitCAsym);
		exploits.add(exploitD);
		exploits.add(exploitAD);
		scenario = new AttackScenario("AccumulatorTestScenarioAsymmetricViaBoth", sysModel, attackerProfile, exploits);
		c = new Crawler(scenario);
		ag = c.crawlAndGetAttackGraphOnSalfer(false);
	}

	/**
	 * Test method for {@link attackGraph.visualization.GexfExporter#exportAttackGraphToGexfFile(attackGraph.AttackGraph, java.lang.String)}.
	 */
	@Test
	public final void testExportAttackGraphToGexfFile() {
		new File(SAMPLE_FILE_ASYMMETRIC).delete();
		new File(SAMPLE_FILE_SYMMETRIC).delete();
		assertFalse(new File(SAMPLE_FILE_ASYMMETRIC).exists());
		assertFalse(new File(SAMPLE_FILE_SYMMETRIC).exists());
		assertEquals(0, new File(SAMPLE_FILE_SYMMETRIC).length());
		assertEquals(0, new File(SAMPLE_FILE_ASYMMETRIC).length());
		
		
		new GexfExporter().exportAttackGraphToGexfFile(ag, SAMPLE_FILE_SYMMETRIC);
		recrawlForAsymmetricScenario();
		new GexfExporter().exportAttackGraphToGexfFile(ag, SAMPLE_FILE_ASYMMETRIC);
			
		/* Test Assertions*/
		assertNotNull(ag);
		assertTrue(new File(SAMPLE_FILE_ASYMMETRIC).exists());
		assertTrue(new File(SAMPLE_FILE_SYMMETRIC).exists());
		assertTrue(Math.abs(3_600 - new File(SAMPLE_FILE_SYMMETRIC).length()) <= 100); // 100 Byte difference only allowed.
		assertTrue(Math.abs(3_654 - new File(SAMPLE_FILE_ASYMMETRIC).length()) <= 100); // 100 Byte difference only allowed.
	}

}
