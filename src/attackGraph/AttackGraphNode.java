package attackGraph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import analysis.CrawlableGraphNode;
import systemModel.Exploitable;
import types.Resources;



/**
 * Each attack graph node represents a task, an attractor or an access instance.
 * The attack graph is built from one system model and for one attack profile.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public abstract class AttackGraphNode {

	@XmlTransient
	private AttackGraphNode predecessor;
	
	
//	/**
//	 * Predecessor and cost tuple.
//	 * The cost defines how much effort is necessary to reach this node coming
//	 * from the according predecessor.
//	 * TODO: This collection can be unified with the predecessor field above.
//	 */
//	private Map<AttackGraphNode,Double> predecessors;


	private Resources mostAttackablePathCost = new Resources(Double.POSITIVE_INFINITY, 0d);
	private double mostAttackablePathAffordability = 0d; // start with impossible.
	private double mostAttackablePathVulnerability = 0d; // start with impossible.
	private double mostAttackablePathAttackability = 0d; // start with impossible.

	private Resources recordedExpansionsResources = new Resources(0d, 0d);
	private double recordedExpansionVulnerabilityAcc = 0d;
	private int recordedPathExpansionsNumber = 0;
	
	public void setOverallAffordabilityProb(double overallAffordabilityProb) {
		assert 0d <= overallAffordabilityProb && overallAffordabilityProb <= 1d;
		this.mostAttackablePathAffordability = overallAffordabilityProb;
	}

	public double getAttackabilityProbabilityForOptimalPath() {
		return mostAttackablePathAttackability;
	}

	public void setAttackabilityProbabilityForOptimalPath(double overallAttackabilityProb) {
		assert 0 <= overallAttackabilityProb && overallAttackabilityProb <= 1d;
		this.mostAttackablePathAttackability = overallAttackabilityProb;
	}

	public void setVulnerabilityProbabilityForOptimalPath(double overallVulnerablePathsProb) {
		assert 0 <= overallVulnerablePathsProb && overallVulnerablePathsProb <= 1d;
		this.mostAttackablePathVulnerability = overallVulnerablePathsProb;
	}
	
	public double getVulnerabilityProbabilityForOptimalPath() {
		return mostAttackablePathVulnerability;
	}

	public double getAffordabilityProbabilityForOptimalPath() {
		return mostAttackablePathAffordability;
	}
	
	/**
	 * Get the represented system node.
	 * @return
	 */
	public abstract Exploitable getSysNode();

	/**
	 * Get a representative name for this node.
	 * @return 
	 */
	@XmlAttribute
	public abstract String getName();
	
	/**
	 * 
	 * @return An object that identifies an attack graph node.
	 */
	public abstract Object getIdentificationObject();

	/**
	 * Set the predecessor of a graph.
	 * 
	 * @param origin
	 */
	public void setPredecessor(AttackGraphNode origin) {
		this.predecessor = origin;
	}
	
	/**
	 * Get the predecessor.
	 * @return The predecessor node in an attack graph.
	 */
	@XmlTransient
	public AttackGraphNode getPredecessor() {
		return predecessor;
	}

	/**
	 * @return resources for most attackable path (not necessarily the minimum cost).
	 */
	Resources getCostForOptimalPath() {
		return mostAttackablePathCost;
	}

	public void transferMultipathResultsFromCrawlable(CrawlableGraphNode crawlable) {
		mostAttackablePathAffordability   = crawlable.getMostAttackablePathAffordability();
		mostAttackablePathVulnerability   = crawlable.getMostAttackablePathVulnerability();
		mostAttackablePathAttackability   = crawlable.getMostAttackablePathAttackability();
		mostAttackablePathCost            = crawlable.getMostAttackablePathCost();
		recordedExpansionsResources       = crawlable.getRecordedExpansionsResources();
		recordedExpansionVulnerabilityAcc = crawlable.getRecordedExpansionsVulnerabilityAccumulator();
		recordedPathExpansionsNumber      = crawlable.getRecordedExpansionsNumber();
	}

	public Resources getCostExpectedValueForSuccessfulPathsCombined() {
		return new Resources(recordedExpansionsResources.divideBy(recordedPathExpansionsNumber));
	}

	public double getVulnerabilityProbabilityMeanForPathsThroughHere() {
		return recordedExpansionVulnerabilityAcc / recordedPathExpansionsNumber;
	}

	public int getNumberOfRecordedPathExpansionsHere() {
		return recordedPathExpansionsNumber;
	}
}