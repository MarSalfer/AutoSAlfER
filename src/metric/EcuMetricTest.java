/**
 * 
 */
package metric;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;

import javax.xml.bind.JAXBException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import attackerProfile.AttackerProfile;
import systemModel.Authentication;
import systemModel.ECU;
import systemModel.JobIncomingParameter;
import systemModel.JobParam;
import systemModel.ParameterType;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.com.Signal;
import systemModel.fibexImporter.Fibex31toModel;
import systemModel.fibexImporter.Fibex31toModelBigTest;
import systemModel.importer.ZedisXlsx;
import systemModel.importer.ZedisXlsxTest;
import systemModel.subTypes.InputType;

/**
 * @author Martin Salfer
 * 2015-03-01
 *
 */
public class EcuMetricTest {

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
	 * Tests the metric on the case study ECU.
	 */
	@Test
	@Ignore
	public final void MetricCaseStudyTest() {
		Software sw = new Software("CaseStudyECU");
		
		int i = 0;
		for (; i<10 ; i++) {
			sw.addJob(new JobIncomingParameter("JobParam" + i, ParameterType.CHECKED, Authentication.CHALLENGE_RESPONSE));
		}
		for (; i<60 ; i++) {
			sw.addJob(new JobIncomingParameter("JobParam" + i, ParameterType.CHECKED, Authentication.NONE));
		}
		for (; i<61; i++) {
			sw.addJob(new JobIncomingParameter("JobParam" + i, ParameterType.UNCHECKED_COMPLEX, Authentication.CHALLENGE_RESPONSE));
		}
		for (; i<70; i++) {
			sw.addJob(new JobIncomingParameter("JobParam" + i, ParameterType.UNCHECKED_COMPLEX, Authentication.NONE));
		}
		
		i=0;
		for (; i<5 ; i++) {
			sw.registerReadsFrom(new Signal("Signal" + i, ParameterType.CHECKED));
		}
		for (; i<70; i++) {
			sw.registerReadsFrom(new Signal("Signal" + i, ParameterType.UNCHECKED_COMPLEX));
		}
		
		
		
		AttackerProfile a = new AttackerProfile();
		SoftwareAssessment sa = new SoftwareAssessment(sw);
		
		final double meanFindingEffort = sa.getMeanFindingEffort();
//		final double meanExploitEffort = sa.getMeanExploitEffort();
//		final double compromiseableProbability = sa.getCompromiseableProbability();
		
		System.out.println("ECU Software Assessment Statistics for case study:");
		System.out.println("meanFindingEffort= " + meanFindingEffort);
		
		org.junit.Assert.assertEquals(123d, meanFindingEffort, 1d); // TODO compute value disimilarly.
//		org.junit.Assert.assertEquals(123d, meanExploitEffort, 1d); // TODO compute value disimilarly.
//		org.junit.Assert.assertEquals(123d, compromiseableProbability, 1d); // TODO compute value disimilarly.
	}

	
	
	
	
