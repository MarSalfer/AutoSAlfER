package systemModel.fibexImporter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.junit.Test;

import systemModel.CommunicationMedium;
import systemModel.Node;
import systemModel.Software;
import systemModel.SystemModel;

public class Fibex31toModelBigTest {

	SystemModel mySystem = null;
	Fibex31toModel systemImporter = null;
	public static final String[] TEST_FILES_35UP = {
			"input/fibex/Vehicle_A_FlexRay_V42_PWF_lokal.xml",
			"input/fibex/Vehicle_Body2-CAN_V16_PWF_fern.xml",
			"input/fibex/Vehicle_Body-CAN_V35_PWF_fern.xml",
			"input/fibex/Vehicle_CAS-CAN_V6_PWF_fern.xml",
			"input/fibex/Vehicle_D_CAN_V28_PWF_fern.xml",
			"input/fibex/Vehicle_FA-CAN_V33_PWF_fern.xml",
			"input/fibex/Vehicle_FASL-CAN_V6_PWF_fern.xml",
			"input/fibex/Vehicle_FASR-CAN_V6_PWF_fern.xml",
			"input/fibex/Vehicle_IuK-CAN_V12_PWF_fern.xml",
			"input/fibex/Vehicle_LE-CAN_V9_PWF_fern.xml",
			"input/fibex/Vehicle_LP-CAN_V9_PWF_fern.xml",
			"input/fibex/Vehicle_PS-CAN_V8_PWF_fern.xml",
			"input/fibex/Vehicle_USS-CAN_V8_PWF_fern.xml",
			"input/fibex/Vehicle_ZSG-CAN_V22_PWF_fern.xml"
			};

	/**
	 * Sole AAG ECU for fast loading and simple tests.
	 */
	public static final String[] TEST_FILES_35UP_AAG = {
		"input/fibex/Vehicle_Body-CAN_V36_AAG_V22_PWF_solitaer.xml"
	};

	@Test
	@Ignore
	public void testIfSystemWasCreatedSuccessfully() {
		systemImporter = new Fibex31toModel();
		mySystem = new SystemModel();
		try {
			systemImporter.importSystemModelFromFibex(TEST_FILES_35UP, mySystem, true, true);
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
		assertNotNull(mySystem.nodes);
		assertNotNull(systemImporter.getComMediaMap());
		assertNotNull(systemImporter.getExclusiveComMediaMap());

		// check if ECU DCS was imported correctly

		Software dcs = null;
		for (Node n : mySystem.nodes) {
			if (n.getName().contains("DCS")) {
				dcs = (Software) n;
			}
		}
		

		// add Outputs
		Set<String> outputs = new HashSet<String>();
		outputs.add(new String("NM3_BS_PRTNT_FACAN"));
		outputs.add(new String("NM3_FKTN_PRTNT_FACAN"));
		outputs.add(new String("NM3_SND_ECU_IDENT_FACAN"));
		outputs.add(new String("NM3_CTR_BIT_VEC_FACAN"));
		outputs.add(new String("Bus: DCS_FA_CAN"));

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
		Set<String> requiredInputs = new HashSet<String>();
		requiredInputs.add(new String("ST_CON_VEH"));
		requiredInputs.add(new String("NM3_BS_PRTNT_FACAN"));
		requiredInputs.add(new String("CTR_ECU_CHAS"));
		requiredInputs.add(new String("NM3_FKTN_PRTNT_FACAN"));
		requiredInputs.add(new String("CTR_FKTN_PRTNT"));
		requiredInputs.add(new String("ALIV_CON_VEH"));
		requiredInputs.add(new String("NM3_SND_ECU_IDENT_FACAN"));
		requiredInputs.add(new String("NM3_CTR_BIT_VEC_FACAN"));
		requiredInputs.add(new String("CTR_BS_PRTNT"));
		requiredInputs.add(new String("CRC_CON_VEH"));
		requiredInputs.add(new String("Bus: DCS_FA_CAN"));

		boolean inputsCorrect = true;
		for (CommunicationMedium cm : dcs.getComNodesReadingFrom()) {
			if (!requiredInputs.contains(cm.getName())) {
				inputsCorrect = false;
			}
		}
		assertTrue(inputsCorrect);
		assertTrue(dcs.getComNodesReadingFrom().size() == requiredInputs.size());

	}

}
