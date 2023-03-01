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
 * @created 20.09.2019 17:35:04
 *
 */
public class EvitaAttackScenarios {

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
	private Asset engineStart;
	private Asset ptc1a;
	private Asset ptc2a;
	private Asset ptc3a;
	private Asset web20Credentials;
	private Asset inAppPaymentReputation;
	private Asset inAppPaymentFinancial;
	private Asset brakeReliability;
	private Asset credentialsForEtoll;
	private Asset credentialsForBankAccount;
	private Asset credentialsForCreditCard;
	private Asset headUnitKernelControl;
	private Asset cuControl;
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
		engineStart = new Asset("Engine Start Reliability", engineControl);
		ptc1a = new Asset("Power Train ECU 1 Reliability", ptc1Sw);
		ptc2a = new Asset("Power Train ECU 2 Reliability", ptc2Sw);
		ptc3a = new Asset("Power Train ECU 3 Reliability", ptc3Sw);
		web20Credentials = new Asset("Web 2.0 Credentials Financial", browser);
		inAppPaymentReputation = new Asset("In-App Payment Reputation Attractor", paymentService);
		inAppPaymentFinancial = new Asset("In-App Payment Financial Attractor", paymentService);
		brakeReliability = new Asset("Break Reliability", brakeControl);
		credentialsForEtoll = new Asset("eToll Credentials", etollControl);
		credentialsForCreditCard = new Asset("Bank Account Credentials", etollControl);
		credentialsForBankAccount = new Asset("Credit Card Credentials", etollControl);
		cuControl = new Asset("Control over Communication Unit", etollControl);
		headUnitKernelControl = new Asset("Control over the Head-Unit kernel", headUnitKernel);
		
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
		brakeControl.registerReadsWritesOn(can);
		etollControl.registerReadsWritesOn(can);
		browser.registerReadsWritesOn(dBus);
		paymentService.registerReadsWritesOn(dBus);
		headUnitKernel.registerReadsWritesOn(can);
		headUnitKernel.registerReadsWritesOn(bluetooth);
		headUnitKernel.registerReadsWritesOn(usbToHeadUnit);
	}
	

	/**
	 * EVITA Attack Scenario B.3.7: Engine DoS-Attack (Engine Refuse to Start)
	 */
	@Test
	public final void b3_7_EngineDosAttackTest() {
		// The System Model is generated in setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Hacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(engineStart, new Resources(90_000d, 0d));
		ap.desires(ptc1a, new Resources(30_000d, 0d));
		ap.desires(ptc2a, new Resources(30_000d, 0d));
		ap.desires(ptc3a, new Resources(30_000d, 0d));
		ap.desires(web20Credentials, new Resources(10_000d, 0d));
		ap.desires(inAppPaymentFinancial, new Resources(20_000d, 0d));
		ap.desires(inAppPaymentReputation, new Resources(0d, 0d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		ap.addAccess(new Access("Phishing Website", browser, 5_000_000, new Resources(50d,50d)));
		final Access obdAccess = new Access("OBD Adapter", can, 1_000_000, new Resources(200d,100d));
		ap.addAccess(obdAccess);
		
		/*
		 * JUnit Assertion Tests
		 */
		assertNotNull(systemModel);
		assertNotNull(ap);
		assertNotNull(engineControl);
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(obdAccess));
		assertTrue("Powertrain Backbone not available", engineControl.getComNodesWritingOnto().contains(ptcBackbone));
		assertTrue("Engine asset not in engine control software.", engineControl.getAttractors().contains(engineStart));
	}

	/**
	 * EVITA Attack Scenario B.3.8: Unauthorized Brake
	 */
	@Test
	public final void b3_8_UnauthorizedBrakeTest() {
		// The System Model is generated in setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Hacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(engineStart, new Resources(90_000d, 0d));
		ap.desires(ptc1a, new Resources(30_000d, 0d));
		ap.desires(ptc2a, new Resources(30_000d, 0d));
		ap.desires(ptc3a, new Resources(30_000d, 0d));
		ap.desires(web20Credentials, new Resources(10_000d, 0d));
		ap.desires(inAppPaymentFinancial, new Resources(20_000d, 0d));
		ap.desires(inAppPaymentReputation, new Resources(0d, 0d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		ap.addAccess(new Access("Phishing Website", browser, 5_000_000, new Resources(50d,50d)));
		final Access obdAccess = new Access("OBD Adapter", can, 1_000_000, new Resources(200d,100d));
		ap.addAccess(obdAccess);

		/*
		 * JUnit Assertion Tests
		 */
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(obdAccess));
		assertTrue("Powertrain Backbone not available", engineControl.getComNodesWritingOnto().contains(ptcBackbone));
		assertTrue("Break asset not in CSC software.", brakeControl.getAttractors().contains(brakeReliability));
	}

	/**
	 * EVITA Attack Scenario B.3.10: Attacking E-Toll
	 */
	@Test
	public final void b3_10_AttackingETollTest() {
		// The System Model is generated in setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Hacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(credentialsForEtoll, new Resources(100d,10d));
		ap.desires(credentialsForBankAccount, new Resources(1_000d,500d));
		ap.desires(credentialsForCreditCard, new Resources(500d,50d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		ap.addAccess(new Access("Phishing Website", browser, 5_000_000, new Resources(50d,50d)));
		final Access accessToCu = new Access("CU Proximity", cu, 1, new Resources(50d,50d));
		ap.addAccess(accessToCu);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);

		/*
		 * JUnit Assertion Tests
		 */
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("CU access not in Attacker Profile", ap.getAccesses().contains(accessToCu));
		assertTrue("Bank account credentials not in CU software.", etollControl.getAttractors().contains(credentialsForBankAccount));
		assertTrue("Credit card credentials not in CU software.", etollControl.getAttractors().contains(credentialsForCreditCard));
		assertTrue("etoll credentials not in CU software.", etollControl.getAttractors().contains(credentialsForEtoll));
	}

	/**
	 * EVITA Attack Scenario B.4.1: Flashing per OBD
	 */
	@Test
	public final void b4_1_FlashingPerObdTest() {
		// The System Model is generated in setUp().
		
		/*
		 * Attacker Profile
		 */
		AttackerProfile ap = new AttackerProfile("Hacker", 1, new Resources(100_000d, 10_000d));
		ap.desires(cuControl, new Resources(10_000d,5_000d));
		ap.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		ap.addSkill(new Skill("Browser Proficiency"), 0.8f);
		ap.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access internetAccessToCu = new Access("Internet Access", cu, 5_000_000, new Resources(50d,50d));
		ap.addAccess(internetAccessToCu);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000_000, new Resources(200d,100d));
		ap.addAccess(accessToObd);

		/*
		 * JUnit Assertion Tests
		 */
		assertTrue("OBD access not in Attacker Profile", ap.getAccesses().contains(accessToObd));
		assertTrue("CU Internet access not in Attacker Profile", ap.getAccesses().contains(internetAccessToCu));
		assertTrue("Bank account credentials not in CU software.", etollControl.getAttractors().contains(cuControl));
	}

	/**
	 * EVITA Attack Scenario B.4.2: Head Unit Attack
	 */
	@Test
	public final void b4_2_HeadUnitAttackTest() {
		// The System Model is generated in setUp().
		
		/*
		 * Attacker Profile for remote attacks
		 */
		AttackerProfile apRemote = new AttackerProfile("Remote Attacker", 1, new Resources(100_000d, 10_000d));
		apRemote.desires(headUnitKernelControl, new Resources(10_000d,5_000d));
		apRemote.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		apRemote.addSkill(new Skill("Browser Proficiency"), 0.8f);
		apRemote.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		final Access accessOverCuToCan = new Access("Access onto CAN over CU", can, 5_000, new Resources(50d,50d));
		apRemote.addAccess(accessOverCuToCan);

		/*
		 * Attacker Profile for local attacks
		 */
		AttackerProfile apLocal = new AttackerProfile("Remote Attacker", 1, new Resources(100_000d, 10_000d));
		apLocal.desires(headUnitKernelControl, new Resources(10_000d,5_000d));
		apLocal.addSkill(new Skill("dBus Payment Proficiency"), 0.2f);
		apLocal.addSkill(new Skill("Browser Proficiency"), 0.8f);
		apLocal.addSkill(new Skill("ECU Firmware Reversing"), 1.5f);
		apLocal.addAccess(accessOverCuToCan);
		final Access accessToObd = new Access("OBD Adapter", can, 1_000, new Resources(200d,100d));
		apLocal.addAccess(accessToObd);
		final Access accessToBluetooth = new Access("Bluetooth Adapter", bluetooth, 1_000, new Resources(200d,100d));
		apLocal.addAccess(accessToBluetooth);
		final Access accessToUsb = new Access("USB Adapter", usbToHeadUnit, 1_000, new Resources(200d,100d));
		apLocal.addAccess(accessToUsb);

		/*
		 * JUnit Assertion Tests
		 */
		assertTrue("CU Internet access not in Attacker Profile Remote", apRemote.getAccesses().contains(accessOverCuToCan));
		assertTrue("CU Internet access not in Attacker Profile Local", apLocal.getAccesses().contains(accessOverCuToCan));
		assertTrue("OBD access not in Attacker Profile", apLocal.getAccesses().contains(accessToObd));
		assertTrue("Bluetooth access not in Attacker Profile", apLocal.getAccesses().contains(accessToBluetooth));
		assertTrue("USB access not in Attacker Profile", apLocal.getAccesses().contains(accessToUsb));
		assertTrue("Head-Unit control not in Head-Unit.", headUnitKernel.getAttractors().contains(headUnitKernelControl));
		assertNotNull("Head-Unit control not in Attacker Profile Local.", apLocal.getValueforAttractor(headUnitKernelControl));
		assertNotNull("Head-Unit control not in Attacker Profile Remote.", apRemote.getValueforAttractor(headUnitKernelControl));
	}

}
