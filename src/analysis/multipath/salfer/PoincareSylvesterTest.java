/**
 * 
 */
package analysis.multipath.salfer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Martin Salfer
 * @created 29.05.2016 18:45:52
 *
 */
public class PoincareSylvesterTest {

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * This test gets the probability with a dissimilar implementation
	 * and is constrained to combining two input variables, i.e, k=2.
	 */
	@Test
	public final void staticY2Test() {
		PoincareSylvester s = new PoincareSylvester();
		s.addEventAndProbability("A", 0.1d);
		s.addEventAndProbability("B", 0.1d);
		s.setUnificationAmount(2);
		assertEquals(0.19d, s.getTotalProbability(), 0.000001d);
		s.setUnificationAmount(1);
		assertEquals(0.19d, s.getTotalProbability(), 0.000001d);
		
		s.addEventAndProbability("C", 0.1d);
		s.setUnificationAmount(1);
		assertEquals(0.271d, s.getTotalProbability(), 0.000001d);
		s.setUnificationAmount(2);
		assertEquals(0.271d, s.getTotalProbability(), 0.000001d);
	}

	@Test
	public final void unifyByBenchmark() {
		long start, stop;
		double result;
		
		// Preparation
		start = System.nanoTime();
		PoincareSylvester s = new PoincareSylvester();
		for (int i = 1 ; i <= 1_000_000 ; i++) {
			s.addEventAndProbability("E"+i, 0.000_001d);
		}
		stop = System.nanoTime();
		System.out.println("Vorbereitung in " + (stop-start)/1_000_000 + " ms.");

		for (int i=0; i<10; i++) {
			// Benchmark for unifyBy1
			s.setUnificationAmount(1);
			start = System.nanoTime();
			result = s.getTotalProbability();
			stop = System.nanoTime();
			System.out.println("Result unifyBy1 (" + result + ") in " + (stop-start)/1_000_000 + " ms.");
		}

		// Benchmark for unifyBy2
		for (int i=0; i<10; i++) {
			s.setUnificationAmount(2);
			start = System.nanoTime();
			result = s.getTotalProbability();
			stop = System.nanoTime();
			System.out.println("Result unifyBy2 (" + result + ") in " + (stop-start)/1_000_000 +" ms.");
		}
		
		// Benchmark for unifyBy3
		for (int i=0; i<10; i++) {
			s.setUnificationAmount(3);
			start = System.nanoTime();
			result = s.getTotalProbability();
			stop = System.nanoTime();
			System.out.println("Result unifyBy3 (" + result + ") in " + (stop-start)/1_000_000 +" ms.");
		}
	}

	
	@Test
	public final void totalProbability1Test() {
		PoincareSylvester s = new PoincareSylvester();
		s.addEventAndProbability("A", 0.1d);
		s.setUnificationAmount(1);
		assertEquals(0.1d, s.getTotalProbability(), 0.000_001d);
	}

	@Test
	public final void totalProbability2Test() {
		PoincareSylvester s = new PoincareSylvester();
		s.addEventAndProbability("A", 0.1d);
		s.addEventAndProbability("B", 0.1d);
		s.setUnificationAmount(1);
		assertEquals(0.19d, s.getTotalProbability(), 0.000_001d);
		s.setUnificationAmount(2);
		assertEquals(0.19d, s.getTotalProbability(), 0.000_001d);
	}

	@Test
	public final void totalProbability3Test() {
		PoincareSylvester s = new PoincareSylvester();
		s.addEventAndProbability("A", 0.1d);
		s.addEventAndProbability("B", 0.1d);
		s.addEventAndProbability("C", 0.1d);
		s.setUnificationAmount(1);
		assertEquals(0.271d, s.getTotalProbability(), 0.000_001d);
		s.setUnificationAmount(2);
		assertEquals(0.271d, s.getTotalProbability(), 0.000_001d);
		s.setUnificationAmount(3);
		assertEquals(0.271d, s.getTotalProbability(), 0.000_001d);
	}

	
	/**
	 * Math Check.
	 * Path combination at beginning
	 */
	@Test
	@Ignore
	public final void mathPathCombinationBeginningTest() {
		fail("not implemented, yet.");
	}
	
	/**
	 * Math Check.
	 * Path combination at end.
	 */
	@Test
	@Ignore
	public final void mathPathCombinationEndTest() {
		fail("not implemented, yet.");
	}
	
	/**
	 * Math Check.
	 * Combination of many paths.
	 */
	@Test
	@Ignore
	public final void mathManyPathCombinationTest() {
		fail("not implemented, yet.");
	}
	
	
	
	/**
	 * Die Varianz muss größer werden, da die Weg-Diversität steigt.
	 * ==> Testfall: Mehrpfad-Varianz deutlich höher?
	 */
	@Test
	@Ignore
	public final void higherVarianceTest() {
		fail("not implemented, yet.");
	}
	
	/**
	 * Die durchschnittlichen Kosten werden vermutlich steigen,
	 * da auch teurere Wege mit eingerechnet werden,
	 * was aber durch eine deutlich höhere Diversität sich ausgleichen sollte.
	 * ==> Testfall: Mehrpfad-Kosten höher bei zugleich höherer Wahrscheinlichkeit?
	 */
	@Test
	@Ignore
	public final void higherAverageCosts() {
		fail("not implemented, yet.");
	}
	
	/**
	 * Die Wahrscheinlichkeit muss größer werden, da es mehr Möglichkeiten gibt.
	 * (Bestimmt durch µ und Varianz.)
	 * ==> Testfall: Mehrpfad-Wahrscheinlichkeit höher auf Durchschlag?!
	 */
	@Test
	@Ignore
	public final void higherProbabilityTest() {
		fail("not implemented, yet.");
	}
	

}
