/**
 * 
 */
package analysis.plotWriter;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


/**
 * @author Martin Salfer
 * @created 08.04.2017 19:51:20
 *
 */
public class PlotDatWriterTest {

	/**
	 * Test method for {@link analysis.plotWriter.PlotDatWriter#PlotDatWriter(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public final void testPlotDatWriter() {
		// Optionen.
		final String outputFileName = "output/test/PlotDataWriter.dat";

		// Pre-Writer Assertions
		new File(outputFileName).delete();
		assertFalse(new File(outputFileName).exists());
		assertEquals(0, new File(outputFileName).length());

		// Execute
		PlotDatWriter p = new PlotDatWriter("Testdaten", outputFileName, "nodes", "msRuntime");
		p.writeData("10", "1");
		p.writeData("20", "3");
		p.writeData("30", "5");
		p.writeData("40", "8");
		p.writeData("50", "13");
		p.writeData("60", "19");
		p.close();
		
		// Post-Writer Assertions.
		assertTrue(new File(outputFileName).exists());
		assertTrue(Math.abs(126 - new File(outputFileName).length()) <= 10); // 10 Byte difference only allowed.
	}

}
