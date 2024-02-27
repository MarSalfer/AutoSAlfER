package systemModel.fibexImporter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import supplement.ExportExclusiveSignalsToDatabase;
import systemModel.CommunicationMedium;
import systemModel.Node;
import systemModel.Software;
import systemModel.SystemModel;

public class Fibex31toModelTest {

	static SystemModel mySystem = null;
	static Fibex31toModel systemImporter = null;
	
	@BeforeClass
	public static void setUp() {
		systemImporter = new Fibex31toModel();
		mySystem = new SystemModel();
		String[] testFiles = {
				"input/fibex/Vehicle_D_CAN_V28_PWF_fern.xml",
				"input/fibex/Vehicle_FA-CAN_V33_PWF_fern.xml"
				};
		try {
			systemImporter.importSystemModelFromFibex(testFiles, mySystem, true, true);
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
	}

	@Test
	public void testIfSystemWasCreatedSuccessfully() {
		assertNotNull(systemImporter);
		assertNotNull(mySystem);
		assertNotNull(mySystem.nodes);
		assertNotNull(systemImporter.getComMediaMap());
		assertNotNull(systemImporter.getExclusiveComMediaMap());
	}

	
	@Test
	public void testIfFibexWasImportedCorrectly() {
		// check if ECU DCS was imported correctly

		Software dcs = null;
		for (Node n : mySystem.nodes) {
			if (n.getName().contains("DCS")) {
				dcs = (Software) n;
			}
		}

		// print the test ecu
		for (Node n : mySystem.nodes) {
			Software s = (Software) n;
			if (s.toString().contains("DCS")) {
				System.out.println(s + "\n");
				System.out.println("Outputs: ");
				for (CommunicationMedium cm : s.getWriteableComMedia()) {
					System.out.println(cm.toString());
				}
				System.out.println("\nInputs: ");
				for (CommunicationMedium cm : s.getComNodesReadingFrom()) {
					System.out.println(cm.toString());
				}

				System.out
						.println("------------------------------------------------------");

			}

		}

		// add Outputs
		Set<String> outputs = new HashSet<String>();
		outputs.add(new String("NM3_BS_PRTNT_FACAN"));
		outputs.add(new String("NM3_FKTN_PRTNT_FACAN"));
		outputs.add(new String("NM3_SND_ECU_IDENT_FACAN"));
		outputs.add(new String("NM3_CTR_BIT_VEC_FACAN"));
		outputs.add(new String("FA_CAN"));

		boolean outputsCorrect = true;
		boolean outputsNumberCorrect = dcs.getComNodesWritingOnto().size() == outputs
				.size();

		for (CommunicationMedium cm : dcs.getComNodesWritingOnto()) {
			if (!outputs.contains(cm.getName())) {
				outputsCorrect = false;
			}
		}

		assertTrue(outputsCorrect);
		assertTrue(outputsNumberCorrect);

		// add Inputs
		Set<String> inputs = new HashSet<String>();
		inputs.add(new String("ST_CON_VEH"));
		inputs.add(new String("NM3_BS_PRTNT_FACAN"));
		inputs.add(new String("CTR_ECU_CHAS"));
		inputs.add(new String("NM3_FKTN_PRTNT_FACAN"));
		inputs.add(new String("CTR_FKTN_PRTNT"));
		inputs.add(new String("ALIV_CON_VEH"));
		inputs.add(new String("NM3_SND_ECU_IDENT_FACAN"));
		inputs.add(new String("NM3_CTR_BIT_VEC_FACAN"));
		inputs.add(new String("CTR_BS_PRTNT"));
		inputs.add(new String("CRC_CON_VEH"));
		inputs.add(new String("FA_CAN"));

		boolean inputsCorrect = true;
		boolean inputsNumberCorrect = dcs.getComNodesReadingFrom().size() == inputs
				.size();

		for (CommunicationMedium cm : dcs.getComNodesReadingFrom()) {
			if (!inputs.contains(cm.getName())) {
				inputsCorrect = false;
			}
		}

		assertTrue(inputsCorrect);
		assertTrue(inputsNumberCorrect);

	}


	
	
	/**
	 */
	@Ignore
	@Test
	public void testDatabaseCreation() {

		System.out.println("Total number of signals"
				+ systemImporter.getComMediaMap().size());

		Map<String, CommunicationMedium> exclusiveSignals = systemImporter
				.getExclusiveComMediaMap();

		System.out.println("Number of exclusive Signals: "
				+ exclusiveSignals.size());

		for (CommunicationMedium cm : exclusiveSignals.values()) {
			System.out.println(cm.toString2());
		}

		ExportExclusiveSignalsToDatabase dbExporter = new ExportExclusiveSignalsToDatabase();
		dbExporter.createTableInDatabase("jdbc:sqlite:test.db",
				"EXCLUSIVE_SIGNALS");
		try {
			dbExporter.fillTableWithEntries(exclusiveSignals, "jdbc:sqlite:test.db",
					"EXCLUSIVE_SIGNALS");
		} catch (Exception e) {
			System.out.println("Map must only contain exclusive signals");
		}
		dbExporter.printTable("jdbc:sqlite:test.db", "EXCLUSIVE_SIGNALS");

		assertNotNull(mySystem);
		assertFalse(mySystem.nodes.isEmpty());

	}
}
