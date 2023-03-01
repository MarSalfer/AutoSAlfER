package attackGraph;

import java.util.List;

import analysis.Crawlable;
import analysis.multipath.salfer.Path;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.Exploitable;

public class AttackGraphNodeAsset extends AttackGraphNode {

	public final Asset attractor;

	/**
	 * Cheapest path to here.
	 */
	private List<Crawlable> cheapestPath; // TODO auf AttackGraphNode umbauen.
	/**
	 * All exploits that were used on the cheapest path.
	 */
	private List<Exploit> cheapestPathExploits;
	private double cheapestPathVulnProb = 0d;

	public AttackGraphNodeAsset(Asset a) {
		this.attractor = a;
	}
	
	@Override
	public Exploitable getSysNode() {
		return attractor;
	}
	
	@Override
	public String getName() {
		return attractor.toString();
	}
	
	@Override
	public String toString() {
		return attractor.toString();
	}

	/* (non-Javadoc)
	 * @see attackGraph.AttackGraphNode#getIdentificationObject()
	 */
	@Override
	public Object getIdentificationObject() {
		return attractor;
	}

	/**
	 * Get an attack graph attractor node's attractor.
	 * @return The node's attractor.
	 */
	public Asset asset() {
		return attractor;
	}

	/**
	 * Count a Path.
	 * @param path - The Path that is to be counted.
	 */
	public void setAttackPath(Path path) {
		cheapestPath = path.getPath();
		cheapestPathExploits = path.getUsedExploits();
	}
	
}
