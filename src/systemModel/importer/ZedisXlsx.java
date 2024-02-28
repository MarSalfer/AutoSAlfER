/**
 * 
 */
package systemModel.importer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import systemModel.ECU;
import systemModel.JobParam;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.subTypes.InputType;

/**
 * @author Martin Salfer
 * 2014-12-08
 *
 */
public class ZedisXlsx {

	private static final int HEADER_ROWS = 3;  // first three rows are only title rows.
	private static final String AUTH_LEVEL_STRING = "Auth. Level"; // Title name for authentication leve.
	private static final int HEADER_ROW_INDEX = 2; // Index of Header Row

	
	
	/*
	 * V1 Graph Inclusion definitions.
	 */
	private static final int V1_EXTRACT_HEADER_ROW_INDEX = 0;
	private static final String ECU_NAME_HEADER_TITLE = "SG";
	private static enum V1_ZEDIS {ECU, JOBNAME, PARNAME, TYPE, AUTH_LEVEL};
	private static final Map<V1_ZEDIS,Integer> V1_ZEDIS_COLS = new HashMap<V1_ZEDIS,Integer>() {{
		put(V1_ZEDIS.ECU, 0);
		put(V1_ZEDIS.JOBNAME, 1);
		put(V1_ZEDIS.PARNAME, 3);
		put(V1_ZEDIS.TYPE, 8);
		put(V1_ZEDIS.AUTH_LEVEL, 14);
	}};
	
