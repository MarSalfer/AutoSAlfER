package systemModel.importer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import systemModel.ECU;
import systemModel.SystemModel;

public class ZedisXlsxTest {

	static final String[] zedisFiles35up = new String[] {
			"input/zedis/Vehicle_AAG-01.xlsx", 
			"input/zedis/Vehicle_ACSM-05.xlsx", 
			"input/zedis/Vehicle_AMP-TOPHB01.xlsx", 
			"input/zedis/Vehicle_AMP-TOPHB02.xlsx", 
			"input/zedis/Vehicle_AMP-TOPHB03.xlsx", 
			"input/zedis/Vehicle_ASD-01.xlsx", 
			"input/zedis/Vehicle_ATM-01.xlsx", 
			"input/zedis/Vehicle_BDC-CT02.xlsx", 
			"input/zedis/Vehicle_DME-840.xlsx", 
			"input/zedis/Vehicle_DME-860.xlsx", 
			"input/zedis/Vehicle_DME-880 (Slave).xlsx", 
			"input/zedis/Vehicle_DME-880.xlsx", 
			"input/zedis/Vehicle_DME-8XX, DME-8XXH.xlsx", 
			"input/zedis/Vehicle_DME-DDE802 DME-DDE832.xlsx", 
			"input/zedis/Vehicle_DME-DDE832.xlsx", 
			"input/zedis/Vehicle_DSC-EBC460.xlsx", 
			"input/zedis/Vehicle_EGS-ZFB-CC.xlsx", 
			"input/zedis/Vehicle_EMARS-01-HA.xlsx", 
			"input/zedis/Vehicle_EMARS-01-VA.xlsx", 
			"input/zedis/Vehicle_EPS-ZF02.xlsx", 
			"input/zedis/Vehicle_FLA-03.xlsx", 
			"input/zedis/Vehicle_FLM-01 (links).xlsx", 
			"input/zedis/Vehicle_FLM-01 (rechts).xlsx", 
			"input/zedis/Vehicle_FRR-02.xlsx", 
			"input/zedis/Vehicle_FZD-35.xlsx", 
			"input/zedis/Vehicle_GWS-G04M.xlsx", 
			"input/zedis/Vehicle_HKA-PR01.xlsx", 
			"input/zedis/Vehicle_HKFM_HKL-CT01.xlsx", 
			"input/zedis/Vehicle_HSR-02ZF.xlsx", 
			"input/zedis/Vehicle_HU-NBTEVO.xlsx", 
			"input/zedis/Vehicle_ICAM2_SVS-BO01.xlsx", 
			"input/zedis/Vehicle_ICAM_RFK_VA01.xlsx", 
			"input/zedis/Vehicle_IC_HIGH-BO.xlsx", 
			"input/zedis/Vehicle_IC_MID-JCI.xlsx", 
			"input/zedis/Vehicle_IHKA-PR01.xlsx", 
			"input/zedis/Vehicle_KAFAS-CT03.xlsx", 
			"input/zedis/Vehicle_LEM-01.xlsx", 
			"input/zedis/Vehicle_LMV-02.xlsx", 
			"input/zedis/Vehicle_NIVI-03FIR.xlsx", 
			"input/zedis/Vehicle_PCU500-BUM (G11).xlsx", 
			"input/zedis/Vehicle_RS-01.xlsx", 
			"input/zedis/Vehicle_RSE-NBTEVO.xlsx", 
			"input/zedis/Vehicle_SAS-10.xlsx", 
			"input/zedis/Vehicle_SCR-01.xlsx", 
			"input/zedis/Vehicle_SM-04-BF.xlsx", 
			"input/zedis/Vehicle_SM-04-BFH.xlsx", 
			"input/zedis/Vehicle_SM-04-FA.xlsx", 
			"input/zedis/Vehicle_SM-04-FAH.xlsx", 
			"input/zedis/Vehicle_SPNM-01-HL.xlsx", 
			"input/zedis/Vehicle_SPNM-01-HR.xlsx", 
			"input/zedis/Vehicle_SPNM-01-VL.xlsx", 
			"input/zedis/Vehicle_SPNM-01-VR.xlsx", 
			"input/zedis/Vehicle_TV-MODUL2.xlsx", 
			"input/zedis/Vehicle_TVM2-T2.xlsx", 
			"input/zedis/Vehicle_USS-VA03.xlsx", 
			"input/zedis/Vehicle_VDP-01.xlsx", 
			"input/zedis/Vehicle_ZBE-06TC, ZBE-06HI.xlsx", 
			"input/zedis/Vehicle_ZGW-01 ZGW-02.xlsx", 
			"input/zedis/Vehicle_ZUSA-FUNKT-SG.xlsx"
	};

	
	/**
	 *	ZEDIS-custom file for V1 development test. 
	 */
	public static final String ZEDIS_FILE_35up = "input/zedis/Vehicle-V1-Extrakt.xlsx";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public final void test() {
		fail("Not yet implemented"); // TODO
	}
	@Test
	@Ignore
	public final void parseTestSmall() {
//		String xlsxFileName = "input/zedis/Vehicle.xlsx";  // 2GB heap is not enough for this file.
		iterateXlsx(zedisFiles35up[0]);
		System.out.println("Done");
	}

	@Test
	public final void EcuNameTest() throws IOException {
		InputStream inp = new FileInputStream(zedisFiles35up[0]);
		XSSFWorkbook wb = new XSSFWorkbook(inp);
		String ecuName = ZedisXlsx.extractEcuName(wb);
		assertTrue("AAG-01".equals(ecuName));
	}
	
	
	@Test
	public final void collectDtcs() throws IOException {
		InputStream inp = new FileInputStream(zedisFiles35up[0]);
		XSSFWorkbook wb = new XSSFWorkbook(inp);
		ECU ecu = new ECU();
		ZedisXlsx.parseDtcs(wb, ecu);
		
		assertTrue(ecu.getDtcNumberPrimary() == 113);
		assertTrue(ecu.getDtcNumberSecondary() == 4);
//		AAG, lastrow: 115, firstRow: 3; number: 113;
	}
	
	
	@Test
	public final void collectJobs() throws IOException {
		InputStream inp = new FileInputStream(zedisFiles35up[0]);
		XSSFWorkbook wb = new XSSFWorkbook(inp);
		ECU ecu = new ECU();
		ZedisXlsx.parseJobs(wb, ecu);
		
		assertTrue(ecu.getJobsNumberDiagnosis() == 26);
		assertTrue(ecu.getJobsNumberStandard() == 77);
		assertTrue(ecu.getJobsNumberDeveloper() == 0);
	}
	
	/**
	 * @param xlsxFileName
	 */
	private void iterateXlsx(String xlsxFileName) {
		InputStream inp;
		try {
			inp = new FileInputStream(xlsxFileName);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			XSSFSheet sheet = wb.getSheetAt(0);
			inp.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public final void parseTestBig() {
for (String xlsxFileName : zedisFiles35up) {
			iterateXlsx(xlsxFileName);
		}
		System.out.println("Done");
	}
	
	
}
