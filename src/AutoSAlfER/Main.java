package AutoSAlfER;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.xml.bind.JAXBException;

import analysis.Analyzer;
import analysis.Crawler;
import analysis.multipath.salfer.PoincareSylvester;
import analysis.report.Report;
import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import exploit.Exploit;
import exploit.PotentialExploit;
import systemModel.Asset;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.fibexImporter.Fibex31toModel;
import types.Resources;

/**
 * AutoSAlfER - Automotive Security Analyzer for Exploitability Risks.
 * 
 * This software is a proof of concept implementation for an automated and
 * attack graph-based evaluation of on-board networks. First, please run all
 * JUnit tests for ensuring a working installation, e.g.
 * analysis.AnalyzerTest.java. Then use this main file or a new JUnit test
 * method for executing analyses.
 * 
 * @author Martin Salfer
 * @created 2014-10-28
 */
public class Main {

	/**
	 * Main method.
	 * 
	 * @param args
	 *            are not necessary. Will be ignored.
	 */
	public static final void main(String[] args) {
		/* Start a real analysis. */
		doAnalysis();

		/* Start tests. */
		// testQueue();
		// testHashSetForNullAdd();
		// testDoubleNullArithmetics();
		// expectedValueTest();
		// listIteratorTest();
		// massiveAccumulationProbabilities();
		// booleanAdditionTest();
		// testAffordabilityRoundingProblem();
		// maxMemory();
	}

	private static final void doAnalysis() {
		/* System Model */
		/* Software Definition */
		final String[] fibexFiles = { "input/fibex/Vehicle_A_FlexRay_V42_PWF_lokal.xml", "input/fibex/Vehicle_Body2-CAN_V16_PWF_fern.xml",
				"input/fibex/Vehicle_Body-CAN_V35_PWF_fern.xml", "input/fibex/Vehicle_CAS-CAN_V6_PWF_fern.xml", "input/fibex/Vehicle_D_CAN_V28_PWF_fern.xml",
				"input/fibex/Vehicle_FA-CAN_V33_PWF_fern.xml", "input/fibex/Vehicle_FASL-CAN_V6_PWF_fern.xml", "input/fibex/Vehicle_FASR-CAN_V6_PWF_fern.xml",
				"input/fibex/Vehicle_IuK-CAN_V12_PWF_fern.xml", "input/fibex/Vehicle_LE-CAN_V9_PWF_fern.xml", "input/fibex/Vehicle_LP-CAN_V9_PWF_fern.xml",
				"input/fibex/Vehicle_PS-CAN_V8_PWF_fern.xml", "input/fibex/Vehicle_USS-CAN_V8_PWF_fern.xml", "input/fibex/Vehicle_ZSG-CAN_V22_PWF_fern.xml" };
		final String reportFileName = "reports/AttackRiskReport-Main.xml";
		final SystemModel sysModel = loadSystemModelFromFibex(fibexFiles);
		final String nameAtm = "ATM";
		final String nameDme1 = "DME1";
		final String nameBdc = "BDC2015";
		final String nameNbt = "NBTevo";
		final String nameDsc = "DSC_Modul";
		final String nameObd = "ZGW_OBD";

		/* Asset Definition */
		final Asset assetVMax = new Asset("vMax", sysModel.getSoftwareWithName(nameDme1), new Resources(10_000D, 5_000d));
		final Asset assetCarTheft = new Asset("CarTheft", sysModel.getSoftwareWithName(nameBdc), new Resources(50_000, 10_000d));
		final Asset assetPaymentCredentials = new Asset("PaymentCredentials", sysModel.getSoftwareWithName(nameNbt), new Resources(500, 500d));
		final Asset assetPaymentReputation = new Asset("PaymentReputation", sysModel.getSoftwareWithName(nameNbt), new Resources(100, 100d));
		final Asset assetSafetyDSC = new Asset("PassangerSafety_DSC", sysModel.getSoftwareWithName(nameDsc), new Resources(1_000, 500d));
		final Asset assetSafetyBDC = new Asset("PassangerSafety_BDC", sysModel.getSoftwareWithName(nameBdc), new Resources(1_000, 500d));
		final Asset[] assets = { assetVMax, assetCarTheft, assetPaymentCredentials, assetPaymentReputation, assetSafetyDSC, assetSafetyBDC };

		/* Attacker Profile Definition */
		final AttackerProfile attacker = new AttackerProfile("Generic", 10, new Resources(20_000d, 15_000d));
		attacker.addAccess(new Access("OBD access", sysModel.getSoftwareWithName(nameObd), 100, new Resources(300, 100d)));
		attacker.addAccess(new Access("Internet", sysModel.getSoftwareWithName(nameAtm), 1_000, new Resources(50, 100d)));
		attacker.addAccess(new Access("HU Malware", sysModel.getSoftwareWithName(nameNbt), 100, new Resources(50, 100d)));
		attacker.desires(assets);

		/* Exploit database. */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		for (Software sw : sysModel.getSoftwareSet()) {
			if (sw.getName().equals(nameBdc)) {
				exploits.add(new PotentialExploit(sw.getName(), sw, new Resources(20_000d, 5_000d)));
				continue;
			}
			exploits.add(new PotentialExploit(sw.getName(), sw, new Resources(5_000d, 5_000d)));
		}
		for (Asset a : assets) {
			exploits.add(new PotentialExploit(a.getName(), a, new Resources(1_000, 500d)));
		}

		/* Attack Scenario */
		final AttackScenario scenario = new AttackScenario("generic-attack", sysModel, attacker, exploits);

		/* Run */
		final Crawler c = new Crawler(scenario);
		final AttackGraph attackGraph = c.crawlAndGetAttackForest();

		/* Report */
		final String report = Report.analyzeAndGenerateReport(attackGraph, reportFileName);
		System.out.println("Main.doAnalysis():");
		System.out.println(attackGraph);
		System.out.println(report);
	}

