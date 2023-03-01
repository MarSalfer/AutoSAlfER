/**
 * 
 */
package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analysis.multipath.salfer.Path;
import attackGraph.AttackScenario;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.Exploitable;
import systemModel.Software;

/**
 * @author Martin Salfer
 * @created 2013
 *
 */
public class CrawledAsset extends CrawlableGraphNode {

	private final Asset asset;
	private List<Crawlable> cheapestPath;
	private List<Exploit> cheapestPathExploits;
	
	
	public CrawledAsset(AttackScenario scenario, Asset attractor) {
		super(scenario);
		this.asset = attractor;
	}

	
	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#getTask()
	 */
	@Override
	public Software getSoftware() {
		throw new UnsupportedOperationException("getTask() not supported for CrawledAttractors! Maybe intended CrawledSoftware.getTask().");
	}

	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#getHopDistanceToAttractors()
	 */
	@Override
	public int getHopDistanceToAttractors() {
		return 0;
	}


	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#getReachableTasks()
	 */
	/**
	 * returns empty set; allows Collection copy constructors without exception handling.
	 */
	@Override
	public Set<Software> getDirectlyReachableSoftwareNodes() {
		return new HashSet<Software>();
	}

	/**
	 * returns empty set; allows Collection copy constructors without exception handling.
	 */
	@Override
	public Set<Software> getIndirectlyReachableSoftwareNodes() {
		return getDirectlyReachableSoftwareNodes();
	}

	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#hasAttractors()
	 */
	@Override
	public boolean hasAttractors() {
		return false;
	}

	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#getAttractors()
	 */
	/**
	 * returns empty set; allows Collection copy constructors without exception handling.
	 */
	@Override
	public Set<Asset> getAttractors() {
		return new HashSet<Asset>();
	}

	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#isAttractor()
	 */
	@Override
	public boolean isAttractor() {
		return true;
	}

	/* (non-Javadoc)
	 * @see attackGraphConstruction.Crawlable#getAttractor()
	 */
	@Override
	public Asset getAsset() {
		return asset;
	}


	@Override
	public Exploitable getExploitable() {
		return asset;
	}

	@Override
	public String toString() {
		return asset.toString();
	}

	/**
	 * @return null as an attractor cannot target anything.
	 */
	@Override
	public Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see analysis.Crawlable#getName()
	 */
	@Override
	public String getName() {
		return asset.getName();
	}
	
	/**
	 * @param path
	 */
	public void setAttackPathAndUsedExploits(Path path) {
		cheapestPath = new ArrayList<Crawlable>(path.getPath());
		cheapestPathExploits = new ArrayList<Exploit>(path.getUsedExploits());
	}

	
}
