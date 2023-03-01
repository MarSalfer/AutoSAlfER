package analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import systemModel.Asset;
import systemModel.Capability;
import systemModel.CommunicationMedium;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;
import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import attackerProfile.Skill;
import exploit.Exploit;
import exploit.PotentialExploit;

public class AnalyzerTest {

	/* Precision lacks by manual calculation */
	private static final double PRECISION_JAVA = 1e-300d; // calculated purely with java.
	private static final double PRECISION_OFFICE = 1e-6d; // calculated with Office.
	private static final double PRECISION_TABLES = 1e-2d; // calculated with look up tables, here Gaussian distribution tables.
	
	private AttackGraph attackGraph;
	private final Set<Asset> attractors = new HashSet<Asset>(3);
	private Asset attractorWeb;
	private Asset attractorPay1;
	private Asset attractorPay2;
	private final Set<Asset> attractorsPayment = new HashSet<Asset>(2);
	
	@Before
	public void setUp() throws Exception {
		
		/* Configuration */
		final int amountOfUselessSoftware = 100;
		
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Software swBrowser = new Software("Browser");
		Software swPayment = new Software("Payment Service");
		List<Software> uselessSoftware = new ArrayList<Software>(amountOfUselessSoftware);
		for (int i = 1; i <= amountOfUselessSoftware; i++) {
			uselessSoftware.add(new Software("Useless Software"));
		}
		
		sysModel.addNode(swBrowser);
		sysModel.addNode(swPayment);
		for (Software s : uselessSoftware) {
			sysModel.addNode(s);
		}
		
		// Primary Communication Medium
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		swBrowser.registerReadsWritesOn(dBus);
		swPayment.registerReadsWritesOn(dBus);
		for (Software s : uselessSoftware) {
			s.registerReadsWritesOn(dBus);
		}
		
		// Secondary Communication Medium
		CommunicationMedium proprietaryIpc = new CommunicationMedium("ProprietaryIPC");
		swBrowser.registerReadsWritesOn(proprietaryIpc);
		swPayment.registerReadsWritesOn(proprietaryIpc);
		for (Software s : uselessSoftware) {
			s.registerReadsWritesOn(proprietaryIpc);
		}
		
		
		Asset v1 = new Asset("Web2.0", swBrowser, new Resources(0.045, 0.02d));
		Asset v2 = new Asset("In-App Payment Financial", swPayment);
		Asset v3 = new Asset("In-App Payment Reputation", swPayment);
		sysModel.addNode(v1);
		sysModel.addNode(v2);
		sysModel.addNode(v3);
		attractors.add(v1);
		attractors.add(v2);
		attractors.add(v3);
		attractorWeb = v1;
		attractorPay1 = v2;
		attractorPay2 = v3;
		attractorsPayment.add(attractorPay1);
		attractorsPayment.add(attractorPay2);

		
		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 5, new Resources(350_000, 100_000d)); // +50k b/c of 2 sigma cut off.
		// TODO recalculate values below b/c of 350 k budget instead of 300 k.
		attackerProfile.addAccess(new Access("Phishing URL Access", swBrowser, 10_000, new Resources(0.1, 0.1d)));
		attackerProfile.desires(v1);
		attackerProfile.desires(v2, new Resources(1_000, 1_000d));
		attackerProfile.desires(v3, new Resources(10_000, 10_000d));
		Skill skillBrowser = new Skill("Browser");
		Skill skillPayment = new Skill("Payment");
		attackerProfile.addSkill(skillBrowser, 0.8f);
		attackerProfile.addSkill(skillPayment, 0.2f);
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", swBrowser, new Resources(5_000, 4_000d), new Resources());
		final PotentialExploit exploit2 = new PotentialExploit("Browser Extraction", v1, new Resources(1_000, 500d), new Resources());
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", swPayment, new Resources(100_000, 50_000d), new Resources());
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2, new Resources(5_000, 1_000d), new Resources());
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3, new Resources(5_000, 1_000d), new Resources());
		exploit1.setRecommendedSkillProficiency(skillBrowser, 1f);
		exploit2.setRecommendedSkillProficiency(skillBrowser, 1f);
		exploit3.setRecommendedSkillProficiency(skillPayment, 1f);
		exploit4.setRecommendedSkillProficiency(skillPayment, 1f);
		exploit5.setRecommendedSkillProficiency(skillPayment, 1f);
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		for (Software s : uselessSoftware) {
			exploits.add(new PotentialExploit("Some Random Exploit", s, new Resources(), new Resources(1_000, 0d)));
		}

		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		attackGraph = c.crawlAndGetAttackForest();
	}

	@After
	public void tearDown() throws Exception {
		attackGraph = null;
		attractors.clear();
		attractorsPayment.clear();
	}

	
	
	
	
	
	
	
	
	
	@Test
	public final void testExpectedAttackerCost() {
		System.out.println(attackGraph);
		Assert.assertEquals(558_500d, Analyzer.costForAllFloat(attackGraph), 1d);
	}

	@Test
	public final void testExpectedAttackerCostForWebAtractor() {
		Assert.assertEquals(8_500f, Analyzer.costMaxForAttractorFloat(attackGraph, attractorWeb), 1);
	}

	@Test
	public final void testExpectedAttackerCostForFinancialAttractor() {

		float cost = Analyzer.costMaxForAttractorsFloat(attackGraph, attractorsPayment);
		Assert.assertEquals(557_250f, cost, 1);
	}

	@Test
	public final void testExpectedAttackerCostDeviation() {
		Assert.assertEquals(111_938.8789, Analyzer.expectedAttackerCostDeviation(attackGraph), 2); // Expected result stems from Office calculation and lacks precision.
	}

	@Test
	public final void testExpectedAttackerCostDeviationForWebAttractor() {
		assertEquals(4506.9390943299866163990265843381, Analyzer.expectedAttackerCostDeviationForAttractor(attackGraph, attractorWeb), 0.1f);
	}

	@Test
	public final void testExpectedAttackerCostDeviationForFinancialAttractor() {
		Assert.assertEquals(111_937.483, Analyzer.expectedAttackerCostDeviation(attackGraph), 2f); // Expected result stems from Office calculation and lacks precision.
	}

	@Test
	public final void testHowManyAttackersCanAffordTheAttacks() {
		// TODO recalculate theses expected values for 350 k budget instead of 300 k budget dissimilarly again.
		Assert.assertEquals(0.99919, Analyzer.howManyAttackersCanAffordTheAttacksOnto(attackGraph, attractorWeb), 1e-3); // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		Assert.assertEquals(0.08367846586558858, Analyzer.howProbableAttackersCanAffordTheAttacksOnto(attackGraph, attractorsPayment), 1e-3); // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		Assert.assertEquals(0.08240656087851383, Analyzer.howProbableAttackersCanAffordTheAttacksOnto(attackGraph, attractors), 1e-3); // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		Assert.assertEquals(0.08240656087851383, Analyzer.howManyAttackersCanAffordTheAttacks(attackGraph), 1e-3); // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		Assert.assertEquals(Analyzer.howProbableAttackersCanAffordTheAttacksOnto(attackGraph, attractors), Analyzer.howManyAttackersCanAffordTheAttacks(attackGraph), 1e-100);
	}





	@Test
	public final void testGainOnlyWeb() {
		final Resources gain1 = Analyzer.expectedAttackerGain1ForSingleAttractor(attackGraph, attractorWeb);
		final Resources gain2 = Analyzer.gainMaxForAttractor(attackGraph, attractorWeb);
		Assert.assertEquals("Web Attractor Gain Single Exp.", 0.045d, gain1.getMoneyExpectedValue(), PRECISION_OFFICE); // Expected result stems from Office calculation and lacks precision.
		Assert.assertEquals("Web Attractor Gain Maxim. Exp.", 450, gain2.getMoneyExpectedValue(), 1e-100d); // Expected result stems from Office calculation and lacks precision.
		Assert.assertEquals("Web Attractor Gain Single Dev", 0.02d, gain1.getMoneyStandardDeviation(), PRECISION_OFFICE); // Expected result stems from Office calculation and lacks precision.
		Assert.assertEquals("Web Attractor Gain Maxim. Dev", 2, gain2.getMoneyStandardDeviation(), 1e-100d); // Expected result stems from Office calculation and lacks precision.
	}

	@Test
	public final void testGainAll() {
		final double gainExpManually = 110_000_450d; // Expected result stems from Office calculation and lacks precision.
		final double gainStdDevMan = 1_004_987.562d; // Expected result stems from Office calculation and lacks precision.
		final Resources gain1 = Analyzer.gainMaxForAll(attackGraph);
		final Resources gain2 = Analyzer.gainMaxForAttractors(attackGraph, attractors);
		
		/* Check Expected Value */
		Assert.assertEquals("Gain method 1 ExpValue", gainExpManually, gain1.getMoneyExpectedValue(), gainExpManually * PRECISION_OFFICE);
		Assert.assertEquals("Gain method 2 ExpValue", gainExpManually, gain2.getMoneyExpectedValue(), gainExpManually * PRECISION_OFFICE);
		Assert.assertEquals("Gain method ExpValue comparison", gain1.getMoneyExpectedValue(), gain2.getMoneyExpectedValue(), gain1.getMoneyExpectedValue() * PRECISION_JAVA);

		/* Check Standard Deviation */
		Assert.assertEquals("Gain method 1 StdDev", gainStdDevMan, gain1.getMoneyStandardDeviation(), gainStdDevMan * PRECISION_OFFICE);
		Assert.assertEquals("Gain method 2 StdDev", gainStdDevMan, gain2.getMoneyStandardDeviation(), gainStdDevMan * PRECISION_OFFICE);
		Assert.assertEquals("Gain method StdDev comparison", gain1.getMoneyStandardDeviation(), gain2.getMoneyStandardDeviation(), gain1.getMoneyStandardDeviation() * PRECISION_JAVA);
	}

	@Test
	public final void testGainWeb() {
		final double gainExpManually = 450; // Expected result stems from Office calculation and lacks precision.
		final double gainStdDevMan = 2; // Expected result stems from Office calculation and lacks precision.
		final Resources gain = Analyzer.gainMaxForAttractor(attackGraph, attractorWeb);
		Assert.assertEquals("Gain Web ExpValue", gainExpManually, gain.getMoneyExpectedValue(), gainExpManually * PRECISION_OFFICE);
		Assert.assertEquals("Gain Web StdDev", gainStdDevMan, gain.getMoneyStandardDeviation(), gainStdDevMan * PRECISION_OFFICE);
	}

	@Test
	public final void testGainPayment() {
		final double gainExpManually = 110_000_000; // Expected result stems from Office calculation and lacks precision.
		final double gainStdDevMan = 1_004_987.562; // Expected result stems from Office calculation and lacks precision.
		final Resources gain = Analyzer.gainMaxForAttractors(attackGraph, attractorsPayment);
		Assert.assertEquals("Gain Payment ExpValue", gainExpManually, gain.getMoneyExpectedValue(), gainExpManually * PRECISION_OFFICE);
		Assert.assertEquals("Gain Payment StdDev", gainStdDevMan, gain.getMoneyStandardDeviation(), gainStdDevMan * PRECISION_OFFICE);
	}
	

	@Test
	public final void testExpectedAttackerProfitWeb() {
		final double profitManExp = -8050; // Expected result stems from Office calculation and lacks precision.
		final double profitManStD = 4506.950632; // Expected result stems from Office calculation and lacks precision.
		final Resources profit = Analyzer.profitForAttractor(attackGraph, attractorWeb);
		Assert.assertEquals("Profit Web ExpValue", profitManExp, profit.getMoneyExpectedValue(), profitManExp * PRECISION_OFFICE);
		Assert.assertEquals("Profit Web ExpValue", profitManStD, profit.getMoneyStandardDeviation(), profitManStD * PRECISION_OFFICE);
	}

	@Test
	public final void testExpectedAttackerProfitPayment() {
		final double profitManExp = 109_442_750; // Expected result stems from Office calculation and lacks precision.
		final double profitManStD = 1_011_202.255; // Expected result stems from Office calculation and lacks precision.
		final Resources profit = Analyzer.profitForAttractors(attackGraph, attractorsPayment);
		Assert.assertEquals("Profit Payment ExpValue", profitManExp, profit.getMoneyExpectedValue(), profitManExp * PRECISION_OFFICE);
		Assert.assertEquals("Profit Payment ExpValue", profitManStD, profit.getMoneyStandardDeviation(), profitManStD * PRECISION_OFFICE);
	}

	@Test
	public final void testExpectedAttackerProfitAll() {
		final double profitManExp = 109_441_950; // Expected result stems from Office calculation and lacks precision.
		final double profitManStD = 1_011_202.409; // Expected result stems from Office calculation and lacks precision.
		final Resources profit = Analyzer.profitForAttractors(attackGraph, attractors);
		Assert.assertEquals("Profit All ExpValue", profitManExp, profit.getMoneyExpectedValue(), profitManExp * PRECISION_OFFICE);
		Assert.assertEquals("Profit All ExpValue", profitManStD, profit.getMoneyStandardDeviation(), profitManStD * PRECISION_OFFICE);
	}

	@Test
	public final void testExpectedAttackerRoiWeb() {
		final double roiOffice = -0.947058824; // Expected result stems from Office calculation and lacks precision.
		final Resources roi = Analyzer.roiForAttractor(attackGraph, attractorWeb);
		Assert.assertEquals("RoI Web", roiOffice, roi.getMoneyExpectedValue(), -roiOffice * PRECISION_OFFICE);
	}

	@Test
	public final void testExpectedAttackerRoiPayment() {
		final double roiOffice = 196.397_936_3; // Expected result stems from Office calculation and lacks precision.
		final Resources roi = Analyzer.roiForAttractors(attackGraph, attractorsPayment);
		Assert.assertEquals("RoI Payment", roiOffice, roi.getMoneyExpectedValue(), roiOffice * PRECISION_OFFICE);
	}

	@Test
	public final void testExpectedAttackerRoiAll() {
		final double roiOffice = 195.956_938_2; // Expected result stems from Office calculation and lacks precision.
		final Resources roi = Analyzer.roiForAttractors(attackGraph, attractors);
		Assert.assertEquals("RoI All", roiOffice, roi.getMoneyExpectedValue(), roiOffice * PRECISION_OFFICE);
	}

	@Test
	public final void testHowManyAttackerCouldProfitWeb() {
		final double posProfProbOffice = 0.03673; // Expected result stems from Office calculation and lacks precision.
		final double posProfProb = Analyzer.profitPositiveProbability(attackGraph, attractorWeb);
		Assert.assertEquals("posProfProb Web", posProfProbOffice, posProfProb, posProfProbOffice * PRECISION_TABLES);
	}

	@Test
	public final void testHowManyAttackerCouldProfitPay() {
		final double posProfProbOffice = 1; // Expected result stems from Office calculation and lacks precision.
		final double posProfProb = Analyzer.profitPositiveProbability(attackGraph, attractorsPayment);
		Assert.assertEquals("posProfProb Payment", posProfProbOffice, posProfProb, posProfProbOffice * PRECISION_TABLES);
	}

	@Test
	public final void testHowManyAttackerCouldProfitAll() {
		final double posProfProbOffice = 1; // Expected result stems from Office calculation and lacks precision.
		final double posProfProb = Analyzer.profitPositiveProbability(attackGraph, attractors);
		Assert.assertEquals("posProfProb All", posProfProbOffice, posProfProb, posProfProbOffice * PRECISION_TABLES);
	}

	@Test
	public final void testProbabilityForSuccessfulAttacksWeb() {
		final double attackProbabilityOffice = 0.036_664; // Expected result stems from Office calculation and lacks precision.
		final double attackProbability = Analyzer.attackProbability(attackGraph, attractorWeb);
		Assert.assertEquals("attack probability Web", attackProbabilityOffice, attackProbability, attackProbabilityOffice * PRECISION_TABLES);
	}

	@Test
	public final void testProbabilityForSuccessfulAttacksPayment() {
		final double attackProbabilityOffice = 0.08367846586558858; // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		final double attackProbability = Analyzer.attackProbability(attackGraph, attractorsPayment);
		Assert.assertEquals("attack probability Payment", attackProbabilityOffice, attackProbability, attackProbabilityOffice * PRECISION_TABLES);
	}

	@Test
	public final void testProbabilityForSuccessfulAttacksAll() {
		final double attackProbabilityOffice = 0.08240656087851383; // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		final double attackProbability = Analyzer.attackProbability(attackGraph, attractors);
		Assert.assertEquals("attack probability All", attackProbabilityOffice, attackProbability, attackProbabilityOffice * PRECISION_TABLES);
	}

	@Test
	public final void testHowBigIsTheExpectedDamageWeb() {
		final double damageOffice = 82.49; // Expected result stems from Office calculation and lacks precision.
		final double damage = Analyzer.damageExpected(attackGraph, attractorWeb);
		Assert.assertEquals("attack probability All", damageOffice, damage, damageOffice * PRECISION_TABLES);
	}

	@Test
	public final void testHowBigIsTheExpectedDamagePayment() {
		final double damageOffice = 4.602315622607372E7; // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		final double damage = Analyzer.damageExpected(attackGraph, attractorsPayment);
		Assert.assertEquals("attack probability All", damageOffice, damage, damageOffice * PRECISION_TABLES);
	}

	@Test
	public final void testHowBigIsTheExpectedDamageAll() {
		final double damageOffice = 4.5323793897944584E7; // Expected result are close to Office calculation but stem from java calculations due to changed budget.
		final double damage = Analyzer.damageExpected(attackGraph, attractors);
		Assert.assertEquals("attack probability All", damageOffice, damage, damageOffice * PRECISION_TABLES);
	}

	
	
	
	
	
	
	
	
	
}
