/**
 * 
 */
package benchmarking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import analysis.Crawler;
import analysis.plotWriter.PlotDatWriter;
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
 * @created 09.04.2017 18:34:52
 *
 */
public class BenchmarkingBasics {


	
	/**
	 * @param numberOfIntermediarySoftware
	 * @param benchmarkName
	 * @param budgetQuota
	 * @param vulnProbRce
	 * @param assetAccessReturn TODO
	 * @return
	 */
	public static Crawler createChainCrawler(final int numberOfIntermediarySoftware, final String benchmarkName, final float budgetQuota, final float vulnProbRce, BenchmarkP3Salfer assetAccessReturn) {
		// Test run
		SystemModel sysModel = new SystemModel();
		ArrayList<Software> softwares = new ArrayList<Software>(numberOfIntermediarySoftware);
		for (int i = 1; i <= numberOfIntermediarySoftware; i++) {
			final Software sw = new Software("S"+i, vulnProbRce);
			softwares.add(sw);
			sysModel.addNode(sw);
		}
		
		/* Communication Definition */
		/* A can send to B and C. B and C can send to D. */
		CommunicationMedium earlierCom = new CommunicationMedium("AfterA");
		CommunicationMedium laterCom;
		for (Software sw : softwares) {
			laterCom = new CommunicationMedium("Com" + sw.getName());			
			sw.registerReadsFrom(earlierCom);
			sw.registerWritesTo(laterCom);
			earlierCom = laterCom;
		}
		Asset a = new Asset("ZielD", softwares.get(softwares.size() - 1));
		if (assetAccessReturn != null)
			assetAccessReturn.asset = a; 
		sysModel.addNode(a);
	
		HashSet<Exploit> exploits = new HashSet<Exploit>(numberOfIntermediarySoftware+1);
		for (Software sw : softwares) {
			exploits.add(new PotentialExploit("Ex"+sw.getName(), sw, new Resources(1_000d, 100d)));
		}
		exploits.add(new PotentialExploit("Extraction", a, new Resources(0d,0d)));
		
		AttackerProfile attackerProfile = new AttackerProfile("Attacker", 1, new Resources(numberOfIntermediarySoftware*budgetQuota*1_000d, 1_000d));
		
		Access ac = new Access("Access", softwares.get(0), 1);
		if (assetAccessReturn != null) 
			assetAccessReturn.access = ac; 
		attackerProfile.addAccess(ac);
		
		AttackScenario scenario = new AttackScenario(benchmarkName, sysModel, attackerProfile, exploits);
		Crawler c = new Crawler(scenario);
		return c;
	}



	/**
	 * @param numberOfIntermediarySoftware
	 * @param benchmarkName
	 * @param vulnProbRce
	 * @param budgetinSoftwareNodes
	 * @param benchmarksObject TODO
	 * @return
	 */
	public static Crawler createMeshCrawler(final int numberOfIntermediarySoftware, final String benchmarkName, final float vulnProbRce,
			final int budgetinSoftwareNodes, BenchmarkP3Salfer benchmarksObject) {
		SystemModel sysModel = new SystemModel();
		ArrayList<Software> softwares = new ArrayList<Software>(numberOfIntermediarySoftware);
		for (int i = 1; i <= numberOfIntermediarySoftware; i++) {
			final Software sw = new Software("S"+i, vulnProbRce);
			softwares.add(sw);
			sysModel.addNode(sw);
		}
		
		/* Communication Definition */
		/* A can send to B and C. B and C can send to D. */
		CommunicationMedium oneCom = new CommunicationMedium("MeshCom");
		for (Software sw : softwares) {
			sw.registerReadsWritesOn(oneCom);
		}
		Asset asset = new Asset("Ziel", softwares.get(softwares.size() - 1));
		sysModel.addNode(asset);
	
		HashSet<Exploit> exploits = new HashSet<Exploit>(numberOfIntermediarySoftware+1);
		for (Software sw : softwares) {
			exploits.add(new PotentialExploit("Ex"+sw.getName(), sw, new Resources(1_000d, 100d)));
		}
		exploits.add(new PotentialExploit("Extraction", asset, new Resources(0d,0d)));
		
		AttackerProfile attackerProfile = new AttackerProfile("Attacker", 1, new Resources(budgetinSoftwareNodes*1_000d, budgetinSoftwareNodes*250d));
		Access access = new Access("Access", softwares.get(0), 1);
		attackerProfile.addAccess(access);
		
		AttackScenario scenario = new AttackScenario(benchmarkName, sysModel, attackerProfile, exploits);
		Crawler c = new Crawler(scenario);
		
		if (benchmarksObject != null) {
			benchmarksObject.asset = asset;
			benchmarksObject.access = access;
		}
		return c;
	}