	public static final Map<String,String> V1_ECU_NAME_ZEDIS_2_FIBEX = Collections.unmodifiableMap(
		new HashMap<String,String>() {{
		put("AAG-01", "AAG");
		
		put("ACSM-05","ACSM");
//		put("AMP-TOPHB01","");
//		put("AMP-TOPHB02","");
//		put("AMP-TOPHB03","");
		put("ASD-01","ASD");
		put("ATM-01","ATM");
		put("BDC-CT02","BDC2015");
		put("DME-840","DME1");
		put("DME-860","DME1");
		put("DME-880 (Slave)","DME2");
		put("DME-880","DME1");
		put("DME-8XX, DME-8XXH","DME1");
		put("DME-DDE802 DME-DDE832","DME1");
		put("DME-DDE832","DME1");
		put("DSC-EBC460","DSC_Modul");
		put("EGS-ZFB-CC","EGS_EL");
		put("EMARS-01-HA","emARS_h");
		put("EMARS-01-VA","emARS_v");
		put("EPS-ZF02","EPS");
		put("FLA-03","FLA");
		put("FLM-01 (links)","FLM_L");
		put("FLM-01 (rechts)","FLM_R");
//		put("FRR-02","");
		put("FZD-35","FZD");
		put("GWS-G04M","GWS");
		put("HKA-PR01","HKA");
		put("HKFM_HKL-CT01","HKFM");
		put("HSR-02ZF","HSR");
		put("HU-NBTEVO","NBTevo");
		put("ICAM2_SVS-BO01","iCAM2");
		put("ICAM_RFK_VA01","iCAM_RFK");
//		put("IC_HIGH-BO","");
//		put("IC_MID-JCI","");
		put("IHKA-PR01","IHKA");
//		put("KAFAS-CT03","");
		put("LEM-01","LEM01");
		put("LMV-02","LMV");
		put("NIVI-03FIR","NiVi");
		put("PCU500-BUM (G11)","PCU500");
		put("RS-01","RSL");
		put("RSE-NBTEVO","ZBE_FOND");
		put("SAS-10","SAS");
		put("SCR-01","SCR");
		put("SM-04-BF","SM_BF");
		put("SM-04-BFH","SM_BFH");
		put("SM-04-FA","SM_FA");
		put("SM-04-FAH","SM_FAH");
		put("SPNM-01-HL","SPnM_HL");
		put("SPNM-01-HR","SPnM_HR");
		put("SPNM-01-VL","SPnM_VL");
		put("SPNM-01-VR","SPnM_VR");
//		put("TV-MODUL2","");
//		put("TVM2-T2","");
		put("USS-VA03","USS");
//		put("VDP-01","");
		put("ZBE-06TC, ZBE-06HI","ZBE");
		put("ZGW-01 ZGW-02","ZGW");
//		put("ZUSA-FUNKT-SG","");
	}});
	public static final Set<String> V1_UNKNOWN_NAMES = new HashSet<String>();
	
	
//	private static final Map<String, int[]> myMap = Collections.unmodifiableMap(
//		    new HashMap<String, int[]>() {{
//		        put(FRED_TEXT, new int[] {ONE, TWO, FOUR});
//		        put(DAVE_TEXT, new int[] {TWO, THREE});
//		    }});
	
	
	public ZedisXlsx() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Reads a ZEDIS XLSX file and parse security relevant meta data into an ECU object.
	 * @param filename - a ZEDIS XLSX file that contains the data of one single ECU.
	 * @return a System Model ECU with relevant data included.
	 */
	public static void parseZedisIntoSysModel(SystemModel sysModel, String filename) {
		InputStream inp;
		try {
			inp = new FileInputStream(filename);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			
			// Determine name and select from sys model, otherwise create an own one.
			String ecuName = extractEcuName(wb);
			ECU ecu = sysModel.getEcuByName(ecuName);
			if (ecu == null) {
				ecu = new ECU(ecuName);
				sysModel.addNode(ecu);
			}
			
			// Parse DTC and jobs data from the ZEDIS XLSX.
			parseDtcs(wb, ecu);
			parseJobs(wb, ecu); // 3,4,5 zzgl. 9.
//			
//			
//			
//			
//			XSSFSheet sheet = wb.getSheetAt(0);
//			java.util.Iterator<Row> rowIterator = sheet.iterator();
//			// Following Iterator code is from a POI tutorial.
//			
//			while (rowIterator.hasNext()) 
//			{
//				Row row = rowIterator.next();
//				//For each row, iterate through all the columns
//				Iterator<Cell> cellIterator = row.cellIterator();
//				
//				while (cellIterator.hasNext()) 
//				{
//					Cell cell = cellIterator.next();
//					//Check the cell type and format accordingly
//					switch (cell.getCellType()) 
//					{
//					case Cell.CELL_TYPE_NUMERIC:
//						System.out.print(cell.getNumericCellValue() + "t");
//						break;
//					case Cell.CELL_TYPE_STRING:
//						System.out.print(cell.getStringCellValue() + "t");
//						break;
//					}
//				}
//				System.out.println("");
//			}
			inp.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Parse data about primary and secondary DTCs into the ECU.
	 * @param ecu The ECU to parse data into.
	 * @param wb 
	 */
	static void parseDtcs(XSSFWorkbook wb, ECU ecu) {
		XSSFSheet s;
		s = wb.getSheet("1 Primärfehler");
		ecu.setDtcNumberPrimary(s.getLastRowNum() - 2);  // The first three rows are header, i.e. row 0, 1 and 2.
		
		s = wb.getSheet("2 Sekundärfehler");
		ecu.setDtcNumberSecondary(s.getLastRowNum() - 2); // The first three rows are header, i.e. row 0, 1 and 2.
	}

	/**
	 * Parse data about jobs into the ECU.
	 * 
	 * Empty auth level fields are interpreted as a 0.
	 * @param ecu The ECU to parse data into.
	 * @param wb 
	 */
	static void parseJobs(XSSFWorkbook wb, ECU ecu) {
		XSSFSheet s;
		s = wb.getSheet("3 Diagnosejobs");
		ecu.setJobsNumberDiagnosis(s.getLastRowNum() - 2);  // The first three rows are header, i.e. row 0, 1 and 2.
		parseJobAuthenticationLevels(ecu, s);

		s = wb.getSheet("4 Standardjobs");
		ecu.setJobsNumberStandard(s.getLastRowNum() - 2);  // The first three rows are header, i.e. row 0, 1 and 2.
		parseJobAuthenticationLevels(ecu, s);

		s = wb.getSheet("5 Entwicklerjobs");
		ecu.setJobsNumberDeveloper(s.getLastRowNum() - 2);  // The first three rows are header, i.e. row 0, 1 and 2.
		parseJobAuthenticationLevels(ecu, s);
	}

	
	/**
	 * Parsa job parameter data into an Software object.
	 * 
	 * TODO: Refactor to ECU object, after FIBEX-to-ECU refactoring.
	 * @param wb - Workbook with incoming data.
	 * @param sm - SM about to receive the data.
	 */
	public static void parseJobParametersForV1(XSSFWorkbook wb, SystemModel sm) {
		XSSFSheet s;
		s = wb.getSheet("9 ArgErg");

		for (Row r : s) {
			if (r.getRowNum() <= V1_EXTRACT_HEADER_ROW_INDEX)
				continue;
			
			final String swName = r.getCell(V1_ZEDIS_COLS.get(V1_ZEDIS.ECU)).getStringCellValue();
			try {
				Software sw = sm.getSoftwareWithName(translateZedis2Fibex(swName));
				
				if (sw == null) {
					V1_UNKNOWN_NAMES.add(swName);
					continue; // next row. ZEDIS ECU name is unknown in FIBEX nomenclature. 
				}

				String jobParName = r.getCell(V1_ZEDIS_COLS.get(V1_ZEDIS.JOBNAME)).getStringCellValue() + 
						"--" +
						r.getCell(V1_ZEDIS_COLS.get(V1_ZEDIS.PARNAME)).getStringCellValue();
				
				InputType type = InputType.getTypeFor(r.getCell(V1_ZEDIS_COLS.get(V1_ZEDIS.TYPE)).getStringCellValue());

				final int authLevel = (int) r.getCell(V1_ZEDIS_COLS.get(V1_ZEDIS.AUTH_LEVEL)).getNumericCellValue();
				
				JobParam jobParam = new JobParam(jobParName, type, authLevel);
				
				sw.addAttackSurface(jobParam);
				
			} catch (IllegalStateException e) {
				System.out.println("Error in row \"" + r.getRowNum() + "\" with content \"" + swName + "\".");
				throw e;
			}
		}
	}
	
	
	
	/**
	 * Translate an ECU/SW name of ZEDIS data to FIBEX name conventions.
	 * @param stringCellValue
	 * @return
	 */
	public static String translateZedis2Fibex(String stringCellValue) {
		String ret = V1_ECU_NAME_ZEDIS_2_FIBEX.get(stringCellValue);
		if (ret == null) {
			V1_UNKNOWN_NAMES.add(stringCellValue);
		}
		return ret;
	}

	/**
	 * Parse the number of authentication levels of an ECU from ZEDIS.
	 * @param ecu - the ECU under consideration.
	 * @param s - the Worksheet to parse.
	 */
	private static void parseJobAuthenticationLevels(ECU ecu, XSSFSheet s) {
		int authLevelColIndex = -1;
		for (Cell c : s.getRow(HEADER_ROW_INDEX)){
			if (!c.getStringCellValue().equals(AUTH_LEVEL_STRING)) {
				continue;
			} else { // "Auth. Level" found.
				authLevelColIndex = c.getColumnIndex();
				break;
			}
		}
		assert authLevelColIndex != -1;

		for (Row r : s) {
			if (r.getRowNum() < HEADER_ROWS)
				continue;
			
			final String stringCellValue = r.getCell(authLevelColIndex).getStringCellValue();
			try {
				final int authLevel;
				if (stringCellValue.length() == 0)
					authLevel = 0;
				else 
					authLevel = (int) Integer.decode(stringCellValue);
				
				switch (authLevel) {
				case 0:
					ecu.setJobsAL0Inc();
					break;
				case 1:
					ecu.setJobsAL1Inc();
					break;
				case 3:
					ecu.setJobsAL3Inc();
					break;
				case 4:
					ecu.setJobsAL4Inc();
					break;
				case 5:
					ecu.setJobsAL5Inc();
					break;
				default: throw new IllegalArgumentException("AuthLevel cell returned a number not 0, 3, 4, 5, but : \"" + authLevel + "\".");
				}
			} catch (IllegalStateException e) {
				System.out.println("Error in row \"" + r.getRowNum() + "\" with content \"" + stringCellValue + "\".");
				throw e;
			}
		}
	}


	/**
	 * Extracts the ECU name of the ZEDIS export XLSX.
	 * @param wb - a ECU specific XLSX workbook.
	 * @return - The ECU name that this workbook is for.
	 */
	static String extractEcuName(XSSFWorkbook wb) {
		XSSFSheet s = wb.getSheet("0 Allgemein");
		assert s != null;
		
		XSSFRow r = s.getRow(3);
		XSSFCell c = r.getCell(0);
		String  ecuName = c.getStringCellValue(); // Cell with ECU name.
		return ecuName;
	}


	/**
	 * Reads the 35up ZEDIS XLSX file and parse security relevant meta data into a system model.
	 * @param sysmodel - The system model the XLSX data is supposed to be parsed into.
	 * @return a System Model ECU with relevant data included.
	 */
	public static void parseZedisIntoSysModel(SystemModel sysModel) {
		for (String fileName : ZedisXlsxTest.zedisFiles35up) {
			ZedisXlsx.parseZedisIntoSysModel(sysModel, fileName);
		}
	}
	
//	public static void main(String... ags) throws FileNotFoundException {
//		System.out.println("Hi");
//		
//	}
}
