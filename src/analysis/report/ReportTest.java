package analysis.report;

import java.util.ArrayList;
import java.util.Arrays;
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
import analysis.Crawler;
import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import attackerProfile.Skill;
import exploit.Exploit;
import exploit.PotentialExploit;

public class ReportTest {

	
	private static final String REPORT_FILE_NAME_EXAMPLE_SIMPLE = "reports/AttackRiskReport-JUnit-ReportTest.xml";
	private static final String REPORT_FILE_NAME_EXAMPLE_COMPLEX = "reports/AttackRiskReport-JUnit-ReportTest-moreComplex.xml";
	
	private AttackGraph attackGraph;
	private final Set<Asset> attractors = new HashSet<Asset>(3);
	private Asset attractorWeb;
	private Asset attractorPay1;
	private Asset attractorPay2;
	private final Set<Asset> attractorsPayment = new HashSet<Asset>(2);
	private AttackScenario scenario;

	@Before
	public void setUp() throws Exception {
		// TODO setup must not interfere with more complex model.
//		/* Configuration */
//		final int amountOfUselessSoftware = 100;
//		
//		
//		/* System model */
//		final SystemModel sysModel = new SystemModel();
//		Software swBrowser = new Software("Browser");
//		Software swPayment = new Software("Payment Service");
//		List<Software> uselessSoftware = createUselessSoftware(amountOfUselessSoftware);
//		
//		sysModel.addNode(swBrowser);
//		sysModel.addNode(swPayment);
//		for (Software s : uselessSoftware) {
//			sysModel.addNode(s);
//		}
//		
//		CommunicationNode dBus = new CommunicationNode("dBus");
//		Capability dBusAccess = new Capability("dBusAccess");
//		dBusAccess.grantAccessTo(dBus);
//		swBrowser.grant(dBusAccess);
//		swPayment.grant(dBusAccess);
//		for (Software s : uselessSoftware) {
//			s.grant(dBusAccess);
//		}
//		
//		
//		Attractor v1 = new Attractor("Web2.0", swBrowser, new Resources(0.045, 0.02d));
//		Attractor v2 = new Attractor("In-App Payment Financial", swPayment);
//		Attractor v3 = new Attractor("In-App Payment Reputation", swPayment);
//		sysModel.addNode(v1);
//		sysModel.addNode(v2);
//		sysModel.addNode(v3);
//		attractors.add(v1);
//		attractors.add(v2);
//		attractors.add(v3);
//		attractorWeb = v1;
//		attractorPay1 = v2;
//		attractorPay2 = v3;
//		attractorsPayment.add(attractorPay1);
//		attractorsPayment.add(attractorPay2);
//
//		
//		/* Attacker Profile. */
//		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 5, new Resources(300_000, 100_000d));
//		attackerProfile.addAccess(new Access("Phishing URL Access", swBrowser, 10_000, new Resources(0.1, 0.1d)));
//		attackerProfile.desires(v1);
//		attackerProfile.desires(v2, new Resources(1_000, 1_000d));
//		attackerProfile.desires(v3, new Resources(10_000, 10_000d));
//		Skill skillBrowser = new Skill("Browser");
//		Skill skillDbus = new Skill("dBus");
//		attackerProfile.addSkill(skillBrowser, 0.8f);
//		attackerProfile.addSkill(skillDbus, 0.2f);
//		
//		/* Exploit database. */
//		final HashSet<Exploit> exploits = new HashSet<Exploit>();
//		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", swBrowser, new Resources(), new Resources(5_000, 4_000d));
//		final PotentialExploit exploit2 = new PotentialExploit("Browser Extraction", v1, new Resources(), new Resources(1_000, 500d));
//		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", swPayment, new Resources(), new Resources(100_000, 50_000d));
//		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", v2, new Resources(), new Resources(5_000, 1_000d));
//		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", v3, new Resources(), new Resources(5_000, 1_000d));
//		exploit1.setRecommendedSkillProficiency(skillBrowser, 1f);
//		exploit2.setRecommendedSkillProficiency(skillBrowser, 1f);
//		exploit3.setRecommendedSkillProficiency(skillDbus, 1f);
//		exploit4.setRecommendedSkillProficiency(skillDbus, 1f);
//		exploit5.setRecommendedSkillProficiency(skillDbus, 1f);
//		exploits.add(exploit1);
//		exploits.add(exploit2);
//		exploits.add(exploit3);
//		exploits.add(exploit4);
//		exploits.add(exploit5);
//		for (Software s : uselessSoftware) {
//			exploits.add(new PotentialExploit("Some Random Exploit", s, new Resources(), new Resources(1_000, 0d)));
//		}
//
//		scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
	}

