/**
 * 
 */
package analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import attackGraph.AttackScenario;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Exploitable;
import systemModel.Software;

/**
 * 
 * 
 * @author Martin Salfer
 *
 */
// TODO: Refactor auf CrawledExploitable, da es auch einen Attrac
public class CrawledSoftware extends CrawlableGraphNode implements Comparable<Crawlable> {

	public static final Crawlable START_NODE = new CrawledSoftware(null, null);

	private final Software software;

	private HashMap<CrawlableInterface, Set<Exploit>> consideredExploitsCache = new HashMap<CrawlableInterface, Set<Exploit>>();
	
	public CrawledSoftware(AttackScenario scenario, Software task) {
		super(scenario);
		this.software = task;
	}


	@Deprecated
	public CrawledSoftware(AttackScenario scenario, Software software, float pathCostSoFar) {
		super(scenario, pathCostSoFar);
		this.software = software;
	}


	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawable#getTask()
	 */
	@Override
	public Software getSoftware() {
		return software;
	}
	
	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawable#getHopDistanceToAttractors()
	 */
	@Override
	public int getHopDistanceToAttractors() {
		return software.getHopsToAssets();
	}


//	public void setSearchPrecessorNode(CrawledSoftware n) {
//		searchPredecessor = n;
//	}

//	public CrawledSoftware getSearchPrecessorNode() {
//		return searchPredecessor;
//	}



	/**
	 * Get all reachable Software nodes.
	 * Nothing, as Software nodes have to be yielded via Communication nodes, not Software nodes.
	 * Complexity: O(1).
	 * @return Empty set as Software nodes can never directly reach other Software (yet).
	 */
	@Override
	public Set<Software> getDirectlyReachableSoftwareNodes() {
		return new HashSet<Software>();
	}

	/**
	 * Get all reachable Software nodes.
	 */
	@Override
	public Set<Software> getIndirectlyReachableSoftwareNodes() {
		return getSoftware().getReachableSoftwareOverOpenComNodes();
	}


	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawable#hasAttractors()
	 */
	@Override
	public boolean hasAttractors() {
		return software.hasAttractors();
	}


	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawable#getAttractors()
	 */
	@Override
	public Set<Asset> getAttractors() {
		return software.getAttractors();
	}


	@Override
	public boolean isAttractor() {
		return false;
	}


	@Override
	public Asset getAsset() {
		throw new UnsupportedOperationException("Not supported by CrawledSoftare! Maybe CrawledAttractor or getAttractors() intended.");
	}


	@Override
	public Exploitable getExploitable() {
		return software;
	}
	
	@Override
	public String toString() {
		return software.toString();
	}


	/**
	 * Return a set of all connected Communication Medium nodes.
	 * @return  set of all connected Communication Medium nodes.
	 * @deprecated
	 */
	@Override
	public Set<CommunicationMedium> getReachableComMedia() {
		return software.getAttachedComMedia();
	}

	/**
	 * Get all attackable Communication Media.
	 * Those are attackable as soon as the software can write to the Com Media.
	 * attackable := writeable.
	 * @return The set of attackable Communication Media. 
	 */
	@Override
	public Set<CommunicationMedium> getAttackableComMedia() {
		return software.getWriteableComMedia();
	}


	/**
	 * @param target
	 * @return
	 */
	@Override
	public Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target) {
		Set<Exploit> consideredExploits = consideredExploitsCache.get(target);
		if (consideredExploits == null) { // cache miss?
			consideredExploits = new HashSet<Exploit>();
			consideredExploitsCache.put(target, consideredExploits);
			// collect possible exploits.
			for (Exploit e : scenario.getAllExploitsAgainstTask(target.getExploitable())) { // Complexity: O(E) + O(E) iterations.
				if (e.areSwCapComConditionsMet(getSoftware(), target.getExploitable())) // Complexity: O(1) as of 1,2,3,4,6,7,8.
					consideredExploits.add(e); // Complexity: O(1)
			}
		}
		return Collections.unmodifiableSet(consideredExploits);
	}


	/* (non-Javadoc)
	 * @see analysis.Crawlable#getVulnerabilityLikelihood()
	 */
	@Override
	public double getVulnerabilityProbability() {
		return software.getVulnProb();
	}

//	/**
//	 * Get the exploitation likelihood against this crawlable coming from another crawlable. 
//	 * @see analysis.CrawlableInterface#getExploitationLikelihoodFrom(analysis.CrawlableInterface)
//	 */
//	public double getExploitationLikelihoodFrom(CrawlableInterface origin) {
////		Set<Exploit> usedExploits = attackOrigins.get(origin).keySet();
//		
//		// get costs from the whole path so far (as resources, with proper variance)
//		// hard, as many pathes are possible and several have to be unified again.
//		
//		// get the budget of the attacker
////		for (Exploit)
//		// TODO Auto-generated method stub
//		return 1d;
//	}

	/* (non-Javadoc)
	 * @see analysis.Crawlable#getName()
	 */
	@Override
	public String getName() {
		return software.getName();
	}
	
	

}