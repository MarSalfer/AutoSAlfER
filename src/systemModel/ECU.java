package systemModel;

import java.util.HashSet;
import java.util.Set;

import types.Resources;

/**
 * An Electronic Control Unit.
 * 
 * Note: The composition relationship implies a dependency/control relationship.
 * 
 * Further possible attributes, but not formalized:
 * <ul>
 * 	<li>SGBD (Steuerger�te-Beschreibungsdatei)</li>
 * </ul>
 * 
 * 
 * We assume that all ECUs are awake or that attackers are able to wake up ECUs
 * and tasks on demand.
 * This implies a simplificiation as cars power off certain ECUs whenever possible,
 * even during a ride.
 * The powered off ECUs are impossible to attack, so a real car is a bit more
 * secure than our always-on-line model evaluation.
 * 
 * 
 * We assume that all tasks are read from storage, i.e., a compromised storage
 * replaces all tasks of a system.
 * This implies that a storage tampering attack is modelled as an access to all
 * ECU incorporated tasks.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class ECU extends Node {
	
	/**
	 * Placeholder for unset integer variables.
	 */
	private final static int UNSET = -1;

	/**
	 * Number of primary DTCs.
	 */
	private int dtcNumberPrimary = ECU.UNSET;

	/**
	 * Number of secondary DTCs.
	 */
	private int dtcNumberSecondary = ECU.UNSET;

	@Override
	public String toString() {
		return "ECU [allTasksMemoryProtected=" + allTasksMemoryProtected
				+ ", binaryCodeSize=" + binaryCodeSize
				+ ", hasPhysicallyAccessibleStorage="
				+ hasPhysicallyAccessibleStorage + ", isDiagAble=" + isDiagAble
				+ ", isReprogrammable=" + isReprogrammable
				+ ", replacementCost=" + replacementCost
				+ ", securityReviewQuality=" + securityReviewQuality
				+ ", tasks=" + softwareIncorporated + "]";
	}
	/**
	 * Are tasks memory protected. Necessary for task spawning and their dependency
	 * relationship.
	 */
	private boolean allTasksMemoryProtected;
	/**
	 * The size of the ECUs software in bytes.
	 * Becomes a substitute for a tasks code size in case of a black box ECU.
	 */
	private long binaryCodeSize = 0;
	/**
	 * Are memory blocks accessible (within less than 10 minutes of effort)?
	 * Influences spawning creating access objects.
	 */
	private boolean hasPhysicallyAccessibleStorage;
	/**
	 * Has this ECU a diagnosis interface? (see BNE)
	 * 
	 * Requirement: Diagnostic accesses should be considered as they are handy for
	 * attackers.
	 * This implies spawning a diagnostic task for every ECU in the model that has
	 * documented diag functionality.
	 * The spawned task will be connected to all ECU associated communication nodes.
	 * A side effect implication is spawning tasks that sometimes do not exist.
	 * The graph might become bigger as necessary and some task side effects might not
	 * be seen by the model, which assumes an isolated task.
	 */
	private boolean isDiagAble;
	private boolean isReprogrammable;
	/**
	 * The cost a replacement takes at an official dealer.
	 */
	private Resources replacementCost;
	private float securityReviewQuality;
	
	private final Set<Software> softwareIncorporated;

	/**
	 * Number of diagnosis jobs this ECU has.
	 */
	private int jobsNumberDiagnosis = ECU.UNSET;

	/**
	 * Number of standard jobs this ECU has.
	 */
	private int jobsNumberStandard = ECU.UNSET;

	/**
	 * Number of developer jobs this ECU has.
	 */
	private int jobsNumberDeveloper = ECU.UNSET;

	/**
	 * Number of jobs without any authentication (level 0).
	 */
	private int jobsNumberAL0 = 0;

	/**
	 * Number of jobs with authentication level 1.
	 */
	private int jobsNumberAL1 = 0;

	/**
	 * Number of jobs with authentication level 3.
	 */
	private int jobsNumberAL3 = 0;

	/**
	 * Number of jobs with authentication level 4.
	 */
	private int jobsNumberAL4 = 0;

	/**
	 * Number of jobs with authentication level 5.
	 */
	private int jobsNumberAL5 = 0;
	

	public ECU(String name) {
		super(name);
		softwareIncorporated = new HashSet<Software>();
	}

	public ECU(){
		super("<NoNameECU>");
		softwareIncorporated = new HashSet<Software>();
	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	/**
	 * Returns all Communication Nodes that are reachable from software within this
	 * ECU.
	 */
	public Set<CommunicationMedium> getReachableComNodes(){
		return null;
	}

	public void addSoftware(Software software) {
		softwareIncorporated.add(software);
	}

	@Override
	public Set<Software> getReachableSoftwareComplete() {
		return new HashSet<Software>(softwareIncorporated);
	}

	@Override
	public Set<Software> getControlledTasks() {
		return new HashSet<Software>(softwareIncorporated);
	}

	/** 
	 * Query for the software incorporated into this system node.
	 * 
	 * @return - returns a set containing all software objects of this ECU.
	 * @see systemModel.Node#getIncorporatedSoftware()
	 */
	@Override
	public Set<Software> getIncorporatedSoftware() {
		return getControlledTasks();
	}

	/**
	 * Return the number of primary DTCs.
	 * @return 
	 */
	public int getDtcNumberPrimary() {
		return dtcNumberPrimary;
	}
	
	/**
	 * Set the number of primary DTCs.
	 * @param numberOfDtcs - The number of DTCs an ECU has.
	 */
	public void setDtcNumberPrimary(int numberOfDtcs) {
		assert dtcNumberPrimary==ECU.UNSET;
		dtcNumberPrimary = numberOfDtcs;
	}
	
	/**
	 * Return the number of secondary DTCs.
	 * @return 
	 */
	public int getDtcNumberSecondary() {
		return dtcNumberSecondary;
	}
	
	/**
	 * Set the number of Secondary DTCs.
	 * @param numberOfDtcs - The number of DTCs an ECU has.
	 */
	public void setDtcNumberSecondary(int numberOfDtcs) {
		assert dtcNumberSecondary == ECU.UNSET;
		dtcNumberSecondary = numberOfDtcs;
	}

	/**
	 * Get number of diagnosis jobs.
	 * @return
	 */
	public int getJobsNumberDiagnosis() {
		return jobsNumberDiagnosis;
	}
	
	/**
	 * Get number of standard jobs.
	 * @return
	 */
	public int getJobsNumberStandard() {
		return jobsNumberStandard;
	}
	
	/**
	 * Get number of developer jobs.
	 * @return
	 */
	public int getJobsNumberDeveloper() {
		return jobsNumberDeveloper;
	}
	
	/**
	 * Set the number of diagnosis jobs that this ECU has.
	 * Should only be called once.
	 */
	public void setJobsNumberDiagnosis(int number) {
		assert jobsNumberDiagnosis == ECU.UNSET;
		jobsNumberDiagnosis = number;
	}

	/**
	 * Set the number of standard jobs that this ECU has.
	 * Should only be called once.
	 */
	public void setJobsNumberStandard(int number) {
		assert jobsNumberStandard == ECU.UNSET;
		jobsNumberStandard = number;
	}

	/**
	 * Set the number of developer jobs that this ECU has.
	 * Should only be called once.
	 */
	public void setJobsNumberDeveloper(int number) {
		assert jobsNumberDeveloper == ECU.UNSET;
		jobsNumberDeveloper = number;
	}

	/**
	 * Add code size to the ECU.
	 * Must be add up, for binary images comprise several images. 
	 * @param length
	 */
	public void addCodeSize(long length) {
		this.binaryCodeSize += length;
	}

	/**
	 * Get the size of the binary code of this ECU.
	 * @return the size of the binary code of this ECU.
	 */
	public long getBinaryCodeSize() {
		return binaryCodeSize;
	}

	/**
	 * Get the number of jobs that do not need authentication (=Level 0).
	 * @return Get the number of jobs that do not need authentication (=Level 0).
	 */
	public int getJobsAL0() {
		return jobsNumberAL0;
	}

	/**
	 * Get the number of jobs that do need authentication level 1.
	 */
	public int getJobsAL1() {
		return jobsNumberAL1;
	}

	/**
	 * Get the number of jobs that do need authentication level 3.
	 */
	public int getJobsAL3() {
		return jobsNumberAL3;
	}

	/**
	 * Get the number of jobs that do need authentication level 4.
	 */
	public int getJobsAL4() {
		return jobsNumberAL4;
	}

	/**
	 * Get the number of jobs that do need authentication level 5.
	 */
	public int getJobsAL5() {
		return jobsNumberAL5;
	}


	/**
	 * Increment the number of jobs with the authentication level 0.
	 */
	public void setJobsAL0Inc() {
		jobsNumberAL0++;
	}

	/**
	 * Increment the number of jobs with the authentication level 1.
	 */
	public void setJobsAL1Inc() {
		jobsNumberAL1++;
	}

	/**
	 * Increment the number of jobs with the authentication level 3.
	 */
	public void setJobsAL3Inc() {
		jobsNumberAL3++;
	}

	/**
	 * Increment the number of jobs with the authentication level 4.
	 */
	public void setJobsAL4Inc() {
		jobsNumberAL4++;
	}

	/**
	 * Increment the number of jobs with the authentication level 5.
	 */
	public void setJobsAL5Inc() {
		jobsNumberAL5++;
	}

	/**
	 * Get the number of incoming signals into this ECU's software.
	 * If a signal enters several Software objects, the signal will be counted several times. 
	 * @return
	 */
	public int getIncomingSignalsNumber() {
		int signals = 0;
		for (Software s : softwareIncorporated) {
			signals += s.getComMediaIncomingSignalsNumber();
		}
		return signals; 
	}

	
	
}//end ECU