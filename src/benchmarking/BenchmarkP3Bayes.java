/**
 * 
 */
package benchmarking;


import java.util.ArrayList;
import java.util.List;

import analysis.Crawler;
import attackGraph.AttackGraph;
import unbbayes.prs.bn.JunctionTreeAlgorithm;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.util.extension.bn.inference.IInferenceAlgorithm;

/**
 * @author Martin Salfer
 * @created 09.04.2017 18:02:30
 *
 */
public class BenchmarkP3Bayes {

	
	private static final String CHAIN_DAT = "output/V2Evo/P3Bayes/Chain.dat";
	private static final String MESH_DAT = "output/V2Evo/P3Bayes/Mesh.dat";
	private static final String MULTI_BUS_DAT = "output/V2Evo/P3Bayes/MultiBus.dat";
	
	
	private static void benchmarkChain() {
		final String benchmarkName = "P3BayesChain";

		// Warm Up JVM.
		System.out.print("WarmUp-" + benchmarkName + BenchmarkingBasics.CHAIN_LENGTH_MAX + ": ");
		measure(BenchmarkingBasics.createChainCrawler(BenchmarkingBasics.CHAIN_LENGTH_MAX, benchmarkName, BenchmarkingBasics.CHAIN_BUDGET__QUOTA, BenchmarkingBasics.CHAIN_VULN_PROB, null), BenchmarkingBasics.CHAIN_LENGTH_MAX);

		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		int inc = BenchmarkingBasics.CHAIN_STEPSIZE;
		for (int n = BenchmarkingBasics.CHAIN_LENGTH_MIN ; n >= BenchmarkingBasics.CHAIN_LENGTH_MIN ; n+=inc) {
			if (n > BenchmarkingBasics.CHAIN_LENGTH_MAX) {
				inc = -inc; // reverse iteration direction at Maximum.
				continue;
			}
			for (int i = 1 ; i <= BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION ; i++) { // Create arithmetic mean over several iterations.
				System.out.print(benchmarkName + n + ": ");
				final Crawler c = BenchmarkingBasics.createChainCrawler(n, benchmarkName, BenchmarkingBasics.CHAIN_BUDGET__QUOTA, BenchmarkingBasics.CHAIN_VULN_PROB, null);
				results.add(measure(c, n));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, CHAIN_DAT, BenchmarkingBasics.HEADER);
		System.out.println(benchmarkName + " ended.");
	}


	
	private static void benchmarkMesh() {
		final String benchmarkName = "P3BayesMesh";

		// Warm um JVM.
		System.out.print("WarmUp-" + benchmarkName + BenchmarkingBasics.MESH_SIZE_MAX_BAYES + ": ");
		measure(BenchmarkingBasics.createMeshCrawler(BenchmarkingBasics.MESH_SIZE_MAX_BAYES, benchmarkName+BenchmarkingBasics.MESH_SIZE_MAX_BAYES, 
			BenchmarkingBasics.MESH_VULN_PROB, BenchmarkingBasics.MESH_BUDGET_AS_SOFTWARE_NODES, null), BenchmarkingBasics.MESH_SIZE_MAX_BAYES);
		
		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		int inc = BenchmarkingBasics.MESH_SIZE_STEPS;
		for (int n = BenchmarkingBasics.MESH_SIZE_MIN ; n >= BenchmarkingBasics.MESH_SIZE_MIN; n+=inc) { // P3Bayes is not able to go to the full size.
			if (n > BenchmarkingBasics.MESH_SIZE_MAX_BAYES ) {
				inc = -inc; // reverse iteration direction at Maximum.
				continue;
			}
			for (int i = 1 ; i <= BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION ; i++) { // Create arithmetic mean over several iterations.
				System.out.print(benchmarkName + n + ": ");
				Crawler c = BenchmarkingBasics.createMeshCrawler(n, benchmarkName+n, 
					BenchmarkingBasics.MESH_VULN_PROB, BenchmarkingBasics.MESH_BUDGET_AS_SOFTWARE_NODES, null);
				results.add(measure(c, n));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, MESH_DAT, BenchmarkingBasics.HEADER);
		System.out.println(benchmarkName + " ended.");
		
	}


	private static void benchmarkMultibus() {
		final String benchmarkName = "P3BayesMultiBus";
		
		// Warm up JVM.
		System.out.print("WarmUp-" + benchmarkName + BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS);
		measure(BenchmarkingBasics.createMultiBusCrawler(benchmarkName, BenchmarkingBasics.MULTIBUS_BUSSES_MAX_BAYES, BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS,
				BenchmarkingBasics.MULTIBUS_SOFTWARE_VULN, BenchmarkingBasics.MULTIBUS_BUDGET_AS_SOFTWARES, null), 0);
		
		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		int inc = BenchmarkingBasics.MULTIBUS_BUSSES_STEPSIZE;
		for (int busses = BenchmarkingBasics.MULTIBUS_BUSSES_MIN ; busses >= BenchmarkingBasics.MULTIBUS_BUSSES_MIN ; busses+=inc) {
			if (busses > BenchmarkingBasics.MULTIBUS_BUSSES_MAX_BAYES ) {
				inc = -inc; // reverse iteration direction at Maximum.
				continue;
			}
			final int numberOfNodes = busses*BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS;
			for (int i = 1 ; i <= BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION ; i++) { // Create arithmetic mean over several iterations.
					System.out.print(benchmarkName + numberOfNodes + ": ");
					Crawler c = BenchmarkingBasics.createMultiBusCrawler(benchmarkName, busses, BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS,
							BenchmarkingBasics.MULTIBUS_SOFTWARE_VULN, BenchmarkingBasics.MULTIBUS_BUDGET_AS_SOFTWARES, null);
					results.add(measure(c, numberOfNodes));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, MULTI_BUS_DAT, BenchmarkingBasics.HEADER);
		System.out.println(benchmarkName + " ended.");
	}



	private static MeasurementResults measure(final Crawler c, int numberOfNodes) {
		final MeasurementResults r = new MeasurementResults(numberOfNodes);
//		boolean successfulTry = false;
//		trying: while(!successfulTry) {
		// Crawl
		r.tries++;
		BenchmarkingBasics.gc(); // Start with a tidy memory.
		long timeStart = System.currentTimeMillis();
		AttackGraph ag = c.crawlAndGetAttackGraphOnBayes();
		long timeCrawl = c.getCrawlerRunTimeMilli();
		System.out.print("crawled in " + timeCrawl + " ms; ");
		// Inference
		final ProbabilisticNetwork pn = ag.getAProbabilisticNetwork();
		IInferenceAlgorithm algo = new JunctionTreeAlgorithm();
		algo.setNetwork(pn);
		try {
			algo.run();
//			successfulTry = true;
		} catch (java.lang.OutOfMemoryError e) {
			long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			System.out.print("\nOutOfMemoryError! " + usedMem/1_000_000 + " MB.");
			r.outOfMemoryErrorOccurences++;
//				if (r.outOfMemoryErrorOccurences >= 2) // Bayes dislikes certain system models. Retry once.
//					break trying; // stop trying and return the few results we have.
		} catch (Exception e) {
			// Sometimes UnBBayes fails also with other errors and must not interrupt our benchmark runs.
			System.out.println("Something failed with " + e);
//				break trying; // Such exceptions usually repeat endlessly. UnBBayes often has a NoSuchElementException.
		} finally {
			long timeDif = System.currentTimeMillis() - timeStart;
			long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			r.memory  += usedMem;
			r.runtime += timeDif;
			System.out.println(((float)timeDif)/1_000 + "s in total; " + (usedMem/1_000_000)+ "MB");
			BenchmarkingBasics.gc(); // tidy up afterwards.
		}
//		}
		return r;
	}

	public static void main (String... strings) {
		long startTime = System.currentTimeMillis();
		benchmarkMultibus();
		BenchmarkingBasics.gc();
		benchmarkMesh();
		BenchmarkingBasics.gc();
		benchmarkChain();
		System.out.println("Runtime: " + (System.currentTimeMillis() - startTime) / 1_000 + "s.");
	}
	
}
