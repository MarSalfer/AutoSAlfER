/**
 * 
 */
package analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Exploitable;
import systemModel.Software;
import attackGraph.AttackScenario;
import exploit.Exploit;

/**
 * @author Martin Salfer
 * @created 2014-06-21
 */
public class CrawledComMedia extends Crawlable implements Comparable<Crawlable>  {


	final CommunicationMedium comNode;
	
	/**
	 * Mapping from a system model's communication nodes to crawler-based CommunicationMediaCrawlables.
	 */
	private static Map<CommunicationMedium,CrawledComMedia> sysNode2Crawlable = new HashMap<CommunicationMedium,CrawledComMedia>();
	
	/**
	 * @deprecated
	 * @param scenario
	 */
	public CrawledComMedia(AttackScenario scenario) {
		super(scenario);
		comNode = null;
		throw new UnsupportedOperationException("CrawledComMedia must include a Communication Node.");
	}

	
	/**
	 * @deprecated
	 * @param scenario
	 * @param cost
	 */
	public CrawledComMedia(AttackScenario scenario, float cost) {
		super(scenario, cost);
		comNode = null;
		throw new UnsupportedOperationException("CrawledComMedia must include a Communication Node.");
	}

	/**
	 * CrawledComMedia - Represents a Communication Node of our System Model.
	 * @param scenario - The crawl scenario being considered.
	 * @param comNode - The Communication Medium to refer to.
	 */
	public CrawledComMedia(AttackScenario scenario, CommunicationMedium comNode) {
		super(scenario);
		this.comNode = comNode;
	}
	

	/**
	 * Getter.
	 * @return null - as a CrawledComMedia never refers to a Software node.
	 */
	@Override
	public Software getSoftware() {
		return null;
	}

	/**
	 * Gett
	 * @return Number of Exploitables until reaching the first Attractor.
	 */
	@Override
	public int getHopDistanceToAttractors() {
		return comNode.getHopsToAssets();
	}

	/**
	 * Getter.
	 * @return all Software nodes associated to this Communication node. 
	 */
	@Override
	public Set<Software> getDirectlyReachableSoftwareNodes() {
		return comNode.getReachableSoftwareComplete();
	}

	/**
	 * Getter.
	 * @return all Software nodes associated to this Communication node. 
	 */
	@Override
	public Set<Software> getIndirectlyReachableSoftwareNodes() {
		return getDirectlyReachableSoftwareNodes();
	}

	

	/**
	 * Has this CrawledComMedium attractors?
	 * @return false - Never, only Software nodes have Attractors.
	 */
	@Override
	public boolean hasAttractors() {
		return false;
	}

	/**
	 * @return null - Only Software nodes have Assets.
	 */
	@Override
	public Set<Asset> getAttractors() {
		return null;
	}

	/**
	 * @return Always false - Only Software nodes have Assets.
	 */
	@Override
	public boolean isAttractor() {
		return false;
	}

	/**
	 * @return Always null - Only Software nodes have Assets.
	 */
	@Override
	public Asset getAsset() {
		return null;
	}

	/**
	 * @return Always null - Only Software and Asset nodes are exploitable.
	 */
	@Override
	public Exploitable getExploitable() {
		return null;
	}

	
	/**
	 * Get the Crawlable wrapper for a system model's communication node.
	 * @param comNode The system model node to be wrapped.
	 * @return A wrapper including the given system model communication medium in it. (Null if not found.)
	 */
	public static CrawledComMedia convertToCrawlable(CommunicationMedium comNode) {
		return CrawledComMedia.sysNode2Crawlable.get(comNode);
	}


	/**
	 * Getter
	 * @return Communication Node of the System Model represented by this Crawlable.
	 */
	public CommunicationMedium getComMedium() {
		return comNode;
	}

	public String toString() {
		return comNode.toString();
	}
	
	public static void register(CommunicationMedium comNode, CrawledComMedia targetedCrawledComNode) {
		sysNode2Crawlable.put(comNode, targetedCrawledComNode);
	}

	/**
	 * @param target
	 * @return
	 */
	public Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target) {
		Set<Exploit> consideredExploits = new HashSet<Exploit>(); 
		for (Exploit e : scenario.getAllExploitsAgainstTask(target.getExploitable())) { // Complexity: O(E) + O(E) iterations.
			if (e.areSwCapComConditionsMet(getComMedium(), target.getExploitable())) // Complexity: O(1) as of 1,2,3,4,6,7,8.
				consideredExploits.add(e); // Complexity: O(1)
		}
		return consideredExploits;
	}

	/* (non-Javadoc)
	 * @see analysis.Crawlable#getName()
	 */
	@Override
	public String getName() {
		return comNode.getName();
	}
	
	
}
