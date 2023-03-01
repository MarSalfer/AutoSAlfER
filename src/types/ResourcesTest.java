/**
 * 
 */
package types;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Salfer
 *
 */
public class ResourcesTest {

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
	 * Test method for {@link types.Resources#multiplyWith(double)}.
	 */
	@Test
	public final void testMultiplyWith() {
		Resources a = new Resources(10,10);
		
		/* Test Expected Value */
		assertEquals(100, a.multiplyWith(10).getMoneyExpectedValueFloat(), 0.1f);
		assertEquals(10, a.multiplyWith(10).multiplyWith(0.1f).getMoneyExpectedValueFloat(), 0.1f);
		
		/* Test Deviation */
		assertEquals(10, a.multiplyWith(10).multiplyWith(0.1f).getMoneyStandardDeviationFloat(), 0.1f);
		assertEquals(31.622776601683793319988935444327, a.multiplyWith(10).getMoneyStandardDeviationFloat(), 0.1f);
	}

	/**
	 * Test method for {@link types.Resources#addWith(types.Resources)}.
	 */
	@Test
	public final void testAddWith() {
		Resources a = new Resources(10, 20);
		Resources b = new Resources(10, 20);
		Resources c = a.addWith(b);
	
		assertEquals(20, c.getMoneyExpectedValueFloat(), 0.1);
		assertEquals(28.28, c.getMoneyStandardDeviationFloat(), 0.1);
	}

	/**
	 * Test method for {@link types.Resources#addWith(types.Resources)}.
	 */
	@Test
	public final void testAddWithCommutativeProperty() {
		Resources a = new Resources(10, 1);
		Resources b = new Resources(10, 5_000);
		Resources c = new Resources(10, 625);
		Resources d = new Resources(10, 100_000);
		
		/* Test for commutative property */
		final Resources sum1 = a.addWith(b).addWith(c).addWith(d);
		final Resources sum2 = d.addWith(c).addWith(b).addWith(a);
		final Resources sum3 = d.addWith(b).addWith(c).addWith(a);
		
		/* Expected Value */
		assertEquals(sum1.getMoneyExpectedValueFloat(), sum2.getMoneyExpectedValueFloat(), 0.1);
		assertEquals(sum1.getMoneyExpectedValueFloat(), sum3.getMoneyExpectedValueFloat(), 0.1);
		
		/* Deviation */
		assertEquals(sum1.getMoneyStandardDeviationFloat(), sum3.getMoneyStandardDeviationFloat(), 0.1);
}
	
	
	
	
	@Test
	public final void testAddUpCollection() {
		List<Resources> resources = new ArrayList<Resources>(4);
		Resources a = new Resources(10, 1);
		Resources b = new Resources(10, 5_000);
		Resources c = new Resources(10, 625);
		Resources d = new Resources(10, 100_000);
		resources.add(a);
		resources.add(b);
		resources.add(c);
		resources.add(d);
		
		final Resources sumA = Resources.addUpCollection(resources);
		
		/* Expected Value */
		assertEquals(40, sumA.getMoneyExpectedValueFloat(), 0.1);
		
		/* Deviation */
		assertEquals(100_126.8726466576, sumA.getMoneyStandardDeviationFloat(), 0.1); // (1^2+5_000^2+625^2 + 100_000^2) = 100_126,8726466576
	}
	
	
	
	/**
	 * Test method for {@link types.Resources#getMoneyExpectedValueFloat()}.
	 */
	@Test
	public final void testGetMoneyExpectedValue() {
		Resources a = new Resources(10,10);
		assertEquals(10, a.getMoneyExpectedValueFloat(),0.1f);
	}
	
	@Test
	public final void testSubstractBy() {
		Resources a = new Resources(10d,10d);
		Resources b = new Resources(10d,10d);
		Resources diff = a.substractBy(b);

		assertEquals("Diff exp. value", 0, diff.getMoneyExpectedValueFloat(), 0.1d);
		assertEquals("Diff std. dev.", 14.14, diff.getMoneyStandardDeviationFloat(), 0.1d);
	}


}
