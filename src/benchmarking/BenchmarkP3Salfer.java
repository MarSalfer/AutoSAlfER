/**
 * 
 */
package benchmarking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import analysis.Crawler;
import analysis.plotWriter.PlotDatWriter;
import attackGraph.AttackGraph;
import attackGraph.visualization.GexfExporter;
import attackerProfile.Access;
import systemModel.Asset;

/**
 * @author Martin Salfer
 * @created 05.03.2017 21:44:07
 *
 */
public class BenchmarkP3Salfer {

	private static final String CHAIN_CPU_DAT = "output/V2Evo/P3Salfer/RuntimeChain.dat";
	private static final String CHAIN_DAT = "output/V2Evo/P3Salfer/Chain.dat";
	private static final String CHAIN_MEM_DAT = "output/V2Evo/P3Salfer/MemoryChain.dat";
	private static final String MESH_CPU_DAT = "output/V2Evo/P3Salfer/RuntimeMesh.dat";
	private static final String MESH_MEM_DAT = "output/V2Evo/P3Salfer/MemoryMesh.dat";
	private static final String MESH_DAT = "output/V2Evo/P3Salfer/Mesh.dat";
	private static final String MULTI_BUS_CPU_DAT = "output/V2Evo/P3Salfer/RuntimeMultiBus.dat";
	private static final String MULTI_BUS_MEM_DAT = "output/V2Evo/P3Salfer/MemoryMultiBus.dat";
	private static final String MULTI_BUS_DAT = "output/V2Evo/P3Salfer/MultiBus.dat";
	
	public static final String GEXF_MULTI_BUS = "output/V2Evo/P3Salfer/MultiBus.gexf";
	
	public enum TestTarget {MEMORY, CPU, BOTH };

	public Asset asset;
	public Access access;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		asset = null;
		access = null;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test(timeout=200)
	public final void testChainCapped() {
		// Options
		final int numberOfIntermediarySoftware = 1_000;
		final String benchmarkName = "BenchmarkChainCapped" + numberOfIntermediarySoftware;
		final float budgetQuota = 0.1f;
		final float vulnProbRce = 0.50f;

		AttackGraph ag = runChain(numberOfIntermediarySoftware, benchmarkName, budgetQuota, vulnProbRce, false).getAttackGraphP3Salfer(); 
		
		/* Test Assertions*/
		assertNotNull(ag);
		assertTrue(ag.getAllAppliedExploits().isEmpty());
		assertTrue(ag.getAllAttackGraphNodes().isEmpty());
		assertTrue(ag.getAllUsedAccesses().isEmpty());
		assertTrue(ag.getAttackPathTo(asset).isEmpty());
		assertTrue(ag.getNumberOfAccesses() == 0);
		assertTrue(ag.getNumberOfEdges() == 0);
		assertTrue(ag.getNumberOfNodes() == 0);
		assertTrue(ag.getUsedAccessForAsset(asset) == null);
	}

	/**
	 * @param numberOfIntermediarySoftware
	 * @param benchmarkName
	 * @param budgetQuota
	 * @param vulnProbRce
	 * @param memMeasurment TODO
	 * @return
	 */
	private Crawler runChain(final int numberOfIntermediarySoftware, final String benchmarkName, final float budgetQuota, final float vulnProbRce, boolean memMeasurment) {
		Crawler c = BenchmarkingBasics.createChainCrawler(numberOfIntermediarySoftware, benchmarkName, budgetQuota, vulnProbRce, this);
		
		System.out.print(benchmarkName + ": Initialized. Crawling...");
		c.crawlAndGetAttackGraphOnSalfer(memMeasurment);
		System.out.println("done in " + c.getCrawlerRunTimeMilli() + "ms.");
		return c;
	}

