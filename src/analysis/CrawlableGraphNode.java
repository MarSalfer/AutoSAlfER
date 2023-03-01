package analysis;

import attackGraph.AttackScenario;

/**
 * CrawlableGraphNodes are nodes that count at attack graph construction.
 * These include Access, Software and Asset nodes.
 * 
 * @author Martin Salfer
 * @created 2014
 *
 */
public abstract class CrawlableGraphNode extends Crawlable {

	public CrawlableGraphNode(AttackScenario scenario) {
		super(scenario);
	}

	
	public CrawlableGraphNode(AttackScenario scenario, float cost) {
		super(scenario, cost);
	}


	/**
	 * Getter
	 * @return Previous Crawlable that qualifies for the attack graph as a node.
	 */
	public CrawlableGraphNode getLastCrawlableGraphNode() {
		CrawlableInterface lastNode = this.getAttackPredecessor();

		while (!(lastNode instanceof CrawlableGraphNode) && (lastNode != null)) {
			lastNode = lastNode.getAttackPredecessor();
		}
		return (CrawlableGraphNode) lastNode; // Cast allowed as above while statement searches for CrawlableGraphNode (or null).
	}
	
	
	
	/**
	 * Typical CrawlableGraphNodes do not need a vulnerability to be exploitable.
	 */
	public double getVulnerabilityProbability() {
		return 1d;
	}
	
}
