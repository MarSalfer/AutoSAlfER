package analysis.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import metric.SoftwareAssessment;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import exploit.Exploit;
import exploit.PotentialExploit;
import systemModel.Asset;
import systemModel.JobParam;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.fibexImporter.Fibex31toModel;
import systemModel.fibexImporter.Fibex31toModelBigTest;
import systemModel.importer.ZedisXlsx;
import systemModel.importer.ZedisXlsxTest;
import systemModel.subTypes.InputType;
import types.Resources;
import analysis.Crawler;
import attackGraph.AttackGraph;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import attackerProfile.Skill;

public class V1GraphTest {

	public static String V1_REPORT_FILE = "output/reports/AttackRiskReport-JUnit-V1.xml";
	
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the metric on all 35up ECUs.
	 */
	@Test
	public final void graphFor35upTest() {
		SystemModel sysModel = new SystemModel();
		Fibex31toModel fibexImporter = new Fibex31toModel();

		System.out.print("Parsing FIBEX...");
		try {
//			fibexImporter.importSystemModelFromFibex(new String[] {Fibex31toModelBigTest.TEST_FILES_35UP[8]}, sysModel);
			fibexImporter.importSystemModelFromFibex(Fibex31toModelBigTest.TEST_FILES_35UP, sysModel, true, true);
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
		System.out.println("done");

		Software aag = sysModel.getSoftwareWithName("AAG");

		assertNotNull(fibexImporter);
		assertNotNull(sysModel);
		assertNotNull(fibexImporter.getComMediaMap());
		assertNotNull(fibexImporter.getExclusiveComMediaMap());
		assertEquals("Unexpected Number of incoming signals.", 64, aag.getComNodesReadingFrom().size());
		assertEquals("Unexpected Number of sending signals.", 29, aag.getComNodesWritingOnto().size());
		
		
		System.out.print("Parsing ZEDIS...");
		try {
			InputStream inp = new FileInputStream(ZedisXlsxTest.ZEDIS_FILE_35up);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			ZedisXlsx.parseJobParametersForV1(wb, sysModel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done");
		
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

		Software dsc = sysModel.getSoftwareWithName("DSC_Modul");
		Software bdc = sysModel.getSoftwareWithName("BDC2015");
		Software dme = sysModel.getSoftwareWithName("DME1");
		Software acsm = sysModel.getSoftwareWithName("ACSM");
		Software hu = sysModel.getSoftwareWithName("NBTevo");
		Software nivi = sysModel.getSoftwareWithName("NiVi");
		Software atm = sysModel.getSoftwareWithName("ATM");
//		Software diagnoseTool = sysModel.getSoftwareWithName("Diagnosetool_FA_CAN");
		assertNotNull(dsc);
		assertNotNull(bdc);
		assertNotNull(dme);
		assertNotNull(acsm);
		assertNotNull(hu);
		assertNotNull(nivi);
		assertNotNull(atm);
		
		
		
		
		
		/* Assets */
		Set<Asset> assets = new HashSet<Asset>();
		assets.add(new Asset("Car life module access", bdc, new Resources(50_000d, 10_000d)));
		assets.add(new Asset("Car DSC Safety", dsc, new Resources(1_000_000, 1_000_000d)));
		assets.add(new Asset("Car EMF Safety", dme, new Resources(1_000_000, 1_000_000d)));
		assets.add(new Asset("ACSM Asset", acsm, new Resources(1_000_000, 1_000_000d)));
		assets.add(new Asset("Car EMF Safety", nivi, new Resources(1_000_000, 1_000_000d)));
		assets.add(new Asset("nivi asset", nivi, new Resources(1_000_000, 1_000_000d)));
		for (Asset a : assets) {
			sysModel.addNode(a);
		}



		/* Attacker Profile */
		final AttackerProfile attackerProfile = new AttackerProfile("V1 Attacker", 5, new Resources(100_000d, 1_000d));
		attackerProfile.addAccess(new Access("ATM", atm, 1_000, new Resources(10, 1d)));
		for (Asset a : assets) {
			attackerProfile.desires(a, new Resources(1_000d, 100d));
		}
		Skill skillBrowser = new Skill("Browser");
		Skill skillDbus = new Skill("dBus");
		attackerProfile.addSkill(skillBrowser, 0.8f);
		attackerProfile.addSkill(skillDbus, 0.2f);

		
		
		/* Exploit Database */
		final HashSet<Exploit> exploits = new HashSet<Exploit>();
		for (Software s : sysModel.getSoftwareSet()) {
			SoftwareAssessment sa = new SoftwareAssessment(s);
			exploits.add(new PotentialExploit(s.getName()+"Exploit", 
					s, 
					new Resources(sa.getMeanExploitEffort()+sa.getMeanFindingEffort(), 100d), 
					new Resources(1_000, 0d)));
		}
		for (Asset a : assets) {
			exploits.add(new PotentialExploit("asset exploit", a, new Resources(), new Resources(500, 50d)));
		}


		/* Execution */
		System.out.print("Executing Crawler...");
		AttackScenario scenario = new AttackScenario("Salfer V1 Graph on 35up", sysModel, attackerProfile, exploits);
		Crawler c = new Crawler(scenario);
		AttackGraph attackGraph = c.crawlAndGetAttackForest();
		System.out.println("done");

		/* Report */
		final String report = Report.analyzeAndGenerateReport(attackGraph, V1_REPORT_FILE);
		System.out.println("V1 Attack Graph:");
		System.out.println(attackGraph);
		System.out.println(report);
		Assert.assertTrue("Report Start", report.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<AttackRiskReport date="));
		for (String content : new String[] {"<attackGraphReport", "<budget>", "<attackerProfile", "<accessSet>",
				"<access", "<attackSteps>", "<exploit name=", "<cost>", "<gain>", "<profit>", "<roi>", 
				"<probAffordability>", "<probProfit>", "<probAttack>", "<probAttack>"}) {
			Assert.assertTrue("Report content is missing " + content, report.contains(content));
		}
		Assert.assertTrue("Report End", report.endsWith("</expectedDamage>\n    </attackGraphReport>\n</AttackRiskReport>\n"));
		
	}
	
	
}
