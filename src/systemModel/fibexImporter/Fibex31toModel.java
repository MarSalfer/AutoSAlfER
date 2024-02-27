package systemModel.fibexImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import systemModel.CommunicationMedium;
import systemModel.Software;
import systemModel.SystemModel;
import systemModel.com.Signal;
import systemModel.fibexImporter.xsd31.CONNECTORTYPE;
import systemModel.fibexImporter.xsd31.CONTROLLERTYPE;
import systemModel.fibexImporter.xsd31.ECUPORTTYPE;
import systemModel.fibexImporter.xsd31.ECUTYPE;
import systemModel.fibexImporter.xsd31.ELEMENTS;
import systemModel.fibexImporter.xsd31.FIBEX;
import systemModel.fibexImporter.xsd31.INCLUDEDPDUTYPE;
import systemModel.fibexImporter.xsd31.INCLUDEDSIGNALTYPE;
import systemModel.fibexImporter.xsd31.SIGNALINSTANCETYPE;
//import systemModel.fibexImporter.xsd31.CONNECTORTYPE;
//import systemModel.fibexImporter.xsd31.CONTROLLERTYPE;
//import systemModel.fibexImporter.xsd31.ECUPORTTYPE;
//import systemModel.fibexImporter.xsd31.ECUTYPE;
//import systemModel.fibexImporter.xsd31.ELEMENTS;
//import systemModel.fibexImporter.xsd31.FIBEX;
//import systemModel.fibexImporter.xsd31.INCLUDEDPDUTYPE;
//import systemModel.fibexImporter.xsd31.INCLUDEDSIGNALTYPE;
//import systemModel.fibexImporter.xsd31.SIGNALINSTANCETYPE;
//import systemModel.fibexImporter.xsd31.SIGNALTYPE;
import systemModel.fibexImporter.xsd31.SIGNALTYPE;


/**
 * This class is able to import the specifications of a System Model from a
 * FIBEX file to the Source Code of AutoSAlfER
 */
public class Fibex31toModel {

	/**
	 * Whether or not this class should print to System.out.
	 */
	private static final boolean VERBOSE = true;
	/**
	 * Contains all Signals/Communication Media. Can be filled with the method
	 * importComMediaMap.
	 * 
	 * Key: Signal ID (from FIBEX file) Value: CommunicationMedium
	 */
	private Map<String, CommunicationMedium> comMediaMap = new HashMap<String, CommunicationMedium>();
	private int numberOfImportedSignals = 0;
	private int numberOfImportedSoftware = 0;
	private int numberOfCommunicationMedia = 0;
	
	/**
	 * Whether this importer should add Signals as Communication Nodes.
	 */
	private boolean importSignals;

	/**
	 * Whether this importer should add busses as Communication Nodes.
	 */
	private boolean importBusses;

	/**
	 */
	private HashMap<String, CommunicationMedium> comMediaBusses = new HashMap<String, CommunicationMedium>();
	
	/**
	 * Imports all FIBEX files given in the argument.
	 * 
	 * @param files
	 *            String array of all filenames which should be imported
	 * @param systemModel
	 *            The SystemModel where the system specification should be
	 *            imported to.
	 * @param importBusses Whether to import busses as communication nodes, or ignore them. // MarS
	 * @param importSignals Whether to import signals as communication nodes, or ignore them. // MarS
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */

	// TODO file isnt a FIBEX file -> Exception
	public void importSystemModelFromFibex(String[] files,
			SystemModel systemModel, boolean importBusses, boolean importSignals) throws JAXBException,
			FileNotFoundException {
		if (VERBOSE) System.out.println("Fibex-Import: Start."); // MarS
		this.importBusses = importBusses; // MarS
		this.importSignals = importSignals; // MarS

		Map<String, Software> swMap = new TreeMap<String, Software>();

		// iterates through all files and fills the Software Map
		for (String s : files) {
			if (VERBOSE) System.out.println("Fibex-Import: parsing " + new File(s).getName()); // MarS
			fillSoftwareMap(s, swMap);
		}

		// adds all Softwares previously imported to the SystemModel
		for (Software sw : swMap.values()) {
			systemModel.addNode(sw);
		}
		if (VERBOSE) System.out.println("Fibex-Import: Done."); // MarS

	}