	/**
	 * @param benchmarkName
	 * @param numberOfBusses
	 * @param numberOfSoftwarePerBus
	 * @param vulnProbRce
	 * @param budgetinSoftwareNodes
	 * @param assetAccessObject TODO
	 * @return
	 */
	public static Crawler createMultiBusCrawler(final String benchmarkName, final int numberOfBusses, final int numberOfSoftwarePerBus, final float vulnProbRce,
			final int budgetinSoftwareNodes, BenchmarkP3Salfer assetAccessObject) {
		final SystemModel sysModel = new SystemModel();
		final HashSet<Exploit> exploits = new HashSet<Exploit>(numberOfSoftwarePerBus+1);
		final HashSet<Software> gatewaySw = new HashSet<Software>(numberOfBusses + 1);
		Asset asset = null;
		for (int bus = 1 ; bus <= numberOfBusses ; bus++) {
			CommunicationMedium busCom = new CommunicationMedium("Bus"+bus);
			
			for (int swN = 1 ; swN <= numberOfSoftwarePerBus ; swN++) {
				final Software sw = new Software("S"+bus+"/"+swN, vulnProbRce);
				if (swN == 1) // first node is the gateway
					gatewaySw.add(sw);
				if (swN == 2 && (bus == 2 && numberOfBusses >=2 || numberOfBusses == 1)) { // sw#2 on bus#2 contains the asset. on smaller ones bus#1 respectively.
					asset = new Asset("Ziel"+bus, sw);
					sysModel.addNode(asset);
					exploits.add(new PotentialExploit("Extraction", asset, new Resources(0d,0d)));
				}
				sysModel.addNode(sw);
				sw.registerReadsWritesOn(busCom);
				exploits.add(new PotentialExploit("Ex"+sw.getName(), sw, new Resources(1_000d, 100d)));
			}
		}
		final CommunicationMedium gatewayCom = new CommunicationMedium("Gateway");
		for (Software sw: gatewaySw) {
			sw.registerReadsWritesOn(gatewayCom);
		}
		
		
		final AttackerProfile attackerProfile = new AttackerProfile("Attacker", 1, new Resources(budgetinSoftwareNodes*1_000d, budgetinSoftwareNodes*250d));
		final 		Access access = new Access("Access", gatewaySw.iterator().next(), 1);
		final AttackScenario scenario = new AttackScenario(benchmarkName, sysModel, attackerProfile, exploits);
		final Crawler c = new Crawler(scenario);
		attackerProfile.addAccess(access);
		if (assetAccessObject != null) {
			assetAccessObject.access = access;
			assetAccessObject.asset = asset;
		}
		return c;
	}

	
	static void summarizeAndExportResults(final String benchmarkName, List<MeasurementResults> results, String exportFileName, String... header) {
		// Summarise results.
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, header);
		SortedSet<Integer> nodeNumbers = new TreeSet<Integer>();
		int triesTotal = 0;
		int outOfMemoryOccurences = 0;
		for (MeasurementResults r: results) {
			nodeNumbers.add(r.nodesNumber);
			triesTotal += r.tries;
			outOfMemoryOccurences += r.outOfMemoryErrorOccurences;
		}
//		boolean errorsOccured = false;
		final int iterationsPerLine = triesTotal / nodeNumbers.size();
		for (Integer nodeNumber: nodeNumbers) {
			int errors = 0;
			int tries = 0;
			DescriptiveStatistics statMem = new DescriptiveStatistics();
			DescriptiveStatistics statTime = new DescriptiveStatistics();
			for (MeasurementResults r: results) {
				if (r.nodesNumber == nodeNumber) {
					statTime.addValue(r.runtime);
					statMem.addValue(r.memory);
					errors += r.outOfMemoryErrorOccurences;
					tries  += r.tries;
				}
			}
//			if (errors >= 1)
//				errorsOccured = true; // As soon as errors occur, all further measurements are to be ignored.
			assert iterationsPerLine == statTime.getN();
			assert iterationsPerLine == statMem.getN();
			double memMean = statMem.getMean();
			double timeMean = statTime.getMean();
			double stdErrMem = statMem.getStandardDeviation()/Math.sqrt(statMem.getN()); // getSD() includes Bessel's correction.
			double stdErrTime = statTime.getStandardDeviation()/Math.sqrt(statTime.getN()); // getSD() includes Bessel's correction.
			double memPer5rel = memMean - statMem.getPercentile(5);
			double memPer95rel = statMem.getPercentile(95) - memMean;
			final String timeResult = timeMean!=0 ? "" + timeMean/1_000 : "nan"; // nan is defined in pgfplots as not available. ms to s.
			final String memResult = memMean!=0 ? "" + memMean/1_000_000 : "nan";  // nan is defined in pgfplots as not available. B to MB.
//			p.writeData((errorsOccured ? "#" : "") + nodeNumber, // "#" makes lines ignored by pgfplots
			p.writeData("" + nodeNumber,
					timeResult,
					memResult,
					"" + (float)errors/(float)(tries),
					"" + (stdErrMem/1_000_000), // B to MB
					"" + stdErrTime/1_000, // ms to s.
					"" + memPer5rel/1_000_000, // B to MB
					"" + memPer95rel/1_000_000 // B to MB
				);
		}
		assert (iterationsPerLine == MEASUREMENT_ITERATIONS_PER_DIRECTION * 2); // Iterations are doubled becaus of the back forth measurment.
		p.writeData("# IterationsPerLine=" + iterationsPerLine); 
		p.writeData("# TotalTries=" + triesTotal);
		p.writeData("# TotalMemoryErrors=" + outOfMemoryOccurences);
		p.writeData("# TotalFailureRate=" + (float)outOfMemoryOccurences/(float)triesTotal);
		p.close();
	}

	public static void gc() {
		for (int i = 1 ; i <= 4 ; i++) {
			System.gc();
			try {
				Thread.sleep(100); // 100 ms sleep. Some say it would help the GC in the background.
			} catch (InterruptedException e) {}
		}
	}
	
	public static void main(String...strings) {
		long start = System.currentTimeMillis();
		BenchmarkP3Salfer.main(strings);
		gc();
		BenchmarkP3Bayes.main(strings);
		long diff = System.currentTimeMillis() - start;
		System.out.println("Whole Benchmark done in " + (diff / 1_000) + " s.");
	}
	
	
	static final String[] HEADER_MEM = {"n", "avgMB", "maxMB"};
	static final String[] HEADER_CPU = {"n", "s"};
	static final String[] HEADER = {"n", "s", "memMB", "errorRate", "stdErrMem", "stdErrTime", "memPer5", "memPer95"};
	static final int MEASUREMENT_ITERATIONS_PER_DIRECTION = 2; // results in 4 iterations, as both directions are executed.
	
	static final int CHAIN_LENGTH_MIN = 200;
	static final int CHAIN_LENGTH_MAX = 4_000;
	static final int CHAIN_STEPSIZE = 200;
	static final float CHAIN_VULN_PROB = 0.999f;
	static final float CHAIN_BUDGET__QUOTA = 1.0f;
	
	static final int MESH_SIZE_MIN = 10;
	static final int MESH_SIZE_MAX_SALFER = 210; // results in circa 130s runtime.
	static final int MESH_SIZE_MAX_BAYES = 80; // results in circa 100s runtime and 2.5 GB OutOfMemoryErrors.
	static final int MESH_SIZE_STEPS = 20;
	static final int MESH_BUDGET_AS_SOFTWARE_NODES = 3;
	static final float MESH_VULN_PROB = 0.2f;
	
	static final int MULTIBUS_BUSSES_MIN = 8;
	static final int MULTIBUS_BUSSES_MAX_SALFER = 160;
	static final int MULTIBUS_BUSSES_MAX_BAYES = 40; // results in circa 130s runtime and many OutOfMemoryErrors.
	static final int MULTIBUS_BUSSES_STEPSIZE = 8;
	static final int MULTIBUS_SOFTWARES_PER_BUS = 25;
	static final int MULTIBUS_BUDGET_AS_SOFTWARES = 3;
	static final float MULTIBUS_SOFTWARE_VULN = 0.9f;
	
}
