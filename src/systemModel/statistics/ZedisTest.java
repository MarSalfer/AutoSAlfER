package systemModel.statistics;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import systemModel.SystemModel;

public class ZedisTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		System.out.print("Running ZEDIS Test...");
		SystemModel sysModel = new SystemModel();
		systemModel.importer.ZedisXlsx.parseZedisIntoSysModel(sysModel);
		
		// DTCs: je Steuergerät (p+s), Durchschnitt, Median, oberes, unteres Quartil. TODO
		System.out.println(Zedis.giveDtcSummaryPerEcu(sysModel));
		
		// Jobs: je Steuergerät (p+s), Durchschnitt, Median, oberes, unteres Quartil. TODO
		System.out.println(Zedis.giveJobsSummaryPerEcu(sysModel));

		// DTC+Jobs Summary.
		System.out.println(Zedis.giveJobsDTCsSummaryPerEcu(sysModel));

		// Test for RowNumber

		System.out.println("done");
	}
	
	
}