	/**
	 * Load a system model by importing FIBEX files.
	 * 
	 * @param sysModel
	 */
	private static final SystemModel loadSystemModelFromFibex(String[] fibexFiles) {
		SystemModel sysModel = new SystemModel();
		Fibex31toModel systemImporter = new Fibex31toModel();
		try {
			systemImporter.importSystemModelFromFibex(fibexFiles, sysModel, true, true);
		} catch (FileNotFoundException fe) {
			System.out.println(fe);
		} catch (JAXBException je) {
			System.out.println(je);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

		assert (systemImporter != null);
		assert (sysModel != null);
		assert (sysModel.nodes != null);
		assert (systemImporter.getComMediaMap() != null);
		assert (systemImporter.getExclusiveComMediaMap() != null);
		return sysModel;
	}

	private static void maxMemory() {
		System.out.println(Runtime.getRuntime().maxMemory() / 1_000_000);
	}

	private static void booleanAdditionTest() {
		boolean hit = false;
		System.out.println(hit);
		hit |= false;
		System.out.println(hit);
		hit |= true;
		System.out.println(hit);
		hit |= false;
		System.out.println(hit);
		hit |= true;
		System.out.println(hit);
	}

	/**
	 * Show that any massive path occurrence within an network will lead to far
	 * too high probabilities.
	 */
	private static void massiveAccumulationProbabilities() {

		final double dNew = 0.1d;
		double d = 0.1d;
		for (int i = 1; i < 1_000; i++) {
			d = PoincareSylvester.combine2Inputs(d, dNew);
		}
		System.out.println(d);
	}

	private static final void expectedValueTest() {
		System.out.println("Test");

		final double pathHitLikelihood = 0.01;
		final float pathHitLikelihood32bit = (float) pathHitLikelihood;
		final int pathCost = 20;
		final int pathNumber = 100;

		System.out.println("n=" + pathNumber + " => Exp:" + expectedValue(pathHitLikelihood, pathCost, pathNumber) + " (64-bit)");
		System.out.println("n=" + pathNumber + " => Exp:" + expectedValue32bit(pathHitLikelihood32bit, pathCost, pathNumber) + " (32-bit)");

		/* 64-bit run */
		for (int n = 1; n <= 500; n++) {
			// System.out.println("n=" + n + " => Exp:" +
			// expectedValue(pathHitLikelihood, pathCost, n));
			System.out.println(expectedValue(pathHitLikelihood, pathCost, n));
		}

		/* 32-bit run */
		for (int n = 1; n <= 500; n++) {
			System.out.println(expectedValue32bit(pathHitLikelihood32bit, pathCost, n));
		}
	}

	/**
	 * @param pathHitLikelihood
	 * @param pathCost
	 * @param pathNumber
	 */
	private static double expectedValue(final double pathHitLikelihood, final int pathCost, final int pathNumber) {
		double expectedValue = 0;
		// expectedValue += pathCost * pathHitLikelihood; // first try a hit
		// case.
		for (int n = 1; n <= pathNumber; n++) { // every path that could trigger
												// a success.
			double previousPathsFailedLikelihood = Math.pow(1 - pathHitLikelihood, n - 1);
			expectedValue += pathCost * previousPathsFailedLikelihood * pathHitLikelihood;
		}
		// adding the failure expectaction
		expectedValue += pathCost * pathNumber * Math.pow((1 - pathHitLikelihood), pathNumber);

		return expectedValue;
	}

	/**
	 * @param pathHitLikelihood
	 * @param pathCost
	 * @param pathNumber
	 */
	private static float expectedValue32bit(final float pathHitLikelihood, final int pathCost, final int pathNumber) {
		float expectedValue = 0;
		for (int n = 1; n <= pathNumber; n++) { // every path that could trigger
												// a success.
			float previousPathsFailedLikelihood = (float) Math.pow(1 - pathHitLikelihood, n - 1);
			expectedValue += pathCost * previousPathsFailedLikelihood * pathHitLikelihood;
		}
		// adding the failure expectaction
		expectedValue += pathCost * pathNumber * Math.pow((1 - pathHitLikelihood), pathNumber);

		return expectedValue;
	}

	// private static void testDoubleNullArithmetics() {
	// Double a = 1d, b = 1d;
	// b = null;
	// System.out.println(a/b); // Erzeugt eine NullPointerException!
	// }
	//
	// /**
	// *
	// */
	// private static void testHashSetForNullAdd() {
	// HashSet<Object> m = new HashSet<Object>();
	// System.out.println(m.contains(null));
	// m.add(null);
	// System.out.println(m.contains(null));
	// }
	//
	// /**
	// *
	// */
	// private static void testQueue() {
	// Software t1 = new Software("Browser");
	// Software t2 = new Software("Payment Service");
	// Software t3 = new Software("Unrelated Service");
	//
	// t1.setHopDistanceToAssets(1);
	// t2.setHopDistanceToAssets(2);
	// t3.setHopDistanceToAssets(3);
	//
	// PriorityQueue<Software> q = new PriorityQueue<Software>();
	//
	// q.add(t1);
	// q.add(t1);
	// q.add(t1);
	// q.add(t1);
	// q.add(t1);
	// q.add(t1);
	// }

	private static void listIteratorTest() {
		LinkedList<Double> ds = new LinkedList<Double>();
		ds.add(10d);
		ds.add(20d);
		ds.add(30d);
		ds.add(40d);

		ListIterator<Double> i = ds.listIterator();

		while (i.hasNext()) {
			Double d = i.next();
			i.remove();
			i.add(d / 5d);
		}
	}

	public static final void testAffordabilityRoundingProblem() {
		final Resources budget = new Resources(3_000d, 1_000d);
		final Resources pathCost = new Resources(1_000d, 200d);
		double affordabilitySum = 0d;
		int i = 1;
		for (; i <= 3; i++) {
			final double calculateAttackerAffordability = Analyzer.calculateAttackerAffordability(budget, pathCost);
			affordabilitySum += calculateAttackerAffordability;
			System.out.println("singleStepGauß: " + calculateAttackerAffordability);
		}
		i--;
		System.out.println("affSum/3: " + affordabilitySum / i);
		System.out.println("PathGauß: " + Analyzer.calculateAttackerAffordability(budget, pathCost.multiplyWith(i)));
	}
}
