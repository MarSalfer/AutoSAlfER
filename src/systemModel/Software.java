package systemModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import systemModel.attackSurface.ExploitMitTec;
import systemModel.com.Signal;
import types.CPUArchitecture;
import types.ProgrammingLanguage;
import types.Version;
import exploit.Vulnerability;

/**
 * A software object represents an independent piece of software within an ECU. Software is the
 * central entity referenced in this model as it can be individually contaminated
 * with arbitrary code and is directly responsible for its included functionality
 * and data.
 * 
 * Note: The composition relationship implies a dependency/control relationship.
 * 
 * A task is also assumed in an ECU that offers no internal structure.
 * Fulfilled functions are documented in an ECUs Lastenheft and are assumed a task.
 * 
 * A task is a running code entity that is protected from other code.
 * 
 * Software can be considered a task, a runnable, a process, a thread or a fiber.
 * 
 * Note: this class has a natural ordering that is inconsistent with equals.
 * compareTo() decides by the hop distance, equals() by references.
 * 
 * 
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public class Software extends Node implements Exploitable, Comparable<Software> {

	@Deprecated
	private int binaryCodeSize;
	/**
	 * 0 ::= No review done.
	 * 10 ::= software verification done.
	 * Includes external reviews, too, e.g., for CC.
	 */
	@Deprecated
	private int codeReviewQuality = 0;
	/**
	 * How securely was this task compiled? Is ASLR turned on? Is N^X-protection
	 * turned on?
	 * 0 ::= No protection features turned on.
	 * 10 ::= All available protection features turned on.
	 */
	@Deprecated
	private float compilationSecurityMeasure = 0;
	@Deprecated
	private CPUArchitecture cpuArchitecture;
	@Deprecated
	private Integer linesOfCode;
	/**
	 * Certain languages are more secure, e.g., by requiring strict typing.
	 */
	private ProgrammingLanguage programmingLanguage;
	private Version version;
	@Deprecated
	private final Set<Vulnerability> potentialVulnerabilities = new HashSet<Vulnerability>();
	/**
	 * @deprecated
	 */
	private final Set<Capability> capabilities = new HashSet<Capability>();
	private final Set<Asset> attractors = new HashSet<Asset>();
	private boolean isPhaseIClosed = false;

	/**
	 * Communication Media the Software is reading from.
	 */
	private final Set<CommunicationMedium> comReadsFrom = new HashSet<CommunicationMedium>();
	/**
	 * Communication Media the Software is allowed to write to.
	 */
	private final Set<CommunicationMedium> comWritesTo = new HashSet<CommunicationMedium>();
	
	/**
	 * Incoming Job parameter.
	 */
	private final Set<JobIncomingParameter> jobIncomingParameters = new HashSet<JobIncomingParameter>();
	
	/**
	 * A Software's attack surface due to job parameters.
	 */
	private final Set<JobParam> attackSurfaceJobParams = new HashSet<JobParam>();
	
	/**
	 * A Software's attack surface due to incoming signals.
	 */
	private final Set<IncomingSignal> attackSurfaceSignals = new HashSet<IncomingSignal>();
	
	/**
	 * In softare deployed exploit mitigation techniques.
	 */
	private final Set<ExploitMitTec> exploitMitigationTechniques = new HashSet<ExploitMitTec>();
	
	/**
	 * Whether SW/ECU has a reflash method. 
	 */
	public final boolean hasReflashMethod = true; // TODO find out which ECU/SW is not having reflash and adjust accordingly. 
	
	/**
	 * Probability for this Software of being exploitable during an attack try. Defaulting to 20%, if not set specifically.
	 */
	private float vulnProbRce = 0.2f;
	
	
	private final HashMap <Software, HashSet<CommunicationMedium>> comNodesCache = new HashMap<Software, HashSet<CommunicationMedium>>();
	private Set<Software> reachableSoftwareOverComNodes;
	
	
	public Software(){
		super();
	}

	public Software(String name) {
		super(name);
	}
	
	/**
	 * Construct a Software object with given name and vulnerability probability
	 * @param name - Description.
	 * @param vulnProbRce - Probability for this Software of being exploitable during an attack try.
	 */
	public Software(String name, float vulnProbRce) {
		super(name);
		setVulnProb(vulnProbRce);
	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 1 is normal level.
	 * 0 ::= none expected.
	 * above 1: multiplication factor for potential vulnerabilities.
	 */
	public float potentialVulnerabilitiesDanger(){
		return 0.0f; // TODO.
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public ProgrammingLanguage getProgrammingLanguage() {
		return programmingLanguage;
	}

	public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}

	@Override
	public String toString() {
		return "Software [" + name + "]"; //, binaryCodeSize=" + binaryCodeSize
//				+ ", codeReviewQuality=" + codeReviewQuality
//				+ ", compilationSecurityMeasure=" + compilationSecurityMeasure
//				+ ", cpuArchitecture=" + cpuArchitecture + ", linesOfCode="
//				+ linesOfCode + ", minimumNumberHopsToNextAttractor="
//				+ hopDistanceToAttractors + ", programmingLanguage="
//				+ programmingLanguage + ", version=" + version
//				+ ", potentialVulnerability=" + potentialVulnerabilities
//				+ ", m_Capability=" + capabilities + ", m_Attractor="
//				+ attractors + "]";
	}
	

	/**
	 * Getter
	 * @return Communication Media the Software is reading from.  
	 */
	public Set<CommunicationMedium> getComNodesReadingFrom(){
		return Collections.unmodifiableSet(comReadsFrom);
	}
	
	/**
	 * Getter
	 * @return Communication Media the Software is allowed to write to.
	 */
	public Set<CommunicationMedium> getComNodesWritingOnto(){
		return Collections.unmodifiableSet(comWritesTo);
	}
	
	
	public void registerAsset(Asset asset) {
		attractors.add(asset);
		
	}

	/**
	 * Deprecated: Capabilities have been (mis)used for modelling communication relationships.
	 * @deprecated Use {@link #registerReadsFrom(CommunicationMedium)} {@link #registerReadsWritesOn(CommunicationMedium)} and {@link #registerWritesTo(CommunicationMedium)} instead.
	 * @param access
	 */
	public void grant(Capability access) {
		capabilities.add(access);
		access.addOwnerSoftware(this);
		comWritesTo.addAll(access.getCommunicationNodes());
		comReadsFrom.addAll(access.getCommunicationNodes());
	}
	
	/**
	 * Getter.
	 * Complexity: O(1) as of assumption 1,2,3,4. TOOD: Recalcuate after refactoring.
	 * @return A set of Software that are connected to this with an any Com Node.
	 */
	@Override
	public Set<Software> getReachableSoftwareComplete() {
		Set<Software> reachableSoftware = new HashSet<Software>();
		for (CommunicationMedium c : comWritesTo) {
			reachableSoftware.addAll(c.getReachableSoftwareComplete()); // TODO refactor get reachable Software
		}
		return reachableSoftware;
	}
	

	/* (non-Javadoc)
	 * @see systemModel.Node#getAssociatedTasks()
	 */
	public Set<Software> getControlledTasks() {
		Set<Software> s = new HashSet<Software>();
		s.add(this);
		return s;
	}

	public boolean hasAttractors() {
		return attractors.size() > 0;
	}

	/**
	 * Complexity: O(1).
	 * @deprecated
	 */
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
	/**
	 * Complexity: O(1) as of 1,2,3,4 (presumably). TODO: Redo calculation after refactoring.
	 */
	public Set<CommunicationMedium> getComNodesWith(Software target) {
		HashSet<CommunicationMedium> comNodes = comNodesCache.get(target);
		if (comNodes == null) {
			comNodes = 	new HashSet<CommunicationMedium>();
			comNodesCache.put(target, comNodes);
			for (CommunicationMedium c: comWritesTo) {
				if (c.isReadBy(target)) {
					comNodes.add(c);
				}
			}
		}
		return comNodes;
	}

	@Override
	public int compareTo(Software o) {
		if (o == null) {
			throw new NullPointerException("Task must compare with tasks, not a null reference.");
		}
		
		final Integer hopDistanceToAttractors2 = this.hopDistanceToAssets;
		final Integer hopDistanceToAttractors3 = o.hopDistanceToAssets;
		return hopDistanceToAttractors2 - hopDistanceToAttractors3;
	}

	public Set<Asset> getAttractors() {
		return attractors;
	}

	/** 
	 * Query for the software incorporated into this system node.
	 * 
	 * @return - returns a set containing only this software object.
	 * @see systemModel.Node#getIncorporatedSoftware()
	 * @deprecated
	 */
	@Override
	public Set<Software> getIncorporatedSoftware() {
		Set<Software> set = new HashSet<Software>(1);
		set.add(this);
		return set;
	}

	/**
	 * Init the hopdistance of Com Medias.
	 */
	public void initSurroundingComMediaDistance() {
		for (CommunicationMedium cn : getAttachedComMedia()){
			cn.initHopDistance(this.hopDistanceToAssets);
		}
	}

	/**
	 * Get all Communication Media nodes the Software node has access to.
	 * @deprecated
	 */
	public Set<CommunicationMedium> getAttachedComMedia() {
		Set<CommunicationMedium> comMedia = new HashSet<CommunicationMedium>();
		for (Capability cap : capabilities) {
			for (CommunicationMedium cn : cap.getCommunicationNodes()){
				comMedia.add(cn);
			}
		}
		return comMedia;
	}
	
	/**
	 * Get all Communication Media that are writeable for this Software.
	 */
	public Set<CommunicationMedium> getWriteableComMedia() {
		return Collections.unmodifiableSet(comWritesTo);
	}
	


	/**
	 * Was this node already expanded by Phase I? 
	 * @return
	 */
	public boolean isPhaseIClosed() {
		return this.isPhaseIClosed && hopDistanceToAssets != UNSET; 
	}

	/**
	 * Close this System Model Software Node for further Phase I expansions. The heuristic is prepared here.
	 * Complexity: O(1)
	 */
	public void closeNodeForPhaseI() {
		this.isPhaseIClosed  = true;
	}

	
	/**
	 * Getter
	 * @return A set of Software that are connected to this with an unclosed Com Node.
	 */
	public Set<Software> getReachableSoftwareOverOpenComNodes() {
		if (reachableSoftwareOverComNodes == null) {
			reachableSoftwareOverComNodes = new HashSet<Software>(); 
			for (CommunicationMedium c : comWritesTo) {
				reachableSoftwareOverComNodes.addAll(c.getReadingSoftware());
			}
		}
		return reachableSoftwareOverComNodes;
	}

	/**
	 * Mark all attached Communication Media to be not expanded any more in Phase I.
	 * This closes only the writing ones as the reading ones are not applicable for
	 * exploit propagation starting from here.
	 */
	public void closeAttachedComMediaForPhaseI() {
		for (CommunicationMedium c: comWritesTo) {
			c.closeForPhaseI();
		}
	}

	/**
	 * Setter
	 * @param distanceNew How many Exploits ("hops") are necessary until reaching an Asset.
	 */
	public void setHopDistanceToAssets(int distanceNew) {
		hopDistanceToAssets = distanceNew;
	}

	/**
	 * Register a Communication Media that the Software will read from.
	 * @param comMedia The communication media the Software will read from.
	 */
	public void registerReadsFrom(CommunicationMedium comMedia) {
		comReadsFrom.add(comMedia);
		comMedia.registerBeingReadBy(this);
	}

	/**
	 * Register a Communication Media that the Software will write to.
	 * @param comMedia The communication media the Software will write to.
	 */
	public void registerWritesTo(CommunicationMedium comMedia) {
		comWritesTo.add(comMedia);
		comMedia.registerBeingWrittenBy(this);
	}

	/**
	 * Register a Communication Media that the Software will read from and write to. 
	 * @param comMedia The communication media the software reads and writes on.
	 */
	public void registerReadsWritesOn(CommunicationMedium comMedia) {
		registerReadsFrom(comMedia);
		registerWritesTo(comMedia);
	}


	/**
	 * Get the number of incoming signals.
	 * @return Number of incoming signals.
	 */
	public int getComMediaIncomingSignalsNumber() {
		int signals = 0;
		for (CommunicationMedium c : comReadsFrom) {
			if (c instanceof Signal) {
				signals++;
			}
		}
		return signals;
	}

	/**
	 * Get all used (read/write) signals form the Software.
	 */
	public Set<? extends Signal> getUsedSignals() {
		Set<Signal> signals = new HashSet<Signal>(comReadsFrom.size() + comWritesTo.size());
		extractSignalsFromComSet(signals, comReadsFrom);
		extractSignalsFromComSet(signals, comWritesTo);
		return signals;
	}

	/**
	 * Extract Signal elements from CommunicationMedium collections.
	 * @param target The collection to sort the Signal elements into.
	 * @param source The communication medium set to scan for Signal elements.
	 */
	private void extractSignalsFromComSet(Set<Signal> target, Collection<CommunicationMedium> source) {
		for (CommunicationMedium c : source) {
			if (c instanceof Signal) {
				target.add((Signal)c);
			}
		}
	}

	@Deprecated
	public int getJobsIncomingParametersNumber() {
		return jobIncomingParameters.size();
	}

	/**
	 * Add a new parameter to the set of known parameters.
	 * @param jobIncomingParameter
	 */
	@Deprecated
	public void addJob(JobIncomingParameter jobIncomingParameter) {
		jobIncomingParameters.add(jobIncomingParameter);
	}

	/**
	 * Add attack surface to a Software.
	 * 
	 * The attack surface is automatically sorted in separate sets.
	 * @param as
	 */
	public void addAttackSurface(AttackSurfaceSpot as) {
		if (as instanceof JobParam) {
			attackSurfaceJobParams.add((JobParam)as);
		} else if (as instanceof IncomingSignal) {
			attackSurfaceSignals.add((IncomingSignal) as);
		} else {
			throw new IllegalArgumentException("Other Attack Surface Types are not yet implemented.");
		}
	}

	/**
	 * Get attack surface caused by job parameters.
	 */
	public Set<JobParam> getAttackSurfaceJobParams() {
		return Collections.unmodifiableSet(attackSurfaceJobParams);
	}
	
	/**
	 * Get attack surface caused by signals.
	 */
	public Set<IncomingSignal> getAttackSurfaceSignals() {
		return Collections.unmodifiableSet(attackSurfaceSignals);
	}

	/**
	 * get which exploit mitigation techniques are equipped in Software.
	 * @return
	 */
	public Set<ExploitMitTec> getExploitMitigationTechniques() {
		return Collections.unmodifiableSet(exploitMitigationTechniques);
	}

	/**
	 * @return the vulnProb
	 */
	public float getVulnProb() {
		return vulnProbRce;
	}

	/**
	 * @param vulnProb the vulnProb to set
	 */
	public void setVulnProb(float vulnProb) {
		if (0f <= vulnProb && vulnProb <= 1f) {
			this.vulnProbRce = vulnProb;
		} else {
			throw new IllegalArgumentException("Probabilities must be between 0 and 1.");
		}
	}

	
	
}//end Task