/**
 * 
 */
package benchmarking;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import benchmarking.BenchmarkP3Salfer.TestTarget;

/**
 * @author Martin Salfer
 * @created 27.05.2017 18:33:46
 *
 */
@Ignore("Takes about 60 seconds. Active only for benchmarking.")
public class BenchmarkP3SalferTest {

	private static final String DAT = "output/test/P3Salfer-Chain.dat";
	

	/**
	 * This test checks for a sizeable output and validates the assertions.
	 */
	@Test(timeout=100000) // Test takes about 60 seconds, 100s as cancellation limit.
	public final void testChainBenchmark() {
		
		long startTime = System.currentTimeMillis();
		BenchmarkingBasics.gc();
		new BenchmarkP3Salfer().testChainFullPlotExport(DAT, BenchmarkingBasics.MEASUREMENT_ITERATIONS_PER_DIRECTION, TestTarget.BOTH);
		System.out.println("Runtime of testChainBenchmark: " + (System.currentTimeMillis() - startTime) / 1_000 + "s.");
		
		// Post-Writer Assertions.
		assertTrue(new File(DAT).exists());
		assertTrue(Math.abs(2000 - new File(DAT).length()) <= 500); // 500 Byte difference allowed.
	}

}