	@After
	public void tearDown() throws Exception {
		attackGraph = null;
		attractors.clear();
		attractorsPayment.clear();
	}

	
	
	
	
	
	
	
	

//	@Test
	public final void testAnalyzeAndGenerateReport() {
		Crawler c = new Crawler(scenario);
		attackGraph = c.crawlAndGetAttackForest();
		final String report = Report.analyzeAndGenerateReport(attackGraph, REPORT_FILE_NAME_EXAMPLE_SIMPLE);
		System.out.println(report);
		Assert.assertTrue("Report Start", report.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<AttackRiskReport date="));
		Assert.assertTrue("Report length around 9_000", Math.abs(report.length() - 9000) < 100);
		for (String content : new String[] {"<attackGraphReport", "<budget>", "<attackerProfile", "<accessSet>",
				"<access", "<attackSteps>", "<exploit name=", "<cost>", "<gain>", "<profit>", "<roi>", 
				"<probAffordability>", "<probProfit>", "<probAttack>", "<probAttack>"}) {
			Assert.assertTrue("Report content", report.contains(content));
		}
		Assert.assertTrue("Report End", report.endsWith("</expectedDamage>\n    </attackGraphReport>\n</AttackRiskReport>\n"));
	}

	
	
//	TODO implement interactive visualisation!
//	@Test
//	public final void testAnalyzeAndVisitAttackGraph() {
//		Assert.assertTrue(Report.analyzeAndGenerateReport(attackGraph).length() > 100);
//	}

	@Test
	public final void testMoreComplexExample() {
		/* Configuration */
		final int amountOfUselessSoftwarePerBus = 0;
		final int amountOfAdditionalInternalHeadUnitSw = 0;
		
		
		/* System model */
		final SystemModel sysModel = new SystemModel();
		Set<Set<Software>> swSet = new HashSet<Set<Software>>();

		Software swBrowser = new Software("Browser");
		Software swPayment = new Software("Payment Service");
		List<Software> uselessSoftware = createUselessSoftware(amountOfUselessSoftwarePerBus);
		
		/* System Model: single connected software */
		Set<Software> swSetBodyCAN = new HashSet<Software>();
		swSet.add(swSetBodyCAN);
		for (String ecuSw : Arrays.asList("aLBV_FA","FRMFA","PDC", "JBBF","SEC1", "aLBV_BF", "FZD","CAS", "CDM")) {
			swSetBodyCAN.add(new Software(ecuSw));
		}

		Set<Software> swSetKCan = new HashSet<Software>();
		swSet.add(swSetKCan);
		for (String ecuSw : Arrays.asList("EHC2", "FLA", "AHM", "HKL", "SM_FA", "SM_BF", "SM_FAH", "SM_BFH", "SEC2", "CVM", "HKA", "IHKA", "ZBE", "CID", "CID_R1", "CID_R2", "ZBE-Fond", "Video Switch", "HiFi", "FKA", "RDC", "HUD", "TRSVC")) {
			swSetKCan.add(new Software(ecuSw));
		}

		Set<Software> swSetMost = new HashSet<Software>();
		swSet.add(swSetMost);
		for (String ecuSw : Arrays.asList("AmpTop", "SDARS", "MMC", "TVM", "TCU", "Tel_MULF_High")) {
			swSetMost.add(new Software(ecuSw));
		}

		Set<Software> swSetSafetyCan = new HashSet<Software>();
		swSet.add(swSetSafetyCan);
		for (String ecuSw : Arrays.asList("VOCS", "EMA_LI", "EMA_RE")) {
			swSetSafetyCan.add(new Software(ecuSw));
		}
		
		Set<Software> swSetFlexray = new HashSet<Software>();
		swSet.add(swSetFlexray);
		for (String ecuSw : Arrays.asList("ICM-V", "RK-VR", "RK-VL", "RK-HR", "RK-HL", "ASA", "HSR", "EPS")) {
			swSetFlexray.add(new Software(ecuSw));
		}
		
		Set<Software> swSetFaCan = new HashSet<Software>();
		swSet.add(swSetFaCan);
		for (String ecuSw : Arrays.asList("KAFAS", "LMV")) {
			swSetFaCan.add(new Software(ecuSw));
		}
		
		Set<Software> swSetACan = new HashSet<Software>();
		swSet.add(swSetACan);
		for (String ecuSw : Arrays.asList("EKP", "PCU")) {
			swSetACan.add(new Software(ecuSw));
		}
		
		Set<Software> swSetSCan = new HashSet<Software>();
		swSet.add(swSetSCan);
		for (String ecuSw : Arrays.asList("LRR", "SRR-L", "SRR-R")) {
			swSetSCan.add(new Software(ecuSw));
		}
		
		

		
		/* System Model : multi connected or attractor carrying software */
		
		Set<Software> swSetCasBus = new HashSet<Software>();
		Set<Software> swSetEthernetRse = new HashSet<Software>();
		Set<Software> swSetEthernetHdd = new HashSet<Software>();
		Set<Software> swSetDiagCan = new HashSet<Software>();
		Set<Software> swSetHsfz = new HashSet<Software>();
		Set<Software> swSetNvcCan = new HashSet<Software>();
		Set<Software> swSetHc2Can = new HashSet<Software>();

		swSet.add(swSetCasBus);
		swSet.add(swSetEthernetRse);
		swSet.add(swSetEthernetHdd);
		swSet.add(swSetDiagCan);
		swSet.add(swSetHsfz);
		swSet.add(swSetNvcCan);
		swSet.add(swSetHc2Can);

		
		Set<Software> swSetHeadUnitInternal = new HashSet<Software>();
		swSet.add(swSetHeadUnitInternal);
		for (int i = 1 ; i <= amountOfAdditionalInternalHeadUnitSw ; i++) {
			swSetHeadUnitInternal.add(new Software("Head Unit internal additional #" + i));
		}
		swSetHeadUnitInternal.add(swBrowser);
		swSetHeadUnitInternal.add(swPayment);
		
		Software swAcsm = new Software("ACSM");
		swSetFaCan.add(swAcsm);
		swSetSafetyCan.add(swAcsm);
		
		Software swDme1 = new Software("DME1");
		swSetFlexray.add(swDme1);
		swSetFaCan.add(swDme1);
		swSetACan.add(swDme1);
		swSetCasBus.add(swDme1);
		
		Software swDme2 = new Software("DME2");
		swSetFlexray.add(swDme2);
		swSetFaCan.add(swDme2);
		
		Software swDsc = new Software("DSC");
		swSetFlexray.add(swDsc);
		
		Software swEgs = new Software("EGS");
		swSetFaCan.add(swEgs);
		swSetACan.add(swEgs);
		
		Software swEmf = new Software("EMF");
		swSetFaCan.add(swEmf);
		
		Software swGws = new Software("GWS");
		swSetFaCan.add(swGws);
		swSetACan.add(swGws);
		
		Software swHc2 = new Software("HC2");
		swSetFlexray.add(swHc2);
		swSetHc2Can.add(swHc2);
		
		Software swHc2slave = new Software("HC2-Slave");
		swSetHc2Can.add(swHc2slave);
		
		Software swSzlLws = new Software("SZL_LWS");
		swSetFlexray.add(swSzlLws);
		swSetACan.add(swSzlLws);
		
		Software swZgw = new Software("ZGW");
		swSetKCan.add(swZgw);
		swSetBodyCAN.add(swZgw);
		swSetFlexray.add(swZgw);
		swSetFaCan.add(swZgw);
		swSetMost.add(swZgw);
		swSetEthernetHdd.add(swZgw);
		swSetHsfz.add(swZgw);
		swSetDiagCan.add(swZgw);
		
		Software swCic = new Software("CIC");
		swSetEthernetHdd.add(swCic);
		swSetMost.add(swCic);
		swSetKCan.add(swCic);
		swSetEthernetRse.add(swCic);
		swSetHeadUnitInternal.add(swCic);
		
		Software swKombi = new Software("Kombi");
		swSetFaCan.add(swKombi);
		swSetMost.add(swKombi);
		
		Software swNivi = new Software("Nivi");
		swSetFaCan.add(swNivi);
		swSetNvcCan.add(swNivi);
		
		Software swNvc = new Software("NVC");
		swSetNvcCan.add(swNvc);
		
		Software swCas = new Software("CAS");
		swSetCasBus.add(swCas);
		swSetBodyCAN.add(swCas);
		
		Software swRse = new Software("RSE");
		swSetMost.add(swRse);
		swSetKCan.add(swRse);
		swSetEthernetRse.add(swRse);
		
		Software swIcmql = new Software("ICM-QL");
		swSetFlexray.add(swIcmql);
		swSetSCan.add(swIcmql);
		

		/* System Model : Register Nodes */
		sysModel.addNode(swBrowser);
		sysModel.addNode(swPayment);
		for (Set<Software> set : swSet) {
			sysModel.addNodes(set);
		}
		sysModel.addNodes(uselessSoftware);
		


		
		
		/* Communication Nodes */
		CommunicationMedium comGenividBus = new CommunicationMedium("dBus");
		CommunicationMedium comBodyCan = new CommunicationMedium("Body-CAN");
		CommunicationMedium comKCan = new CommunicationMedium("K CAN");
		CommunicationMedium comMost = new CommunicationMedium("MOST Ring");
		CommunicationMedium comFaCan = new CommunicationMedium("FA CAN");
		CommunicationMedium comSafetyCan = new CommunicationMedium("Safety CAN");
		CommunicationMedium comFlexray = new CommunicationMedium("Flexray");
		CommunicationMedium comACan = new CommunicationMedium("A CAN");
		CommunicationMedium comSCan = new CommunicationMedium("S CAN");
		CommunicationMedium comNvcCan = new CommunicationMedium("NVC CAN");
		CommunicationMedium comCasBus = new CommunicationMedium("CAS Bus");
		CommunicationMedium comDiagCan = new CommunicationMedium("Diag CAN");
		CommunicationMedium comHsfz = new CommunicationMedium("High-Speed-Fahrzeug-Zugang");
		CommunicationMedium comEthernetHdd = new CommunicationMedium("Ethernet HDD");
		CommunicationMedium comEthernetRse = new CommunicationMedium("Ethernet RSE");
		CommunicationMedium comHc2Can = new CommunicationMedium("HC2 CAN");
		
		swBrowser.registerReadsWritesOn(comGenividBus);
		swPayment.registerReadsWritesOn(comGenividBus);
		
		for (Software sw : swSetBodyCAN) {
			sw.registerReadsWritesOn(comBodyCan);
		}
		for (Software sw : swSetKCan) {
			sw.registerReadsWritesOn(comKCan);
		}
		for (Software sw : swSetMost) {
			sw.registerReadsWritesOn(comMost);
		}
		for (Software sw : swSetFaCan) {
			sw.registerReadsWritesOn(comFaCan);
		}
		for (Software sw : swSetSafetyCan) {
			sw.registerReadsWritesOn(comSafetyCan);
		}
		for (Software sw : swSetFlexray) {
			sw.registerReadsWritesOn(comFlexray);
		}
		for (Software sw : swSetACan) {
			sw.registerReadsWritesOn(comACan);
		}
		for (Software sw : swSetSCan) {
			sw.registerReadsWritesOn(comSCan);
		}
		for (Software sw : swSetNvcCan) {
			sw.registerReadsWritesOn(comNvcCan);
		}
		for (Software sw : swSetCasBus) {
			sw.registerReadsWritesOn(comCasBus);
		}
		for (Software sw : swSetDiagCan) {
			sw.registerReadsWritesOn(comDiagCan);
		}
		for (Software sw : swSetHsfz) {
			sw.registerReadsWritesOn(comHsfz);
		}
		for (Software sw : swSetEthernetHdd) {
			sw.registerReadsWritesOn(comEthernetHdd);
		}
		for (Software sw : swSetEthernetRse) {
			sw.registerReadsWritesOn(comEthernetRse);
		}
		for (Software sw : swSetHc2Can) {
			sw.registerReadsWritesOn(comHc2Can);
		}
		for (Software sw : swSetHeadUnitInternal) {
			sw.registerReadsWritesOn(comGenividBus);
		}
		for (Software sw : uselessSoftware) {
			sw.registerReadsWritesOn(comGenividBus);
		}

		
		/* System Model : Valuables */
		attractorWeb = new Asset("Web2.0", swBrowser, new Resources(0.045, 0.02d));
		attractorPay1 = new Asset("In-App Payment Financial", swPayment);
		attractorPay2 = new Asset("In-App Payment Reputation", swPayment);
		Asset vCarAccess = new Asset("Car life module access", swCas, new Resources(50_000d, 10_000d));
		Asset vCarSafety1 = new Asset("Car DSC Safety", swDsc, new Resources(1_000_000, 1_000_000d));
		Asset vCarSafety2 = new Asset("Car EMF Safety", swEmf, new Resources(1_000_000, 1_000_000d));
		
		attractors.add(attractorWeb);
		attractors.add(attractorPay1);
		attractors.add(attractorPay2);
		attractors.add(vCarAccess);
		attractors.add(vCarSafety1);
		attractors.add(vCarSafety2);
		for (Asset a : attractors) {
			sysModel.addNode(a);
		}
		attractorsPayment.add(attractorPay1);
		attractorsPayment.add(attractorPay2);

		
		/* Attacker Profile. */
		final AttackerProfile attackerProfile = new AttackerProfile("Internet Harvest Group", 5, new Resources(300_000, 100_000d));
		attackerProfile.addAccess(new Access("Phishing URL Access", swBrowser, 10_000, new Resources(0.1, 0.1d)));
		attackerProfile.desires(attractorWeb);
		attackerProfile.desires(attractorPay1, new Resources(1_000, 1_000d));
		attackerProfile.desires(attractorPay2, new Resources(10_000, 10_000d));
		attackerProfile.desires(vCarAccess);
		attackerProfile.desires(vCarSafety1, new Resources(0, 1_000d));
		attackerProfile.desires(vCarSafety2, new Resources(0, 1_000d));

		Skill skillBrowser = new Skill("Browser");
		Skill skillDbus = new Skill("dBus");
		attackerProfile.addSkill(skillBrowser, 0.8f);
		attackerProfile.addSkill(skillDbus, 0.2f);
		
		
		
		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		final PotentialExploit exploit1 = new PotentialExploit("Browser Exploit", swBrowser, new Resources(), new Resources(5_000, 4_000d));
		final PotentialExploit exploit2 = new PotentialExploit("Browser Extraction", attractorWeb, new Resources(), new Resources(1_000, 500d));
		final PotentialExploit exploit3 = new PotentialExploit("Payment Exploit", swPayment, new Resources(), new Resources(100_000, 50_000d));
		final PotentialExploit exploit4 = new PotentialExploit("Payment Extraction", attractorPay1, new Resources(), new Resources(5_000, 1_000d));
		final PotentialExploit exploit5 = new PotentialExploit("Payment Reputation", attractorPay2, new Resources(), new Resources(5_000, 1_000d));
		final PotentialExploit exploit6 = new PotentialExploit("Life Module Access", vCarAccess, new Resources(), new Resources(500, 50d));
		final PotentialExploit exploit7 = new PotentialExploit("DSC Safety Control", vCarSafety1, new Resources(), new Resources(500, 50d));
		final PotentialExploit exploit8 = new PotentialExploit("EMF Safety Control", vCarSafety2, new Resources(), new Resources(500, 50d));
		final Set<PotentialExploit> exploitsGenerally = new HashSet<PotentialExploit>();
		for (Set<Software> set : swSet) {
			for (Software sw : set) {
				final PotentialExploit potentialExploit = new PotentialExploit("General Potential Exploit for " + sw.toString(), sw, new Resources(), new Resources(50_000, 50_000d));
				exploitsGenerally.add(potentialExploit);
				exploits.add(potentialExploit);
			}
		}
		exploit1.setRecommendedSkillProficiency(skillBrowser, 1f);
		exploit2.setRecommendedSkillProficiency(skillBrowser, 1f);
		exploit3.setRecommendedSkillProficiency(skillDbus, 1f);
		exploit4.setRecommendedSkillProficiency(skillDbus, 1f);
		exploit5.setRecommendedSkillProficiency(skillDbus, 1f);
		exploits.add(exploit1);
		exploits.add(exploit2);
		exploits.add(exploit3);
		exploits.add(exploit4);
		exploits.add(exploit5);
		exploits.add(exploit6);
		exploits.add(exploit7);
		exploits.add(exploit8);
		for (Software s : uselessSoftware) {
			exploits.add(new PotentialExploit("Some Random Exploit", s, new Resources(), new Resources(1_000, 0d)));
		}

		AttackScenario scenario = new AttackScenario("Salfer Proposal Example", sysModel, attackerProfile, exploits);
		
		Crawler c = new Crawler(scenario);
		attackGraph = c.crawlAndGetAttackForest();
		
		
		
		final String report = Report.analyzeAndGenerateReport(attackGraph, REPORT_FILE_NAME_EXAMPLE_COMPLEX);
		System.out.println("More complex system model:");
		System.out.println(attackGraph);
		System.out.println(report);
		Assert.assertTrue("Report Start", report.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<AttackRiskReport date="));
//		Assert.assertTrue("Report length around 9_000", Math.abs(report.length() - 9000) < 100);
		for (String content : new String[] {"<attackGraphReport", "<budget>", "<attackerProfile", "<accessSet>",
				"<access", "<attackSteps>", "<exploit name=", "<cost>", "<gain>", "<profit>", "<roi>", 
				"<probAffordability>", "<probProfit>", "<probAttack>", "<probAttack>"}) {
			Assert.assertTrue("Report content", report.contains(content));
		}
		Assert.assertTrue("Report End", report.endsWith("</expectedDamage>\n    </attackGraphReport>\n</AttackRiskReport>\n"));

		
		
	}

	/**
	 * @param amountOfUselessSoftwarePerBus
	 * @return
	 */
	private List<Software> createUselessSoftware(final int amountOfUselessSoftwarePerBus) {
		List<Software> uselessSoftware = new ArrayList<Software>(amountOfUselessSoftwarePerBus);
		for (int i = 1; i <= amountOfUselessSoftwarePerBus; i++) {
			uselessSoftware.add(new Software("Useless Software"));
		}
		return uselessSoftware;
	}
	
	
	
	
}
