/**
 * 
 */
package modelEvaluation;

import static org.junit.Assert.*;

import org.junit.Test;

import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import attackerProfile.Skill;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.ECU;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;

/**
 * @author Martin Salfer <martin.salfer@elitenetzwerk.de>
 * @created 22.09.2019 10:43:24
 *
 */
public class SecurityFeaturesTest {
	private Asset credentialsForEtollRegular;
	private Asset credentialsForEtollEnhanced;
	private CommunicationMedium canRegular;
	private CommunicationMedium bluetoothRegular;
	private CommunicationMedium usbToHeadUnitRegular;
	private CommunicationMedium canEnhanced;
	private CommunicationMedium bluetoothEnhanced;
	private CommunicationMedium usbToHeadUnitEnhanced;

	/**
	 * Create a regular system model, without new security features.
	 * 
	 * @return The regular system model, without new security features.
	 */
	private SystemModel createRegularSystemModel() {
		/*
		 * New System Model without extra security feature.
		 */
		SystemModel systemModel = new SystemModel("Reference Model");

		/*
		 * Create ECUs and add to System Model.
		 */
		ECU engineEcu = new ECU("Engine ECU");
		ECU ptc1 = new ECU("Random Power Train ECU 1");
		ECU ptc2 = new ECU("Random Power Train ECU 2");
		ECU ptc3 = new ECU("Random Power Train ECU 3");
		ECU csc = new ECU("Chassis Safety Controler");
		ECU cu = new ECU("Communication Unit");
		ECU headUnit = new ECU("Head Unit");
		systemModel.addNode(headUnit);
		systemModel.addNode(engineEcu);
		systemModel.addNode(ptc1);
		systemModel.addNode(ptc2);
		systemModel.addNode(ptc3);
		systemModel.addNode(csc);
		systemModel.addNode(cu);

		/*
		 * Create Software.
		 */
		Software engineControl = new Software("Engine Control");
		Software ptc1Sw = new Software("PTC1 SW");
		Software ptc2Sw = new Software("PTC2 SW");
		Software ptc3Sw = new Software("PTC3 SW");
		Software browser = new Software("Browser");
		Software paymentService = new Software("Payment Service");
		Software headUnitKernel = new Software("Head-Unit Kernel");
		Software brakeControl = new Software("Break Control");
		Software etollControl = new Software("electronic toll control");

		/*
		 * Attach Software to ECUs.
		 */
		engineEcu.addSoftware(engineControl);
		ptc1.addSoftware(ptc1Sw);
		ptc2.addSoftware(ptc2Sw);
		ptc3.addSoftware(ptc3Sw);
		headUnit.addSoftware(browser);
		headUnit.addSoftware(paymentService);
		headUnit.addSoftware(headUnitKernel);
		csc.addSoftware(brakeControl);
		cu.addSoftware(etollControl);

		/*
		 * Create Assets.
		 */
		credentialsForEtollRegular = new Asset("eToll Credentials", etollControl);

		/*
		 * Create the on-board networks.
		 */
		canRegular = new CommunicationMedium("CAN");
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		CommunicationMedium ptcBackbone = new CommunicationMedium("Powertrain backbone");
		bluetoothRegular = new CommunicationMedium("Bluetooth");
		usbToHeadUnitRegular = new CommunicationMedium("USB to Head-Unit");

		/*
		 * Network the Nodes.
		 */
		ptc1Sw.registerReadsWritesOn(ptcBackbone);
		ptc1Sw.registerReadsWritesOn(canRegular);
		ptc2Sw.registerReadsWritesOn(ptcBackbone);
		ptc2Sw.registerReadsWritesOn(canRegular);
		ptc3Sw.registerReadsWritesOn(ptcBackbone);
		ptc3Sw.registerReadsWritesOn(canRegular);
		engineControl.registerReadsWritesOn(ptcBackbone);
		engineControl.registerReadsWritesOn(canRegular);
		brakeControl.registerReadsWritesOn(canRegular);
		etollControl.registerReadsWritesOn(canRegular);
		browser.registerReadsWritesOn(dBus);
		paymentService.registerReadsWritesOn(dBus);
		headUnitKernel.registerReadsWritesOn(canRegular);
		headUnitKernel.registerReadsWritesOn(bluetoothRegular);
		headUnitKernel.registerReadsWritesOn(usbToHeadUnitRegular);

		return systemModel;
	}

	
	/**
	 * Test the creation of two different system models for a model-based feature evaluation.
	 * One model is a regular system model, the other is a system model that incorporates
	 * an HSM (Hardware Security Module).
	 */
	@Test
	public final void securityFeaturesModelEvaluationTest() {
		SystemModel systemModelRegular  = createRegularSystemModel();
		SystemModel systemModelEnhanced = createEnhancedSystemModel();
		
		/*
		 * Attacker Profile for regular system model.
		 */
		AttackerProfile apRegular = new AttackerProfile("Remote Attacker", 1, new Resources(100_000d, 10_000d));
		apRegular.desires(credentialsForEtollRegular, new Resources(10_000d,5_000d));
		apRegular.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		apRegular.addSkill(new Skill("Browser Proficiency"), 0.8f);
		apRegular.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", canRegular, 5_000, new Resources(50d,50d));
		apRegular.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", canRegular, 1_000, new Resources(200d,100d));
		apRegular.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetoothRegular, 1_000, new Resources(200d,100d));
		apRegular.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnitRegular, 1_000, new Resources(200d,100d));
		apRegular.addAccess(accessToUsb);
		
		/*
		 * Attacker Profile for enhanced system model
		 */
		AttackerProfile apEnhanced = new AttackerProfile("Remote Attacker", 1, new Resources(100_000d, 10_000d));
		apEnhanced.desires(credentialsForEtollEnhanced, new Resources(10_000d,5_000d));
		apEnhanced.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		apEnhanced.addSkill(new Skill("Browser Proficiency"), 0.8f);
		apEnhanced.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCanEnhanced = new Access("Access onto CAN over CU", canEnhanced, 5_000, new Resources(50d,50d));
		apEnhanced.addAccess(accessOverCuToCanEnhanced);
		final Access accessToObdEnhanced = new Access("OBD Adapter", canEnhanced, 1_000, new Resources(200d,100d));
		apEnhanced.addAccess(accessToObdEnhanced);
		final Access accessToBluetoothEnhanced = new Access("Bluetooth Adapter", bluetoothEnhanced, 1_000, new Resources(200d,100d));
		apEnhanced.addAccess(accessToBluetoothEnhanced);
		final Access accessToUsbEnhanced = new Access("USB Adapter", usbToHeadUnitEnhanced, 1_000, new Resources(200d,100d));
		apEnhanced.addAccess(accessToUsbEnhanced);
		
		assertNotEquals("System models are equal!", systemModelEnhanced, systemModelRegular);
		assertNotEquals("Attacker profiles are equal!", apRegular, apEnhanced);
		assertNotEquals("Credentials Assets are equal", credentialsForEtollRegular, credentialsForEtollEnhanced);
	}

	/**
	 * Create an enhanced system model, including a new security features.
	 * Here an HSM is keeping the e-tolling credentials separate from the CU.
	 * The CU has a direct connection to the HSM for using the e-tolling credentials.
	 * 
	 * @return The system model including a new feature.
	 */
	private SystemModel createEnhancedSystemModel() {
		/*
		 * New System Model with extra security feature. Here an HSM ECU.
		 */
		SystemModel systemModel = new SystemModel("Enhanced Model including a new security feature");
		
		/*
		 * Create ECUs and add to System Model.
		 */
		ECU engineEcu = new ECU("Engine ECU");
		ECU ptc1 = new ECU("Random Power Train ECU 1");
		ECU ptc2 = new ECU("Random Power Train ECU 2");
		ECU ptc3 = new ECU("Random Power Train ECU 3");
		ECU csc = new ECU("Chassis Safety Controler");
		ECU cu = new ECU("Communication Unit");
		ECU headUnit = new ECU("Head Unit");
		ECU hsm = new ECU("Hardware Security Module for CU");
		systemModel.addNode(headUnit);
		systemModel.addNode(engineEcu);
		systemModel.addNode(ptc1);
		systemModel.addNode(ptc2);
		systemModel.addNode(ptc3);
		systemModel.addNode(csc);
		systemModel.addNode(cu);
		systemModel.addNode(hsm);

		/*
		 * Create Software.
		 */
		Software engineControl = new Software("Engine Control");
		Software ptc1Sw = new Software("PTC1 SW");
		Software ptc2Sw = new Software("PTC2 SW");
		Software ptc3Sw = new Software("PTC3 SW");
		Software browser = new Software("Browser");
		Software paymentService = new Software("Payment Service");
		Software headUnitKernel = new Software("Head-Unit Kernel");
		Software brakeControl = new Software("Break Control");
		Software etollControl = new Software("electronic toll control");
		Software hsmControl = new Software("HSM Software");

		/*
		 * Attach Software to ECUs.
		 */
		engineEcu.addSoftware(engineControl);
		ptc1.addSoftware(ptc1Sw);
		ptc2.addSoftware(ptc2Sw);
		ptc3.addSoftware(ptc3Sw);
		headUnit.addSoftware(browser);
		headUnit.addSoftware(paymentService);
		headUnit.addSoftware(headUnitKernel);
		csc.addSoftware(brakeControl);
		cu.addSoftware(etollControl);
		hsm.addSoftware(hsmControl);

		/*
		 * Create Assets.
		 */
		credentialsForEtollEnhanced = new Asset("eToll Credentials", hsmControl);

		/*
		 * Create the on-board networks.
		 */
		canEnhanced = new CommunicationMedium("CAN");
		CommunicationMedium dBus = new CommunicationMedium("dBus");
		CommunicationMedium ptcBackbone = new CommunicationMedium("Powertrain backbone");
		bluetoothEnhanced = new CommunicationMedium("Bluetooth");
		usbToHeadUnitEnhanced = new CommunicationMedium("USB to Head-Unit");
		CommunicationMedium hsmConnection = new CommunicationMedium("HSM connection with CU");

		/*
		 * Network the Nodes.
		 */
		ptc1Sw.registerReadsWritesOn(ptcBackbone);
		ptc1Sw.registerReadsWritesOn(canEnhanced);
		ptc2Sw.registerReadsWritesOn(ptcBackbone);
		ptc2Sw.registerReadsWritesOn(canEnhanced);
		ptc3Sw.registerReadsWritesOn(ptcBackbone);
		ptc3Sw.registerReadsWritesOn(canEnhanced);
		engineControl.registerReadsWritesOn(ptcBackbone);
		engineControl.registerReadsWritesOn(canEnhanced);
		brakeControl.registerReadsWritesOn(canEnhanced);
		etollControl.registerReadsWritesOn(canEnhanced);
		browser.registerReadsWritesOn(dBus);
		paymentService.registerReadsWritesOn(dBus);
		headUnitKernel.registerReadsWritesOn(canEnhanced);
		headUnitKernel.registerReadsWritesOn(bluetoothEnhanced);
		headUnitKernel.registerReadsWritesOn(usbToHeadUnitEnhanced);
		hsmControl.registerReadsWritesOn(hsmConnection);
		etollControl.registerReadsWritesOn(hsmConnection);

		return systemModel;
	}
}