	@Test(timeout=300)
	public final void testChainFull() {
		// Options
		final int numberOfIntermediarySoftware = 1_000;
		final String benchmarkName = "BenchmarkChainFull" + numberOfIntermediarySoftware;
		final float budgetQuota = 1.0f;
		final float vulnProbRce = 0.999f;

		AttackGraph ag = runChain(numberOfIntermediarySoftware, benchmarkName, budgetQuota, vulnProbRce, false).getAttackGraphP3Salfer();
		
		/* Test Assertions*/
		assertNotNull(ag);
		assertEquals(numberOfIntermediarySoftware + 1, ag.getAllAppliedExploits().size());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getAllAttackGraphNodes().size());
		assertEquals(1, ag.getAllUsedAccesses().size());
		assertEquals(numberOfIntermediarySoftware + 1, ag.getAttackPathTo(asset).size());
		assertEquals(1, ag.getNumberOfAccesses());
		assertEquals(numberOfIntermediarySoftware + 1, ag.getNumberOfEdges());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getNumberOfNodes());
		assertEquals(access, ag.getUsedAccessForAsset(asset));
		assertEquals(0.37d, ag.getVulnerabilityProbabilityMeanForPathsThrough(asset), 0.01d);
	}

	
	/**
	 * Calculates a mesh network with a less potent attacker profile.
	 */
	@Test(timeout=500)
	public final void testMeshPoorAttacker() {
		// Options
		final int numberOfIntermediarySoftware = 100;
		final String benchmarkName = "BenchmarkMeshShort" + numberOfIntermediarySoftware;
		final float vulnProbRce = 0.2f;
		final int budgetinSoftwareNodes = 2;

		AttackGraph ag = runMesh(numberOfIntermediarySoftware, benchmarkName, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		
		/* Test Assertions*/
		assertNotNull(ag);
		assertEquals(numberOfIntermediarySoftware + 1, ag.getAllAppliedExploits().size());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getAllAttackGraphNodes().size());
		assertEquals(1, ag.getAllUsedAccesses().size());
		assertTrue(ag.getAttackPathTo(asset).size() <= numberOfIntermediarySoftware + 1);
		assertTrue(ag.getAttackPathTo(asset).size() >= 2);
		assertEquals(1, ag.getNumberOfAccesses());
		assertEquals(199, ag.getNumberOfEdges());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getNumberOfNodes());
		assertEquals(access, ag.getUsedAccessForAsset(asset));
		assertEquals(0.01, ag.getVulnerabilityProbabilityMeanForPathsThrough(asset), 0.01d);
	}

	/**
	 * Calculates a mesh network with a more potent attacker profile.
	 */
	@Test(timeout=25000)
	@Ignore // This test takes about 20s. 
	public final void testMeshSponsoredAttacker() {
		// Options
		final int numberOfIntermediarySoftware = 100;
		final String benchmarkName = "BenchmarkMeshLong" + numberOfIntermediarySoftware;
		final float vulnProbRce = 0.2f;
		final int budgetinSoftwareNodes = 3;
		
		AttackGraph ag = runMesh(numberOfIntermediarySoftware, benchmarkName, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		
		/* Test Assertions*/
		assertNotNull(ag);
		assertEquals(numberOfIntermediarySoftware + 1, ag.getAllAppliedExploits().size());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getAllAttackGraphNodes().size());
		assertEquals(1, ag.getAllUsedAccesses().size());
		assertTrue(ag.getAttackPathTo(asset).size() <= numberOfIntermediarySoftware + 1);
		assertTrue(ag.getAttackPathTo(asset).size() >= 2);
		assertEquals(1, ag.getNumberOfAccesses());
		assertEquals(9705, ag.getNumberOfEdges());
		assertEquals(numberOfIntermediarySoftware + 2, ag.getNumberOfNodes());
		assertEquals(access, ag.getUsedAccessForAsset(asset));
		assertEquals(1.00, ag.getVulnerabilityProbabilityMeanForPathsThrough(asset), 0.01d);
	}

	/**
	 * @param numberOfIntermediarySoftware
	 * @param benchmarkName
	 * @param vulnProbRce
	 * @param budgetinSoftwareNodes
	 * @param memMeasurement TODO
	 * @return
	 */
	private Crawler runMesh(final int numberOfIntermediarySoftware, final String benchmarkName, final float vulnProbRce, final int budgetinSoftwareNodes, boolean memMeasurement) {
		// Test run
		System.out.print(benchmarkName + ": ");
		Crawler c = BenchmarkingBasics.createMeshCrawler(numberOfIntermediarySoftware, benchmarkName, vulnProbRce, budgetinSoftwareNodes, this);
		
		System.out.print("initialized. Crawling...");
		c.crawlAndGetAttackGraphOnSalfer(memMeasurement);
		System.out.println("done in " + c.getCrawlerRunTimeMilli() + "ms.");
		return c;
	}


	
