/**
 * 
 */
package analysis.multipath.bayes;

import java.util.Set;

import analysis.Crawlable;
import analysis.CrawledSoftware;
import attackGraph.AttackScenario;
import systemModel.Software;

/**
 * 
 * @author Martin Salfer
 * @created 2016-04-04
 *
 */
public class CrawledSoftwareP3 extends CrawledSoftware {

	public CrawledSoftwareP3(AttackScenario scenario, Software task) {
		super(scenario, task);
	}

	/**
	 * Compare according to costs having had so far.
	 * That allows a breadth-first search.
	 */
	@Override
	public int compareTo(Crawlable cs) {
		if (cs == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return (int) (getMinCostMeanTilHere() - cs.getMinCostMeanTilHere()); 
	}

	/**
	 * Get all reachable Software nodes.
	 * WARNING: This will also collect indirectly reachable nodes!
	 * This is necessary for the Bayes-Network-Algorithm.
	 * @return 
	 */
	@Override
	public Set<Software> getDirectlyReachableSoftwareNodes() {
		return getIndirectlyReachableSoftwareNodes();
	}
}
