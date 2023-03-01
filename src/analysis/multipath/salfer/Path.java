/**
 * 
 */
package analysis.multipath.salfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import analysis.Analyzer;
import analysis.Crawlable;
import analysis.CrawlableGraphNode;
import analysis.CrawlableInterface;
import exploit.Exploit;
import types.Resources;

/**
 * A Path stores data about the path through the network, e.g. accumulated costs
 *  and vulnerability likelihood.
 * 
 * @author Martin Salfer
 * @created 22.02.2017 15:16:39
 *
 */
public class Path implements Comparable<Path> {

	/**
	 * A path is vulnerable enough, if the chained vulnerability likelihood is at least as high as this constant.
	 */
	private static final double VULNERABILITY_PROBABILITY_CUTOFF = 0.001;

	/**
	 * Cost for the path so far.
	 */
	private Resources costSoFar;

	/**
	 * Visited nodes so far. (order is relevant for path export to AttackGraph class.)
	 */
	private final ArrayList<CrawlableGraphNode> visitedNodes;
	
	/**
	 * Used Exploits.
	 * exploit[n] is started from node[n] against node[n+1].
	 */
	private final ArrayList<Exploit> usedExploits;
	/**
	 * Likelihood for vulnerabilities along this path so far.
	 */
	private double vulnerabilityProbability = 1d;

	private double attackabilityProbability;

	private double affordabilityProbability;

	/**
	 * 
	 * @param costSoFar - the necessary Resources for reaching this position.
	 * @param vulnProbability - the probability for vulnerabilities along the path combined.
	 * @param visitedNodes - the path travelled so far.
	 * @param usedExploits - the exploits used for this path (must be n-1 big, where n is the number of visited nodes).
	 */
	public Path(Resources costSoFar, double vulnProbability, ArrayList<CrawlableGraphNode> visitedNodes,
			ArrayList<Exploit> usedExploits) {
		this.costSoFar = costSoFar;
		this.visitedNodes = visitedNodes;
		this.usedExploits = usedExploits;
		this.vulnerabilityProbability = vulnProbability;
		assertConsistency();
	}
	
	/**
	 * Copy constructor.
	 * @param template - the Path to be read from.
	 * @return A shallow copy.
	 */
	public Path(Path template) {
		costSoFar = template.costSoFar;
		visitedNodes = new ArrayList<CrawlableGraphNode>(template.visitedNodes);
		usedExploits = new ArrayList<Exploit>(template.usedExploits);
		vulnerabilityProbability = template.vulnerabilityProbability;
	}
	
	/**
	 * Check for consistency. This check is disabled during production for performance.
	 */
	private void assertConsistency() {
		assert this.visitedNodes.size() == this.usedExploits.size() + 1;
		assert 0 <= vulnerabilityProbability && vulnerabilityProbability <= 1d;
	}

	@Override
	public int compareTo(Path right) {
		if (right == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return (int) (getCostSoFar().getMoneyExpectedValue() - right.getCostSoFar().getMoneyExpectedValue());
	}
	
	/**
	 * Get the current position.
	 */
	public CrawlableGraphNode getPosition() {
		return visitedNodes.get(visitedNodes.size() - 1);
	}
	
	/**
	 * Jump to a new node.
	 * 
	 * @param destination - the new node; will be added to the path.
	 * @param exploit - the used exploit to the new node; will be added.
	 * @param cost - the resources necessary for this one jump; will be accumulated.
	 * @param budget TODO
	 */
	public Path jumpTo(CrawlableGraphNode destination, Exploit exploit, Resources cost, Resources budget) {
		// update Path
		assertConsistency();
		visitedNodes.add(destination);
		usedExploits.add(exploit);
		costSoFar = costSoFar.addWith(cost);
		vulnerabilityProbability *= destination.getVulnerabilityProbability();
		affordabilityProbability = Analyzer.calculateAttackerAffordability(budget, costSoFar);
		attackabilityProbability = affordabilityProbability * vulnerabilityProbability;
		assertConsistency();
		
		// check for enough vulnerability probability.
		if (vulnerabilityProbability >= VULNERABILITY_PROBABILITY_CUTOFF) {
			// update CrawlableGraphNode
			destination.recordAttackabilityWith(this);
			return this;
		} else {
			// As the chance for a vulnerability became too low, terminate the path and return null.  
			return null;
		}
	}
	
	/**
	 * Get the vulnerability likelihood for the path so far.
	 */
	public double getVulnerabilityProbability() {
		return vulnerabilityProbability;
	}

	/**
	 * @return the cost for travelling the path.
	 */
	public Resources getCostSoFar() {
		return costSoFar;
	}

	/**
	 * @param c - Crawlable to test of being already on the path.
	 * @return true if it is already on the path.
	 */
	public boolean hasVisited(CrawlableInterface c) {
		return visitedNodes.contains(c);
	}

	/**
	 * @return iterator of an unmodifable list of the visited nodes.
	 */
	public ListIterator<CrawlableGraphNode> getNodesIteratorAtEnd() {
		return Collections.unmodifiableList(visitedNodes).listIterator(visitedNodes.size()); // set the iterator at the very end.
	}

	/**
	 * @return iterator of an unmodifiable list of the used exploits.
	 */
	public ListIterator<Exploit> getExploitsIteratorAtEnd() {
		return Collections.unmodifiableList(usedExploits).listIterator(usedExploits.size()); // set the iterator at the very end.
	}

	/**
	 * @return unmodifiable list of the visited nodes (from Access til now).
	 */
	public List<Crawlable> getPath() {
		return Collections.unmodifiableList(visitedNodes);
	}

	/**
	 * @return unmodifiable list of used exploits for the visited nodes (from Access til now).
	 */
	public List<Exploit> getUsedExploits() {
		return Collections.unmodifiableList(usedExploits);
	}

	/**
	 * Clones the Path and sets the next direct step.
	 * @param target - the destination, i.e. the next step on the path. Will be added internally. 
	 * @param e - the used exploit for directly reaching the target.
	 * @param exploitCost - the resources necessary to commit this step.
	 * @return a new Path with cloned values and already being set onto the new target.
	 */
	public Path cloneAndJumpTo(CrawlableGraphNode target, Exploit e, Resources exploitCost, Resources budget) {
		return new Path(this).jumpTo(target, e, exploitCost, budget);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Path[" + getPosition() + "]";
	}

	public boolean isCheaperAttackAgainst(Crawlable origin) {
		return costSoFar.isCheaperThan(origin.getCheapestAttackPathCostTilHere());
	}

	public double getAttackabilityProbability() {
		return attackabilityProbability;
	}

	public double getAffordabilityProbability() {
		return affordabilityProbability;
	}

}
