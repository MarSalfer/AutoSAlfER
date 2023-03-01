package systemModel.statistics;

import systemModel.ECU;
import systemModel.Software;
import systemModel.SystemModel;

public class Summary {


	/**
	 * String for newline in CSV output.
	 */
	public static String nl = "\n";
	/**
	 * Separator String for CSV output.
	 */
	public static String sep = "\t";
	
	/**
	 * Give a summary over all ECUs of a System Model regarding Jobs + DTCs + PBX.
	 * @param sysModel - The System Model to be printed.
	 * @return - The summary as a CSV-able string.
	 */
	public static String giveJobsDTCsPbxSummaryPerEcu(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("Name" + sep + "DTC Primary" + sep + "DTC Secondary" + sep + 
				"DiagJobs" + sep + "StandJobs" + sep + "DevJobs" + sep + "CodeSize" + nl);
		for (ECU ecu : sysModel.getEcus()) {
			str.append(ecu.getName() + sep);
			str.append(ecu.getDtcNumberPrimary() + sep);
			str.append(ecu.getDtcNumberSecondary() + sep);
			str.append(ecu.getJobsNumberDiagnosis() + sep);
			str.append(ecu.getJobsNumberStandard() + sep);
			str.append(ecu.getJobsNumberDeveloper() + sep);
			str.append(ecu.getBinaryCodeSize() + nl);
		}
		return str.toString();
	}
	
	

	public static String giveJobsTypeAuthSummary(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("Name" + sep + "JobsTotal" + sep + "AuthL0" + sep + "AuthL1" + sep + "AuthL3" + sep + "AuthL4" + sep + "AuthL5" + nl);
		for (ECU ecu : sysModel.getEcus()) {
			str.append(ecu.getName() + sep);
			str.append(ecu.getJobsNumberDiagnosis() + ecu.getJobsNumberStandard() + ecu.getJobsNumberDeveloper() + sep);
			str.append(ecu.getJobsAL0() + sep);
			str.append(ecu.getJobsAL1() + sep);
			str.append(ecu.getJobsAL3() + sep);
			str.append(ecu.getJobsAL4() + sep);
			str.append(ecu.getJobsAL5() + sep);
			str.append(nl);
		}
		return str.toString();
	}
	
	
	public static String incomingSignalsPerECU(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("ECU" + sep + "IncomingSig" + nl);
		
//		for (ECU ecu : sysModel.getEcus()) { // TODO: ECU calling is the correct way. But not supported by Fibex importer.
		// We use the software set instead, which is filled by the fibex importer.
		for (Software s : sysModel.getSoftwareSet()) {
			str.append(s.getName() + sep);
			str.append(s.getComMediaIncomingSignalsNumber() + sep);
			str.append(nl);
		}
		return str.toString();
	}


	/**
	 * Get the total number of used signals in this system model.
	 * Signals are counted uniquely, i.e., no duplicates occur.
	 * @param mySystem The system model under consideration.
	 * @return The number of unique signals.
	 */
	public static String  signalsTotalNumber(SystemModel sysModel) {
		return "Total number of signals in the system model: " + sysModel.getSignalsTotalNumber();
	}


}