//	@Test(timeout=600)
//	@Ignore
//	public final void testSlimStar() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	
//	@Test(timeout=600)
//	@Ignore
//	public final void testFatStar() {
//		fail("Not yet implemented"); // TODO
//	}
	
	
	@Test
	public final void testMultiBus() {
		final String benchmarkName = "MultiBus";
		final int numberOfBusses = 5;
		final int numberOfSoftwarePerBus = 8;
		final float vulnProbRce = 0.9f; 
		final int budgetinSoftwareNodes = 3;
		
		AttackGraph ag = runMultiBus(benchmarkName + (numberOfBusses*numberOfSoftwarePerBus), numberOfBusses, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		
		new GexfExporter().exportAttackGraphToGexfFile(ag, GEXF_MULTI_BUS);
		
		assertTrue(9 <= ag.getAllAppliedExploits().size() && ag.getAllAppliedExploits().size() <= 13); // TODO Unterschiedliches Ergebnis falls die Methode zusammen mit Anderern dieser Klasse getestet wird kommt 9 raus.
		assertTrue(10 <= ag.getAllAttackGraphNodes().size() && ag.getAllAttackGraphNodes().size() <= 14);
		assertTrue(ag.getNumberOfNodes() == ag.getAllAttackGraphNodes().size());
		assertEquals(1, ag.getAllUsedAccesses().size());
		assertEquals(1, ag.getNumberOfAccesses());
		assertTrue(2 <= ag.getAttackPathTo(asset).size());
		assertTrue(7 >= ag.getAttackPathTo(asset).size());
		assertTrue(22 <= ag.getNumberOfEdges() && ag.getNumberOfEdges() <= 45);
		assertEquals(access, ag.getUsedAccessForAsset(asset));
		assertEquals(0.6633899316358594, ag.getVulnerabilityProbabilityMeanForPathsThrough(asset), 0.01d);
	}

	/**
	 * @param benchmarkName
	 * @param numberOfBusses
	 * @param numberOfSoftwarePerBus
	 * @param vulnProbRce
	 * @param budgetinSoftwareNodes
	 * @param memMeasurement TODO
	 */
	private Crawler runMultiBus(final String benchmarkName, final int numberOfBusses, final int numberOfSoftwarePerBus, final float vulnProbRce,
			final int budgetinSoftwareNodes, boolean memMeasurement) {
		// Test run
		System.out.print(benchmarkName + ": ");
		Crawler c = BenchmarkingBasics.createMultiBusCrawler(benchmarkName, numberOfBusses, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, this);
		
		System.out.print("initialized. Crawling...");
		c.crawlAndGetAttackGraphOnSalfer(memMeasurement);
		System.out.println("done in " + c.getCrawlerRunTimeMilli() + "ms.");
		return c;
	}

	@Test
	public final void testRandom() {
//		final String benchmarkName = "Random";
//		final int numberOfSoftware = 2;
//		final float vulnProbRce = 0.9f; 
//		final int budgetinSoftwareNodes = 3;
//		
//		AttackGraph ag = runMultiBus(benchmarkName + numberOfSoftware, numberOfSoftware, vulnProbRce, budgetinSoftwareNodes);
//		
//		new GexfExporter().exportAttackGraphToGexfFile(ag, GEXF_MULTI_BUS);
//		
//		assertEquals(numberOfBusses*(numberOfSoftware + 1), ag.getAllAppliedExploits().size());
//		assertEquals(numberOfBusses*(numberOfSoftware + 1) + 1, ag.getAllAttackGraphNodes().size());
//		assertEquals(1, ag.getAllUsedAccesses().size());
//		assertEquals(1, ag.getNumberOfAccesses());
//		assertEquals(4, ag.getAttackPathTo(asset).size());
//		assertEquals(237, ag.getNumberOfEdges());
//		assertEquals(numberOfBusses*(numberOfSoftware+1) + 1, ag.getNumberOfNodes());
//		assertEquals(access, ag.getUsedAccessForAsset(asset));
//		assertEquals(0.41329612710791536, ag.getVulnerabilityProbabilityMeanForPathsThrough(asset), 0.01d);
	}
	
	final void testChainFullPlotExport(String exportFileName, int measurementIterations, TestTarget withMemMeasure) {
		// Options
		final int numberOfIntermediarySoftwareMax = BenchmarkingBasics.CHAIN_LENGTH_MAX;
		final float budgetQuota = BenchmarkingBasics.CHAIN_BUDGET__QUOTA;
		final float vulnProbRce = BenchmarkingBasics.CHAIN_VULN_PROB;

		switch (withMemMeasure) {
		case CPU:
			benchmarkChainRuntime(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "BenchmarkChainCpu", budgetQuota, vulnProbRce);
			break;
		case MEMORY:
			benchmarkChainMemory(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "BenchmarkChainMem", budgetQuota, vulnProbRce);
			break;
		case BOTH:
			benchmarkChain(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "P3SalferChain", budgetQuota, vulnProbRce);
		}
	}

	/**
	 * @param exportFileName
	 * @param measurementIterations
	 * @param numberOfIntermediarySoftwareMax
	 * @param benchmarkName
	 * @param budgetQuota
	 * @param vulnProbRce
	 */
	private void benchmarkChainRuntime(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float budgetQuota, final float vulnProbRce) {
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, BenchmarkingBasics.HEADER_CPU);

		// first run for warming up the JVM.
		runChain(numberOfIntermediarySoftwareMax, benchmarkName+numberOfIntermediarySoftwareMax, budgetQuota, vulnProbRce, false);
		BenchmarkingBasics.gc();

		// crawling and measuring
		for (int n = 100 ; n <= numberOfIntermediarySoftwareMax ; n+=50) {
			final int iMax = measurementIterations;
			long timeAcc = 0;
			for (int i = 1 ; i <= iMax ; i++) { // Create arithmetic mean over several iterations.
				timeAcc += runChain(n, benchmarkName+n, budgetQuota, vulnProbRce, false).getCrawlerRunTimeMilli();
			}
			p.writeData("" + n, "" + timeAcc/iMax);
			BenchmarkingBasics.gc();
		}
		p.close();
	}
	
	/**
	 * @param exportFileName
	 * @param measurementIterations
	 * @param numberOfIntermediarySoftwareMax
	 * @param benchmarkName
	 * @param budgetQuota
	 * @param vulnProbRce
	 */
	private void benchmarkChain(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float budgetQuota, final float vulnProbRce) {
		// first run for warming up the JVM.
		System.out.print(benchmarkName + numberOfIntermediarySoftwareMax + ": ");
		measure(BenchmarkingBasics.createChainCrawler(numberOfIntermediarySoftwareMax, benchmarkName+numberOfIntermediarySoftwareMax, budgetQuota, vulnProbRce, this), numberOfIntermediarySoftwareMax);
		
		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		final int min = BenchmarkingBasics.CHAIN_LENGTH_MIN;
		int step = BenchmarkingBasics.CHAIN_STEPSIZE;
		for (int n = min ; n >= min ; n+=step) {
			if (n > numberOfIntermediarySoftwareMax) {
				step = -step; // revert iteration direction for compensating JVM optimisation during backwards run.
				continue; 
			}
			final int iMax = measurementIterations;
			for (int i = 1 ; i <= iMax ; i++) { // Create arithmetic mean over several iterations.
				System.out.print(benchmarkName + n + ": ");
				Crawler c = BenchmarkingBasics.createChainCrawler(n, benchmarkName+n, budgetQuota, vulnProbRce, this);
				results.add(measure(c, n));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, exportFileName, BenchmarkingBasics.HEADER);
	}

	private final void testMeshFullPlotExport(String exportFileName, int measurementIterations, TestTarget testTarget) {
		// Options
		final int numberOfIntermediarySoftwareMax = BenchmarkingBasics.MESH_SIZE_MAX_SALFER;
		final float vulnProbRce = BenchmarkingBasics.MESH_VULN_PROB;
		final int budgetinSoftwareNodes = BenchmarkingBasics.MESH_BUDGET_AS_SOFTWARE_NODES;

		switch (testTarget) {
		
		case CPU:
			benchmarkMeshRuntime(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "BenchmarkMeshCPU", vulnProbRce, budgetinSoftwareNodes);
			break;
		case MEMORY:
			benchmarkMeshMemory(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "BenchmarkMeshMem", vulnProbRce, budgetinSoftwareNodes);
			break;
		case BOTH:
			benchmarkMeshBoth(exportFileName, measurementIterations, numberOfIntermediarySoftwareMax, "P3SalferMesh", vulnProbRce, budgetinSoftwareNodes);
		}
	}

	private void benchmarkMeshRuntime(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float vulnProbRce, final int budgetinSoftwareNodes) {
		// Prepare
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, BenchmarkingBasics.HEADER_CPU);
		// Warm um JVM.
		runMesh(numberOfIntermediarySoftwareMax, benchmarkName+numberOfIntermediarySoftwareMax, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		BenchmarkingBasics.gc();
		
		// crawling and measuring
		for (int n = 10 ; n <= numberOfIntermediarySoftwareMax ; n+=10) {
			long timeAcc = 0;
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				timeAcc += runMesh(n, benchmarkName+n, vulnProbRce, budgetinSoftwareNodes, false).getCrawlerRunTimeMilli();
			}
			p.writeData("" + n, "" + timeAcc/measurementIterations);
			BenchmarkingBasics.gc();
		}
		p.close();
	}

	private void benchmarkMeshMemory(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float vulnProbRce, final int budgetinSoftwareNodes) {
		// Prepare
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, BenchmarkingBasics.HEADER_MEM);
		// Warm um JVM.
		runMesh(numberOfIntermediarySoftwareMax, benchmarkName+numberOfIntermediarySoftwareMax, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		BenchmarkingBasics.gc();
		
		// crawling and measuring
		for (int n = 10 ; n <= numberOfIntermediarySoftwareMax ; n+=10) {
			long memMaxAcc = 0;
			long memAvgAcc = 0; 
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				Crawler c = runMesh(n, benchmarkName+n, vulnProbRce, budgetinSoftwareNodes, true);
				memAvgAcc += c.getCrawlerMemAvg();
				memMaxAcc += c.getCrawlerMemMax();
			}
			p.writeData("" + n, "" + memAvgAcc/measurementIterations/1_000_000, "" + memMaxAcc/measurementIterations/1_000_000);
			BenchmarkingBasics.gc();
		}
		p.close();
	}
	
	private void benchmarkMeshBoth(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float vulnProbRce, final int budgetinSoftwareNodes) {
		// Prepare
		// Warm um JVM.
		System.out.print("WarmUp-" + benchmarkName + numberOfIntermediarySoftwareMax + ": ");
		measure(BenchmarkingBasics.createMeshCrawler(numberOfIntermediarySoftwareMax, benchmarkName, vulnProbRce, budgetinSoftwareNodes, this), numberOfIntermediarySoftwareMax);

		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		final int min = BenchmarkingBasics.MESH_SIZE_MIN;
		int step = BenchmarkingBasics.MESH_SIZE_STEPS;
		for (int n = min ; n >= min; n+=step) {
			if (n > numberOfIntermediarySoftwareMax) {
				step = -step; // Revert step direction for evening out JVM optimisation during run.
				continue;
			}
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				System.out.print(benchmarkName + n + ": ");
				Crawler c = BenchmarkingBasics.createMeshCrawler(n, benchmarkName, vulnProbRce, budgetinSoftwareNodes, this);
				results.add(measure(c, n));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, exportFileName, BenchmarkingBasics.HEADER);
	}
	
	
	private final void testMultiBusPlotExport(String exportFileName, int measurementIterations, TestTarget testTarget) {
		final int numberOfBussesMax = BenchmarkingBasics.MULTIBUS_BUSSES_MAX_SALFER;
		final int numberOfSoftwarePerBus = BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS;
		final float vulnProbRce = BenchmarkingBasics.MULTIBUS_SOFTWARE_VULN; 
		final int budgetinSoftwareNodes = BenchmarkingBasics.MULTIBUS_BUDGET_AS_SOFTWARES;

		switch (testTarget) {
		case CPU:
			benchmarkMultiBusCpu(exportFileName, measurementIterations, "MultiBusCpu", numberOfBussesMax, numberOfSoftwarePerBus, vulnProbRce,
					budgetinSoftwareNodes);
			break;
		case MEMORY:
			benchmarkMultiBusMem(exportFileName, measurementIterations, "MultiBusMem", numberOfBussesMax, numberOfSoftwarePerBus, vulnProbRce,
					budgetinSoftwareNodes);
			break;
		case BOTH:
			benchmarkMultiBus(exportFileName, measurementIterations, "P3SalferMultiBus", numberOfBussesMax, numberOfSoftwarePerBus, vulnProbRce,
					budgetinSoftwareNodes);
			break;
		}
	}

	private void benchmarkMultiBus(String exportFileName, int measurementIterations, final String benchmarkName, final int numberOfBussesMax,
			final int numberOfSoftwarePerBus, final float vulnProbRce, final int budgetinSoftwareNodes) {
		int step = BenchmarkingBasics.MULTIBUS_BUSSES_STEPSIZE;
		final int min = BenchmarkingBasics.MULTIBUS_BUSSES_MIN;

		// Warming Up JVM
//		final long start = System.currentTimeMillis();
//		for (int numberOfBusses = min ; numberOfBusses <= numberOfBussesMax ; numberOfBusses+=step*4) {
//			final int n = numberOfBusses*numberOfSoftwarePerBus;
		final int nMax = BenchmarkingBasics.MULTIBUS_BUSSES_MAX_SALFER*BenchmarkingBasics.MULTIBUS_SOFTWARES_PER_BUS;
		System.out.print("WarmUp-" + benchmarkName + nMax + ": ");
		measure(BenchmarkingBasics.createMultiBusCrawler(benchmarkName + nMax, BenchmarkingBasics.MULTIBUS_BUSSES_MAX_SALFER, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, this), nMax);
//		}
//		System.out.println("WarmUp took " + (System.currentTimeMillis() - start) / 1_000 + "s.");
		// crawling and measuring
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();
		for (int numberOfBusses = min ; numberOfBusses >= min ; numberOfBusses+=step) {
			if (numberOfBusses > numberOfBussesMax) {
				step = -step; // revert iteration direction. Two phases to even out JVM optimisation.
				continue;
			}
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				final int n = numberOfBusses*numberOfSoftwarePerBus;
				System.out.print("Measure-" + benchmarkName + n + ": ");
				Crawler c = BenchmarkingBasics.createMultiBusCrawler(benchmarkName + n, numberOfBusses, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, this);
				results.add(measure(c, n));
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, exportFileName, BenchmarkingBasics.HEADER);
	}

	private static MeasurementResults measure(Crawler c, int n) {
		final MeasurementResults r = new MeasurementResults(n);
		BenchmarkingBasics.gc(); // Start with a tidy memory.
		r.tries++;
		System.out.print("initialized. Crawling...");
		c.crawlAndGetAttackGraphOnSalfer(true);
		r.memory  = (long)c.getCrawlerMemAvg();
		r.runtime = c.getCrawlerRunTimeMilli();
		System.out.println(((float)r.runtime)/1_000 + "s in total; " + (r.memory/1_000_000)+ "MB");
		return r;
	}
	
	private void benchmarkMultiBusCpu(String exportFileName, int measurementIterations, final String benchmarkName, final int numberOfBussesMax,
			final int numberOfSoftwarePerBus, final float vulnProbRce, final int budgetinSoftwareNodes) {
		// Prepare
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, BenchmarkingBasics.HEADER_CPU);
		// Warm up JVM.
		runMultiBus(benchmarkName + numberOfBussesMax*numberOfSoftwarePerBus, numberOfBussesMax, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		BenchmarkingBasics.gc();
		
		// crawling and measuring
		for (int n = 1 ; n <= numberOfBussesMax ; n+=2) {
			long timeAcc = 0;
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				timeAcc += runMultiBus(benchmarkName + n*numberOfSoftwarePerBus, n, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, false).getCrawlerRunTimeMilli();
			}
			p.writeData("" + n*numberOfSoftwarePerBus, "" + timeAcc/measurementIterations);
			BenchmarkingBasics.gc();
		}
		p.close();
	}
	
	private void benchmarkMultiBusMem(String exportFileName, int measurementIterations, final String benchmarkName, final int numberOfBussesMax,
			final int numberOfSoftwarePerBus, final float vulnProbRce, final int budgetinSoftwareNodes) {
		// Prepare
		PlotDatWriter p = new PlotDatWriter(benchmarkName, exportFileName, BenchmarkingBasics.HEADER_MEM);
		// Warm up JVM.
		runMultiBus(benchmarkName + numberOfBussesMax*numberOfSoftwarePerBus, numberOfBussesMax, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, false).getAttackGraphP3Salfer();
		BenchmarkingBasics.gc();
		
		// crawling and measuring
		for (int n = 1 ; n <= numberOfBussesMax ; n+=2) {
			long memMaxAcc = 0;
			long memAvgAcc = 0;
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				Crawler c = runMultiBus(benchmarkName + n*numberOfSoftwarePerBus, n, numberOfSoftwarePerBus, vulnProbRce, budgetinSoftwareNodes, true);
				memAvgAcc += c.getCrawlerMemAvg();
				memMaxAcc += c.getCrawlerMemMax();
			}
			p.writeData("" + n*numberOfSoftwarePerBus, "" + memAvgAcc/measurementIterations/1_000_000, "" + memMaxAcc/measurementIterations/1_000_000);
			BenchmarkingBasics.gc();
		}
		p.close();
	}
	

	private void benchmarkChainMemory(String exportFileName, int measurementIterations, final int numberOfIntermediarySoftwareMax, final String benchmarkName,
			final float budgetQuota, final float vulnProbRce) {
		List<MeasurementResults> results = new ArrayList<MeasurementResults>();

		// first run for warming up the JVM.
		runChain(numberOfIntermediarySoftwareMax, benchmarkName+numberOfIntermediarySoftwareMax, budgetQuota, vulnProbRce, false);
		BenchmarkingBasics.gc();

		// crawling and measuring
		for (int n = 100 ; n <= numberOfIntermediarySoftwareMax ; n+=50) {
			for (int i = 1 ; i <= measurementIterations ; i++) { // Create arithmetic mean over several iterations.
				final MeasurementResults r = new MeasurementResults(n);
				System.out.print(benchmarkName + n + ": ");
				BenchmarkingBasics.gc(); // Start with a tidy memory.
				r.tries++;
				final Crawler c = runChain(n, benchmarkName+n, budgetQuota, vulnProbRce, false);
				long timeDif = c.getCrawlerRunTimeMilli();
				long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				r.memory  = usedMem;
				r.runtime = timeDif;
				results.add(r);
				System.out.println(timeDif + "ms in total; " + (usedMem/1_000_000)+ "MB");
			}
		}
		BenchmarkingBasics.summarizeAndExportResults(benchmarkName, results, exportFileName, BenchmarkingBasics.HEADER);
	}


	private static void runtimeMeasurement() {
		new BenchmarkP3Salfer().testChainFullPlotExport(BenchmarkP3Salfer.CHAIN_CPU_DAT, 1, TestTarget.CPU);
		new BenchmarkP3Salfer().testMeshFullPlotExport(BenchmarkP3Salfer.MESH_CPU_DAT, 1, TestTarget.CPU);
		new BenchmarkP3Salfer().testMultiBusPlotExport(MULTI_BUS_CPU_DAT, 1, TestTarget.CPU);
	}
	
	
	private static void memoryMeasurment() {
		new BenchmarkP3Salfer().testChainFullPlotExport(CHAIN_MEM_DAT, 1, TestTarget.MEMORY);
		new BenchmarkP3Salfer().testMeshFullPlotExport(MESH_MEM_DAT, 1, TestTarget.MEMORY);
		new BenchmarkP3Salfer().testMultiBusPlotExport(MULTI_BUS_MEM_DAT, 1, TestTarget.MEMORY);
	}
	
	public static void main(String... strings) {
		long startTime = System.currentTimeMillis();
//		runtimeMeasurement();
//		memoryMeasurment();
//		singleMeasurment();
		new BenchmarkP3Salfer().testMultiBusPlotExport(MULTI_BUS_DAT, BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION, TestTarget.BOTH);
		BenchmarkingBasics.gc();
		new BenchmarkP3Salfer().testMeshFullPlotExport(MESH_DAT, BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION, TestTarget.BOTH);
		BenchmarkingBasics.gc();
		new BenchmarkP3Salfer().testChainFullPlotExport(CHAIN_DAT, BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION, TestTarget.BOTH);
		System.out.println("Runtime: " + (System.currentTimeMillis() - startTime) / 1_000 + "s.");
	}

	private static void singleMeasurment() {
		final int numberOfBusses = 120;
		final int numberOfSoftwarePerBus = 25;
		final int n = numberOfBusses * numberOfSoftwarePerBus;
		System.out.print("Single-P3SalferMultiBus" + n + ": ");
		Crawler c = BenchmarkingBasics.createMultiBusCrawler("", numberOfBusses, numberOfSoftwarePerBus, BenchmarkingBasics.MULTIBUS_SOFTWARE_VULN, BenchmarkingBasics.MULTIBUS_BUDGET_AS_SOFTWARES, null);
		measure(c, 1500);
	}
	
	
}
