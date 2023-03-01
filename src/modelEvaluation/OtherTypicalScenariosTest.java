/**
 * 
 */
package modelEvaluation;

import static org.junit.Assert.*;

import org.junit.Before;
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
 * @created 22.09.2019 14:06:19
 *
 */
public class OtherTypicalScenariosTest {

	private SystemModel systemModel;
	private ECU engineEcu;
	private ECU ptc1;
	private ECU ptc2;
	private ECU ptc3;
	private ECU csc;
	private ECU cu;
	private ECU headUnit;
	private Software engineControl;
	private Software ptc1Sw;
	private Software ptc2Sw;
	private Software ptc3Sw;
	private Software brakeControl;
	private Software etollControl;
	private Software browser;
	private Software paymentService;
	private Software headUnitKernel;
	private Software immobilizerControl;
	private Software mileageSw;
	private Asset engineStart;
	private Asset immobilizerCredentials;
//	private Asset ptc1a;
//	private Asset ptc2a;
//	private Asset ptc3a;
//	private Asset web20Credentials;
//	private Asset inAppPaymentReputation;
//	private Asset inAppPaymentFinancial;
	private Asset brakeReliability;
//	private Asset credentialsForEtoll;
//	private Asset credentialsForBankAccount;
//	private Asset credentialsForCreditCard;
	private Asset headUnitKernelControl;
	private Asset headUnitPaidContent;
	private Asset cuControl;
	private Asset microphoneSurveillance;
	private Asset mileageCounter;
	private CommunicationMedium can;
	private CommunicationMedium dBus;
	private CommunicationMedium ptcBackbone;
	private CommunicationMedium bluetooth;
	private CommunicationMedium usbToHeadUnit;
	