	/**
	 * Imports all signals of the FIBEX file.
	 * 
	 * Watch out: they don't contain the sending and receiving ECUs yet. This
	 * will be done in the method connectComMediaWithSW
	 * 
	 * @param fibexSignalList
	 *            List of Signal types from a FIBEX file
	 * @return Returns a map of all Communication Nodes. The key is the Signal
	 *         ID which is defined in the FIBEX file.
	 */
	private TreeMap<String, CommunicationMedium> importComMediaMap(
			List<SIGNALTYPE> fibexSignalList) {
		TreeMap<String, CommunicationMedium> result = new TreeMap<String, CommunicationMedium>();
		for (SIGNALTYPE s : fibexSignalList) {
			CommunicationMedium cn = new Signal(s.getSHORTNAME());
			numberOfImportedSignals++;
			result.put(s.getID(), cn);
		}

		return result;

	}

	/**
	 * Fills the Software Map given in the arguments with the ECUs from the
	 * FIBEX file.
	 * 
	 * @param fileName
	 *            Filename of the FIBEX document
	 * @param swMap
	 *            Software Map
	 * @throws FileNotFoundException
	 * 
	 * @throws JAXBException
	 * 
	 */

	private void fillSoftwareMap(String fileName, Map<String, Software> swMap)
			throws JAXBException, FileNotFoundException {

		// build fibex doc
		FileInputStream inputStream = new FileInputStream(fileName);
		JAXBContext jc = JAXBContext.newInstance("systemModel.fibexImporter.xsd31");
		Unmarshaller u = jc.createUnmarshaller();
		FIBEX doc = (FIBEX) u.unmarshal(inputStream);
		// get all elements
		ELEMENTS elements = doc.getELEMENTS();

		// fill comMediaMap with all signals
		List<SIGNALTYPE> fibexSignalList = elements.getSIGNALS().getSIGNAL();
		comMediaMap = importComMediaMap(fibexSignalList);

		// import all ECUs and connect them with signals
		List<ECUTYPE> ecuListFromFibex = elements.getECUS().getECU();

		for (ECUTYPE e : ecuListFromFibex) {

			connectComMediaWithSW(e, comMediaMap, swMap);

		}

	}

