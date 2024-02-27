package systemModel.statistics;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import systemModel.SystemModel;
import systemModel.fibexImporter.Fibex31toModel;
import systemModel.fibexImporter.Fibex31toModelBigTest;

public class SummaryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public final void testZedis() {
		SystemModel sysModel = new SystemModel();
		systemModel.importer.ZedisXlsx.parseZedisIntoSysModel(sysModel);

		// Jobs: Given Types and necessary authentication key.
		System.out.println(Summary.giveJobsTypeAuthSummary(sysModel));
		

	}
	
	@Test
	public final void testFibex() {
		SystemModel mySystem = new SystemModel();
		Fibex31toModel systemImporter = new Fibex31toModel();

		try {
			systemImporter.importSystemModelFromFibex(Fibex31toModelBigTest.TEST_FILES_35UP, mySystem, true, true);
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
		
		assertNotNull(systemImporter);
		assertNotNull(mySystem);
		assertNotNull(systemImporter.getComMediaMap());
		assertNotNull(systemImporter.getExclusiveComMediaMap());

		System.out.println(Summary.incomingSignalsPerECU(mySystem));

		System.out.println(Summary.signalsTotalNumber(mySystem));
	}
	
	
}