	/**
	 * Set-up method before the entire class, constructing a uniform system model.
	 */
	@Before
	public void setUp() throws Exception {
		/*
		 * New System Model
		 */
		systemModel = new SystemModel("EVITA Attack Scenarios System Model");
		
		/*
		 * Create ECUs and add to System Model.
		 */
		engineEcu = new ECU("Engine ECU");
		ptc1 = new ECU("Random Power Train ECU 1");
		ptc2 = new ECU("Random Power Train ECU 2");
		ptc3 = new ECU("Random Power Train ECU 3");
		csc = new ECU("Chassis Safety Controler");
		cu = new ECU("Communication Unit");
		headUnit = new ECU("Head Unit");
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
		engineControl = new Software("Engine Control");
		ptc1Sw = new Software("PTC1 SW");
		ptc2Sw = new Software("PTC2 SW");
		ptc3Sw = new Software("PTC3 SW");
		browser = new Software("Browser");
		paymentService = new Software("Payment Service");
		headUnitKernel = new Software("Head-Unit Kernel");
		brakeControl = new Software("Break Control");
		etollControl = new Software("electronic toll control");
		immobilizerControl = new Software("Immobilizer");
		mileageSw = new Software("Mileage counter");

		/*
		 * Attach Software to ECUs.
		 */
		engineEcu.addSoftware(engineControl);
		engineEcu.addSoftware(immobilizerControl);
		ptc1.addSoftware(ptc1Sw);
		ptc2.addSoftware(ptc2Sw);
		ptc3.addSoftware(ptc3Sw);
		headUnit.addSoftware(browser);
		headUnit.addSoftware(paymentService);
		headUnit.addSoftware(headUnitKernel);
		csc.addSoftware(brakeControl);
		csc.addSoftware(mileageSw);
		cu.addSoftware(etollControl);
		
		/*
		 * Create Assets.
		 */
		engineStart = new Asset("Engine Start Reliability", engineControl);
//		ptc1a = new Asset("Power Train ECU 1 Reliability", ptc1Sw);
//		ptc2a = new Asset("Power Train ECU 2 Reliability", ptc2Sw);
//		ptc3a = new Asset("Power Train ECU 3 Reliability", ptc3Sw);
//		web20Credentials = new Asset("Web 2.0 Credentials Financial", browser);
//		inAppPaymentReputation = new Asset("In-App Payment Reputation Attractor", paymentService);
//		inAppPaymentFinancial = new Asset("In-App Payment Financial Attractor", paymentService);
		brakeReliability = new Asset("Break Reliability", brakeControl);
//		credentialsForEtoll = new Asset("eToll Credentials", etollControl);
//		credentialsForCreditCard = new Asset("Bank Account Credentials", etollControl);
//		credentialsForBankAccount = new Asset("Credit Card Credentials", etollControl);
		cuControl = new Asset("Control over Communication Unit", etollControl);
		headUnitKernelControl = new Asset("Control over the Head-Unit kernel", headUnitKernel);
		immobilizerCredentials = new Asset("Immobilizer Credentials", immobilizerControl);
		microphoneSurveillance = new Asset("Microphone Eavesdropping", headUnitKernel);
		headUnitPaidContent = new Asset("Paid Content on the Head Unit", headUnitKernel);
		mileageCounter = new Asset("Mileage Counter", mileageSw);
		
		/*
		 * Create the on-board networks.
		 */
		can = new CommunicationMedium("CAN");
		dBus = new CommunicationMedium("dBus");
		ptcBackbone = new CommunicationMedium("Powertrain backbone");
		bluetooth = new CommunicationMedium("Bluetooth");
		usbToHeadUnit = new CommunicationMedium("USB to Head-Unit");
		
		/*
		 * Network the Nodes.
		 */
		ptc1Sw.registerReadsWritesOn(ptcBackbone);
		ptc1Sw.registerReadsWritesOn(can);
		ptc2Sw.registerReadsWritesOn(ptcBackbone);
		ptc2Sw.registerReadsWritesOn(can);
		ptc3Sw.registerReadsWritesOn(ptcBackbone);
		ptc3Sw.registerReadsWritesOn(can);
		engineControl.registerReadsWritesOn(ptcBackbone);
		engineControl.registerReadsWritesOn(can);
		immobilizerControl.registerReadsWritesOn(can);
		immobilizerControl.registerReadsWritesOn(ptcBackbone);
		brakeControl.registerReadsWritesOn(can);
		mileageSw.registerReadsWritesOn(can);
		mileageSw.registerReadsWritesOn(ptcBackbone);
		etollControl.registerReadsWritesOn(can);
		browser.registerReadsWritesOn(dBus);
		paymentService.registerReadsWritesOn(dBus);
		headUnitKernel.registerReadsWritesOn(can);
		headUnitKernel.registerReadsWritesOn(bluetooth);
		headUnitKernel.registerReadsWritesOn(usbToHeadUnit);
	}
	
	
	@Test
	public final void vehicleTheftTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(immobilizerCredentials, new Resources(50_000d,5_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("Immobilizer Credentials missing in immobilizer software.", immobilizerControl.getAttractors().contains(immobilizerCredentials));
	}

	@Test
	public final void failureOfCriticalFunctionsTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(brakeReliability, new Resources(1_000_000d,5_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("Break reliability missing in CSC.", brakeControl.getAttractors().contains(brakeReliability));
	}

	@Test
	public final void reconnaissanceAndSurveillanceTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(microphoneSurveillance, new Resources(50_000d,5_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("Microphone eavesdropping Asset missing in Head-Unit.", headUnitKernel.getAttractors().contains(microphoneSurveillance));
	}

	@Test
	public final void serviceContentStealingTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(headUnitPaidContent, new Resources(1_000d,5_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("Paid content Asset missing in Head-Unit.", headUnitKernel.getAttractors().contains(headUnitPaidContent));
	}

	@Test
	public final void denialOfServiceTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(engineStart, new Resources(1_000d,5_000d));
		ap.desires(brakeReliability, new Resources(1_000d,5_000d));
		ap.desires(headUnitKernelControl, new Resources(1_000d,5_000d));
		ap.desires(headUnitPaidContent, new Resources(1_000d,5_000d));
		ap.desires(cuControl, new Resources(1_000d,5_000d));
		ap.desires(microphoneSurveillance, new Resources(1_000d,5_000d));
		ap.desires(mileageCounter, new Resources(1_000d,5_000d));
		
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("DoS-able Asset missing.", headUnitKernel.getAttractors().contains(headUnitPaidContent));
		assertTrue("DoS-able Asset missing.", engineControl.getAttractors().contains(engineStart));
		assertTrue("DoS-able Asset missing.", brakeControl.getAttractors().contains(brakeReliability));
		assertTrue("DoS-able Asset missing.", headUnitKernel.getAttractors().contains(headUnitKernelControl));
		assertTrue("DoS-able Asset missing.", etollControl.getAttractors().contains(cuControl));
		assertTrue("DoS-able Asset missing.", headUnitKernel.getAttractors().contains(microphoneSurveillance));
		assertTrue("DoS-able Asset missing.", mileageSw.getAttractors().contains(mileageCounter));
	}

	@Test
	public final void odometerManipulationTest() {
		// The system model is set up with the implicit calling of setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Attacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(mileageCounter, new Resources(3_000d,1_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		ap.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		ap.addAccess(accessToUsb);
		
		/*
		 * Test Assertions
		 */
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", ap.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", ap.getAccesses().contains(accessToUsb));
		assertTrue("Mileage counter Asset missing in CSC.", mileageSw.getAttractors().contains(mileageCounter));
	}

}