	/**
	 * Connects the Softwares with its in- and outgoing signals and the bus as
	 * CommunicationMedium
	 * 
	 * @param fibexEcuType
	 *            ECUTYPE from FIBEX
	 * @param comNodesMap
	 *            The previously from FIBEX imported signal Map
	 * @param swMap
	 *            Map containing all Softwares
	 * 
	 */
	private void connectComMediaWithSW(ECUTYPE fibexEcuType,
			Map<String, CommunicationMedium> comMediaMap,
			Map<String, Software> swMap) {

		// adds the software to the swMap if it doesn't already exist
		String swName = fibexEcuType.getSHORTNAME();
		Software sw;
		if (!swMap.containsKey(swName)) {
			sw = new Software(swName);
			numberOfImportedSoftware++;
			swMap.put(swName, sw);
		} else {
			sw = swMap.get(swName);
		}

		if (importBusses) { // MarS
			// add buses as communication nodes (in- and output)
			List<CONTROLLERTYPE> controllers = fibexEcuType.getCONTROLLERS()
					.getCONTROLLER();
			for (CONTROLLERTYPE c : controllers) {
//				String busName= c.getSHORTNAME().substring(c.getSHORTNAME().lastIndexOf("_") + 1); // MarS
				String[] shortName = c.getSHORTNAME().split("_"); // MarS
				String busName= shortName[shortName.length-2] + "_" + shortName[shortName.length-1];// MarS
				
				
				CommunicationMedium bus = getOrCreateComMediaBus(busName); // MarS
				numberOfCommunicationMedia++;
				boolean alreadyInList=false;
				
				for(CommunicationMedium cm: sw.getComNodesReadingFrom()){
					if(cm.getName().equals(busName)){
						alreadyInList=true;
					}
				}
				
				if(!alreadyInList){
					sw.registerReadsWritesOn(bus);
				}
				
				
				
			}
			
		}
		
		if (importSignals) { // MarS
			// connect comNodes (only output) with ECUs
			
			List<CONNECTORTYPE> connectors = fibexEcuType.getCONNECTORS()
					.getCONNECTOR();
			
			// get outputs
			// long way down to the ECUs
			
			// iterating through all connectors (even if there usually should only
			// be one)
			for (CONNECTORTYPE conn : connectors) {
				
				// check if the ECU has outputs. Otherwise the next command will
				// result in a Null Pointer Error
				if (conn.getOUTPUTS() != null) {
					List<ECUPORTTYPE> outputPorts = conn.getOUTPUTS()
							.getOUTPUTPORT();
					
					// iterating through outputports (there can be more than one
					// because theres one port for each channel/bus the ecus is
					// connected to)
					for (ECUPORTTYPE output : outputPorts) {
						List<INCLUDEDPDUTYPE> pdus = output.getINCLUDEDPDUS()
								.getINCLUDEDPDU();
						
						// iterating through all pdus which contain our signals
						for (INCLUDEDPDUTYPE inclPdu : pdus) {
							List<INCLUDEDSIGNALTYPE> signals = inclPdu
									.getINCLUDEDSIGNALS().getINCLUDEDSIGNAL();
							
							// iterating throgh all signals
							for (INCLUDEDSIGNALTYPE inclSig : signals) {
								SIGNALINSTANCETYPE sigInst = (SIGNALINSTANCETYPE) inclSig
										.getSIGNALINSTANCEREF().getIDREF();
								
								// finally we can add our signal to the ECU/software
								// note: we add it to the software because in our
								// system model ECUs consist of various Softwares,
								// the class ECU can't have any in- and outputs
								SIGNALTYPE sigType = (SIGNALTYPE) sigInst
										.getSIGNALREF().getIDREF();
								
								String IDstring = sigType.getID();
								
								CommunicationMedium outputSignal = comMediaMap
										.get(IDstring);
								
								// registerWritesTo adds the CommunicationMedium to
								// the Software AND adds this Software in the Set
								// softwareWritingOnto of the Communication Medium
								
								boolean alreadyInList = false;
								String signalName = comMediaMap.get(IDstring)
										.getName();
								
								for (CommunicationMedium cm : sw
										.getComNodesWritingOnto()) {
									if (cm.getName().equals(signalName)) {
										alreadyInList = true;
									}
								}
								
								if (!alreadyInList) {
									sw.registerWritesTo(outputSignal);
								}
								
							}
							
						}
					}
				}
				
				// we handle the inputs just as the outputs before...
				if (conn.getINPUTS() != null) {
					List<ECUPORTTYPE> inputPorts = conn.getINPUTS().getINPUTPORT();
					
					for (ECUPORTTYPE input : inputPorts) {
						List<INCLUDEDPDUTYPE> pdus = input.getINCLUDEDPDUS()
								.getINCLUDEDPDU();
						for (INCLUDEDPDUTYPE inclPdu : pdus) {
							List<INCLUDEDSIGNALTYPE> signals = inclPdu
									.getINCLUDEDSIGNALS().getINCLUDEDSIGNAL();
							for (INCLUDEDSIGNALTYPE inclSig : signals) {
								SIGNALINSTANCETYPE sigInst = (SIGNALINSTANCETYPE) inclSig
										.getSIGNALINSTANCEREF().getIDREF();
								// now we have our output signal
								SIGNALTYPE sigType = (SIGNALTYPE) sigInst
										.getSIGNALREF().getIDREF();
								
								String IDstring = sigType.getID();
								
								CommunicationMedium inputSignal = comMediaMap
										.get(IDstring);
								
								// ...except we use the method registerReadsFrom
								// because we want to implement the signal as input
								boolean alreadyInList = false;
								String signalName = inputSignal
										.getName();
								
								for (CommunicationMedium cm : sw
										.getComNodesReadingFrom()) {
									if (cm.getName().equals(signalName)) {
										alreadyInList = true;
									}
								}
								
								if (!alreadyInList) {
									sw.registerReadsFrom(inputSignal);
								}
							}
							
						}
					}
					
				}
			}
		}

	}

	/**
	 * @param busName
	 * @return
	 */
	private CommunicationMedium getOrCreateComMediaBus(String busName) {
		CommunicationMedium m = comMediaBusses.get(busName);
		if (m == null) {
			m = new CommunicationMedium(busName);
			comMediaBusses.put(busName, m);
			if (VERBOSE) System.out.println("Hit " + busName + "!");
		}
		return m;
		
	}

	/**
	 * Exclusive Signals only have 1 sending and 1 receiving ECU
	 * 
	 * @return a Map of all Exclusive Communication Media
	 */
	public Map<String, CommunicationMedium> getExclusiveComMediaMap() {

		Map<String, CommunicationMedium> result = new TreeMap<String, CommunicationMedium>();
		Set<String> keySet = comMediaMap.keySet();
		for (String s : keySet) {
			CommunicationMedium cm = comMediaMap.get(s);
			if (cm.getReadingSoftware().size() == 1
					&& cm.getSoftwareWritingOnto().size() == 1) {
				result.put(s, cm);
			}
		}
		return result;
	}

	/**
	 * 
	 * @return a Map of all Communication Media
	 */
	public Map<String, CommunicationMedium> getComMediaMap() {
		return comMediaMap;
	}

}