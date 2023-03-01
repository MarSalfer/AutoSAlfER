package metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import systemModel.IncomingData;
import systemModel.Software;
import systemModel.attackSurface.ExploitMitTec;
import systemModel.subTypes.InputType;
import attackerProfile.AttackerProfile;

public class SoftwareAssessment {// TODO refactor to ECU instead of software as soon as importer is fixed.

//	private final AttackerProfile a;
	private final Software sw;
	
	/*
	 * Quick Hack attacker profile. Please refactor after V1 conference submission.
	 */
	private final double ah = 2_000; // per hardware access
	private final double authLevelAboveZero = 5_000; // effort for extracting an authentication key.
	private final double fs = 10; // find vulnerabilities per spot
	private final double vulFindingReflash = 1_000d; // find the reflash mechanism.
	private final double bPrimitve = 3_000; // primitive type access.
	private final double bComplex = 500; // complex data types are easier to exploit, usually buffered.
	
	private final Map<ExploitMitTec,Double> c = new HashMap<ExploitMitTec,Double>() {{
		put(ExploitMitTec.ASLR,10_000d);
		put(ExploitMitTec.NX, 10_000d);
		put(ExploitMitTec.SC, 1_000d);
		put(ExploitMitTec.PS, 1_000d);
	}};
	
	
	public SoftwareAssessment(Software sw) {
//		this.a = a;
		this.sw = sw;
	}
	
	
	public double getMeanFindingEffort() { 
		double initialEffortCodeRecovery = getInitialEffortCodeRecoveryFor();
		double meanSignalsEffort = getMeanSignalsEffortFor(sw);
		double meanJobsEffort = getMeanJobsEffortFor(sw);
		double meanFlashEffort = vulFindingReflash;
		
		return initialEffortCodeRecovery + meanJobsEffort + meanSignalsEffort + meanFlashEffort; 
	}



	/**
	 * How much effort is usually necessary for finding a vulnerability in job parameters.
	 * 
	 * Caution: Formula is wrong as X (number of vulnerabilities) is statistically not equally distributed.
	 * @param sw2
	 * @return
	 */
	private double getMeanJobsEffortFor(Software sw2) { // TODO new formula.
		int numberOfIncomingJobParameters = sw2.getAttackSurfaceJobParams().size();
		return (fs * (numberOfIncomingJobParameters + 1)) / 2;
	}


	/**
	 * How much effort is usually necessary for finding a vulnerability in signals.
	 * 
	 * Caution: Formula is wrong as X (number of vulnerabilities) is statistically not equally distributed.
	 * @param sw2
	 * @return
	 */
	private double getMeanSignalsEffortFor(Software sw2) { // TODO new formula
		int numberOfIncomingParameters = sw2.getComMediaIncomingSignalsNumber();
		return (fs * (numberOfIncomingParameters + 1)) / 2;
	}


	/**
	 * Effort for opening hardware for extracting the binary software.
	 * @return
	 */
	private double getInitialEffortCodeRecoveryFor() {
		return ah;
	}


	public double getMeanExploitEffort() {
		List<Double> exploitEfforts = new ArrayList<Double>();
		
		exploitEfforts.addAll(getMeanExploitEffortSignalsAndJobs());

		exploitEfforts.add(getReflashEffort());

		double sum = 0d;
		for (Double effort : exploitEfforts) {
			sum += effort;
		}
		return sum/exploitEfforts.size();
	}


	/**
	 * Get effort for finding (a maybe existing) reflash mechanism.
	 * @return effort for 
	 */
	private Double getReflashEffort() {
		if (sw.hasReflashMethod) {
			return authLevelAboveZero;
		} else {
			return null;
		}
	}


	/**
	 * Effort for signals and job parameters.
	 * @return
	 */
	private List<Double> getMeanExploitEffortSignalsAndJobs() {
		List<Double> efforts = new ArrayList<Double>();
		
		Set<IncomingData> incoming = new HashSet<IncomingData>();
		incoming.addAll(sw.getAttackSurfaceSignals());
		incoming.addAll(sw.getAttackSurfaceJobParams());
		
		for (IncomingData d : incoming) {
			double accessEffort = calculateAccessEffort(d);
			double basicEffort = calculateBasicEffort(d);
			double counterMitigationEffort = calcCounterMitigationEffort(d);
			
			efforts.add(accessEffort+basicEffort+counterMitigationEffort);
		}
		
		return Collections.unmodifiableList(efforts);
	}


	/**
	 * Calculate how much effort for accessing attack surface is necessary.
	 * @param d
	 * @return
	 */
	private double calculateAccessEffort(IncomingData d) {
		if (d.authLevel == 0) {
			return 0d;
		} else if (d.authLevel > 0) {
			return authLevelAboveZero;
		} else throw new IllegalArgumentException("Auth Level must be positive.");
	}


	/**
	 * Basic effort for an incoming Data Attack Surface.
	 * 
	 * Here all is assumed primitive when unknown.
	 * @param as
	 */
	private double calculateBasicEffort(IncomingData as) {
		double basicEffort;
		if (as.type == InputType.UNKNOWN || as.type == InputType.PRIMITIVE) {
			basicEffort = bPrimitve;
		} else if (as.type == InputType.COMPLEX) {
			basicEffort = bComplex;
		} else {
			throw new IllegalArgumentException("Illegal incoming data type.");
		}
		return basicEffort;
	}

	
	/**
	 * Effort for an attacker to counteract mitigation techniques.
	 * @param d
	 * @return
	 */
	private double calcCounterMitigationEffort(IncomingData d) {
		double counterEffort = 0d;
		for (ExploitMitTec e : sw.getExploitMitigationTechniques()) {
			counterEffort += c.get(e);
		}
		return counterEffort;
	}

	
}
