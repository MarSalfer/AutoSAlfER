package attackGraph;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import systemModel.Asset;
import systemModel.SystemModel;
import systemModel.Software;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import exploit.Exploit;
import exploit.PotentialExploit;

public class AttackScenarioTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public final void testAttackScenario() {
//		final SystemModel sysModel = new SystemModel();
//		final AttackerProfile attackerProfile = new AttackerProfile();
//		final HashSet<Exploit> exploits = new HashSet<Exploit>();
//		
//		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
//
//		Software t1 = new Software("Browser");
//		Software t2 = new Software("Payment Service");
//		Attractor v1 = new Attractor("Web2.0", t1);
//		Attractor v2 = new Attractor("In-App Payment Financial", t2);
//		Attractor v3 = new Attractor("In-App Payment Reputation", t2);
//		sysModel.addNode(t1);
//		sysModel.addNode(t2);
//		sysModel.addNode(v1);
//		sysModel.addNode(v2);
//		sysModel.addNode(v3);
//		
//		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", t1);
//		final PotentialExploit exploit2 = new PotentialExploit("Attractor Extraction #1", v1);
//		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", t2);
//		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2);
//		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3);
//		exploits.add(exploit1);
//		exploits.add(exploit2);
//		exploits.add(exploit3);
//		exploits.add(exploit4);
//		exploits.add(exploit5);
//		
////		Access a = new Access("Website injection", t1, 10_000);
//
//		
//		fail("Not yet implemented"); // TODO
//	}
//	
//	
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#getGraph()}.
//	 */
//	@Test
//	public final void testGetGraph() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#reset()}.
//	 */
//	@Test
//	public final void testReset() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#isAttackGraphDone()}.
//	 */
//	@Test
//	public final void testIsAttackGraphDone() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#setAttackGraphDone()}.
//	 */
//	@Test
//	public final void testSetAttackGraphDone() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#getSystemModel()}.
//	 */
//	@Test
//	public final void testGetSystemModel() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#getEntryTasks()}.
//	 */
//	@Test
//	public final void testGetEntryTasks() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#getAttractors()}.
//	 */
//	@Test
//	public final void testGetAttractors() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#chooseCheapestExploit(systemModel.Software, systemModel.Software)}.
//	 */
//	@Test
//	public final void testChooseCheapestExploit() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#costInvestedSoFar(systemModel.Software)}.
//	 */
//	@Test
//	public final void testCostInvestedSoFar() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#cost(exploit.Exploit)}.
//	 */
//	@Test
//	public final void testCost() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link attackGraph.AttackScenario#costRemaining(systemModel.Software)}.
//	 */
//	@Test
//	public final void testCostRemaining() {
//		fail("Not yet implemented"); // TODO
//	}

	
}