	/**
	 * Tests the metric on the AAG ECU.
	 */
	@Test
	public final void MetricAagTest() {
		String aagName = "AAG";
		
		SystemModel sm = new SystemModel();
		Fibex31toModel fibexImporter = new Fibex31toModel();

		try {
			fibexImporter.importSystemModelFromFibex(new String[]{Fibex31toModelBigTest.TEST_FILES_35UP_AAG[0]}, sm, true, true);
		} catch (FileNotFoundException fe) {
			System.out.println("A file was not found");
		} catch (JAXBException je) {
			System.out.println("Error with JAXB");
			System.out
					.println("Possible reason: One of the files isn't a FIBEX file");
		} catch (Exception e) {
			System.out.println("Unknown Exception: ");
			e.printStackTrace();
		}

		Software aag = sm.getSoftwareWithName(aagName);
		
		assertNotNull(fibexImporter);
		assertNotNull(sm);
		assertNotNull(sm.nodes);
		assertNotNull(fibexImporter.getComMediaMap());
		assertNotNull(fibexImporter.getExclusiveComMediaMap());
		assertEquals("Unexpected Number of incoming signals.", 63, aag.getComNodesReadingFrom().size());
		assertEquals("Unexpected Number of sending signals.", 43, aag.getComNodesWritingOnto().size());
		
		
		try {
			InputStream inp = new FileInputStream(ZedisXlsxTest.ZEDIS_FILE_35up);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			ZedisXlsx.parseJobParametersForV1(wb, sm);
	//		assertTrue(ecu.getJobsNumberDiagnosis() == 26);
	//		assertTrue(ecu.getJobsNumberStandard() == 77);
	//		assertTrue(ecu.getJobsNumberDeveloper() == 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		assertEquals("Number of job parameters", 27, aag.getAttackSurfaceJobParams().size());

		
		
		JobParam energiesparmode = null;
		for (JobParam j : aag.getAttackSurfaceJobParams()) {
			if (j.name.equals("ENERGIESPARMODE--ARG_MODE")) {
				energiesparmode = j;
				break;
			}
		}
		assertNotNull(energiesparmode);
		assertEquals("Complexity Type Mismatch", InputType.COMPLEX, energiesparmode.type);
		assertEquals("Auth Level Mismatch", 0, energiesparmode.authLevel);

		
		
		AttackerProfile a = new AttackerProfile();
		SoftwareAssessment sa = new SoftwareAssessment(aag);
		
		final double meanFindingEffort = sa.getMeanFindingEffort();
		final double meanExploitEffort = sa.getMeanExploitEffort();
////		final double compromiseableProbability = sa.getCompromiseableProbability(); // TODO.
//		
		System.out.println("ECU Software Assessment Statistics for 35up-AAG:");
		System.out.println("meanFindingEffort= " + meanFindingEffort);
		System.out.println("meanExploitEffort= " + meanExploitEffort);
//		System.out.println("vulLikelihood= " + meanFindingEffort); // TODO.
		
		assertEquals(3_455d, meanFindingEffort, 1d); // TODO compute value disimilarly.
		assertEquals(2_802d, meanExploitEffort, 1d); // TODO compute value disimilarly.
////		org.junit.Assert.assertEquals(123d, compromiseableProbability, 1d); // TODO compute value disimilarly.
	}

	
	
	
	
	/**
	 * Tests the metric on all 35up ECUs.
	 */
	@Test
	public final void Metric35upTest() {
		SystemModel sm = new SystemModel();
		Fibex31toModel fibexImporter = new Fibex31toModel();

		try {
			fibexImporter.importSystemModelFromFibex(Fibex31toModelBigTest.TEST_FILES_35UP, sm, true, true);
		} catch (FileNotFoundException fe) {
			System.out.println("A file was not found");
		} catch (JAXBException je) {
			System.out.println("Error with JAXB");
			System.out
					.println("Possible reason: One of the files isn't a FIBEX file");
		} catch (Exception e) {
			System.out.println("Unknown Exception: ");
			e.printStackTrace();
		}


		Software aag = sm.getSoftwareWithName("AAG");

		assertNotNull(fibexImporter);
		assertNotNull(sm);
		assertNotNull(sm.nodes);
		assertNotNull(fibexImporter.getComMediaMap());
		assertNotNull(fibexImporter.getExclusiveComMediaMap());
		assertEquals("Unexpected Number of incoming signals.", 64, aag.getComNodesReadingFrom().size());
		assertEquals("Unexpected Number of sending signals.", 29, aag.getComNodesWritingOnto().size());
		
		
		try {
			InputStream inp = new FileInputStream(ZedisXlsxTest.ZEDIS_FILE_35up);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			ZedisXlsx.parseJobParametersForV1(wb, sm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals("Number of job parameters", 27, aag.getAttackSurfaceJobParams().size());
		JobParam energiesparmode = null;
		for (JobParam j : aag.getAttackSurfaceJobParams()) {
			if (j.name.equals("ENERGIESPARMODE--ARG_MODE")) {
				energiesparmode = j;
				break;
			}
		}
		assertNotNull(energiesparmode);
		assertEquals("Complexity Type Mismatch", InputType.COMPLEX, energiesparmode.type);
		assertEquals("Auth Level Mismatch", 0, energiesparmode.authLevel);

		
		
		AttackerProfile a = new AttackerProfile();

		
		System.out.println("ECU Software Assessment Statistics for 35up:");
		System.out.println("name \tmeanFindingEffort \tmeanExploitEffort");
		for (Software sw : sm.getSoftwareSet()) {
			SoftwareAssessment sa = new SoftwareAssessment(sw);
			System.out.println(sw.getName() + "\t" + sa.getMeanFindingEffort() + 
					"\t" + sa.getMeanExploitEffort());
		}
	}
	
	
	
	
	/**
	 * Tests the metric on the BDC ECU.
	 */
	@Test
	@Ignore
	public final void MetricBdcTest() {
		final String bdcSoftwareName = "BDC2015";

		SystemModel sm = new SystemModel();
		Fibex31toModel fibexImporter = new Fibex31toModel(); 

		try {
			fibexImporter.importSystemModelFromFibex(new String[]{Fibex31toModelBigTest.TEST_FILES_35UP[3]}, sm, true, true); // Only the first element is enough.
		} catch (FileNotFoundException fe) {
			System.out.println("A file was not found");
		} catch (JAXBException je) {
			System.out.println("Error with JAXB");
			System.out
					.println("Possible reason: One of the files isn't a FIBEX file");
		} catch (Exception e) {
			System.out.println("Unknown Exception: ");
			e.printStackTrace();
		}
		
		assertNotNull(fibexImporter);
		assertNotNull(sm);
		assertNotNull(sm.nodes);
		assertNotNull(fibexImporter.getComMediaMap());
		assertNotNull(fibexImporter.getExclusiveComMediaMap());
		

		// TODO import ZEDIS! for jobs parameters.
		
//		ECU bdc = sm.getEcuByName("BDC2015"); // TODO ECU sollte nutzbar sein, aber der FIBEX-Importer baut noch keine HW-Zwischenschicht.
		Software bdc = sm.getSoftwareWithName(bdcSoftwareName);
		
		assertNotNull(bdc);
		
		AttackerProfile a = new AttackerProfile();
		SoftwareAssessment sa = new SoftwareAssessment(bdc);
		
		final double meanFindingEffort = sa.getMeanFindingEffort();
//		final double meanExploitEffort = sa.getMeanExploitEffort();
//		final double compromiseableProbability = sa.getCompromiseableProbability();
		
		System.out.println("ECU Software Assessment Statistics for BDC:");
		System.out.println("meanFindingEffort= " + meanFindingEffort);
		
		org.junit.Assert.assertEquals(123d, meanFindingEffort, 1d); // TODO compute value disimilarly.
//		org.junit.Assert.assertEquals(123d, meanExploitEffort, 1d); // TODO compute value disimilarly.
//		org.junit.Assert.assertEquals(123d, compromiseableProbability, 1d); // TODO compute value disimilarly.
		
		
	}

	
	
	
	
}
