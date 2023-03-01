package systemModel.statistics;

import systemModel.ECU;
import systemModel.SystemModel;

public class Zedis {

	public Zedis() {
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * Give a CSV-able summary over all ECUs of a System Model regarding DTCs.
	 * @param sysModel - The System Model to be printed.
	 * @return - The summary as a CSV-able string.
	 */
	public static String giveDtcSummaryPerEcu(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("Name" + Summary.sep + "DTC Primary" + Summary.sep + "DTC Secondary" + Summary.nl);
		for (ECU ecu : sysModel.getEcus()) {
			str.append(ecu.getName() + Summary.sep);
			str.append(ecu.getDtcNumberPrimary() + Summary.sep);
			str.append(ecu.getDtcNumberSecondary() + Summary.nl);
		}
		return str.toString();
	}

	/**
	 * Give a summary over all ECUs of a System Model regarding Jobs.
	 * @param sysModel - The System Model to be printed.
	 * @return - The summary as a CSV-able string.
	 */
	public static String giveJobsSummaryPerEcu(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("Name" + Summary.sep + "DiagJobs" + Summary.sep + "StandJobs" + Summary.sep + "DevJobs" + Summary.nl);
		for (ECU ecu : sysModel.getEcus()) {
			str.append(ecu.getName() + Summary.sep);
			str.append(ecu.getJobsNumberDiagnosis() + Summary.sep);
			str.append(ecu.getJobsNumberStandard() + Summary.sep);
			str.append(ecu.getJobsNumberDeveloper() + Summary.nl);
		}
		return str.toString();
	}
	
	
	/**
	 * Give a summary over all ECUs of a System Model regarding Jobs.
	 * @param sysModel - The System Model to be printed.
	 * @return - The summary as a CSV-able string.
	 */
	public static String giveJobsDTCsSummaryPerEcu(SystemModel sysModel) {
		StringBuilder str = new StringBuilder();
		str.append("Name" + Summary.sep + "DTC Primary" + Summary.sep + "DTC Secondary" + Summary.sep + "DiagJobs" + Summary.sep + "StandJobs" + Summary.sep + "DevJobs" + Summary.nl);
		for (ECU ecu : sysModel.getEcus()) {
			str.append(ecu.getName() + Summary.sep);
			str.append(ecu.getDtcNumberPrimary() + Summary.sep);
			str.append(ecu.getDtcNumberSecondary() + Summary.sep);
			str.append(ecu.getJobsNumberDiagnosis() + Summary.sep);
			str.append(ecu.getJobsNumberStandard() + Summary.sep);
			str.append(ecu.getJobsNumberDeveloper() + Summary.nl);
		}
		return str.toString();
	}



	

}
