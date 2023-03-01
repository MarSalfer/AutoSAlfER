package analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import analysis.multipath.salfer.Path;
import analysis.multipath.salfer.PoincareSylvester;
import attackGraph.AttackScenario;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Exploitable;
import systemModel.Software;
import types.Resources;


/**
 * Crawlable - Wrapper class for system model nodes enriched with graph generator work data.
 * 
 * @author Martin Salfer
 *
 */
public abstract class Crawlable implements Comparable<Crawlable>, CrawlableInterface {

	/**
	 * Creation number counter. Later used for mapping Crawlable to ProbabilisticNode objects.
	 */
	private static int CREATION_NUMBER = 0;

	protected final AttackScenario scenario;
	
	/**
	 * Storage for all known attack vectors onto this Crawlable. Using a sorted
	 * list and having peek() and insert() as fast as possible.
	 */
	@Deprecated
	private final HashMap<CrawledAttackVector, Double> attackVectorsAndCost = new HashMap<CrawledAttackVector, Double>(3);

	private boolean isClosed = false;

	/**
	 * The creation number tells distinctively which node this is.
	 * This is used for mapping a Crawlable to a ProbabilisticNode object.
	 */
	public final int creationNumber;

	private final Map<CrawlableInterface, Map<Exploit, Double>> attackOrigins = new HashMap<CrawlableInterface, Map<Exploit, Double>>();
	
	/**
	 * @deprecated use mostAttackablePath instead.
	 */
	@Deprecated
	private Resources cheapestAttackPathCostTilHere = new Resources(Double.POSITIVE_INFINITY, 0d);

	private Resources mostAttackablePathCost = new Resources(Double.POSITIVE_INFINITY, 0d);
	private double mostAttackablePathAffordability = 0d; // start with impossible.
	private double mostAttackablePathVulnerability = 0d; // start with impossible.
	private double mostAttackablePathAttackability = 0d; // start with impossible.
	/**
	 * @return the cheapestAttackPathCostTilHere
	 * @deprecated use getMostAttackablePathCost() instead
	 */
	@Deprecated
	public Resources getCheapestAttackPathCostTilHere() {
		return cheapestAttackPathCostTilHere;
	}

	/**
	 * @param cheapestAttackPathCostTilHere the cheapestAttackPathCostTilHere to set
	 */
	public void setMinCostTilHere(Resources cheapestAttackPathCostTilHere) {
		assert cheapestAttackPathCostTilHere != null;
		assert cheapestAttackPathCostTilHere.getMoneyExpectedValue() <= this.cheapestAttackPathCostTilHere.getMoneyExpectedValue();
		this.cheapestAttackPathCostTilHere = cheapestAttackPathCostTilHere;
	}
	
	/**
	 * @return the cost of the cheapest attack vector. In case of none, it returns positive infinity.
	 */
	public double getMinCostMeanTilHere() {
		return cheapestAttackPathCostTilHere.getMoneyExpectedValue();
	}

	@Deprecated
	private CrawlableInterface cheapestAttackVectorOrigin;
	
	@Deprecated
	private Exploit cheapestAttackVectorExploit;

	@Deprecated
	private double cheapestPathVulnProb;
	
	@Deprecated
	private double overallVulnerablePathsProb = 0d; // start with impossible.

	@Deprecated
	private double overallAffordabilityProb = 0d; // start with impossible.

	@Deprecated
	private double overallAttackabilityProb = 0d; // start with impossible. 

	/**
	 * Accumulator for all resources of successful paths that traverse through here.
	 */
	private Resources recordedExpansionsResources = new Resources(0d, 0d);
	private double    recordedExpansionsVulnerabilityAccumulator;
	private int       recordedExpansionsNumber = 0;
	
	public Crawlable (AttackScenario scenario) {
		this.scenario = scenario;
		creationNumber = Crawlable.CREATION_NUMBER ++;
	}
	
	@Deprecated
	public Crawlable (AttackScenario scenario, float cost) {
		this(scenario);
	}
	
	public abstract Software getSoftware();

	public abstract int getHopDistanceToAttractors();

	public abstract Set<Software> getDirectlyReachableSoftwareNodes();

	public abstract Set<Software> getIndirectlyReachableSoftwareNodes();

	public abstract boolean hasAttractors();

	public abstract Set<Asset> getAttractors();

	public abstract boolean isAttractor();

	public abstract Asset getAsset();

	/**
	 * Compare according to the A* / heuristics.
	 */
	@Override
	public int compareTo(Crawlable cs) {
		if (cs == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return (int) (scenario.costPredicted(this) - scenario.costPredicted(cs));  // TODO mit der getCostCapitalH-Methode von unten ersetzen.
	}

	/**
	 * Complexity: O(1) // but the Map was added! Maybe Recheck required!
	 * @param origin
	 * @param exploit
	 * @param newPathCostToHere
	 */
	public void addNewExploitOrigin(CrawlableInterface origin, Exploit exploit, double newPathCostToHere) {
		// Input validation.
		if (origin == this) throw new IllegalArgumentException("An AttackVector must not point to something else!");
		if (exploit == null) throw new IllegalArgumentException("An Exploit must be given for an attack vector.");
		if (newPathCostToHere < 0) throw new IllegalArgumentException("Costs must be at lest zero.");

		// Create Exploit Set if necessary
		final Map<Exploit, Double> exploits = attackOrigins.get(origin); 
		if (exploits == null) { // no exploits from this origin, yet.
			final HashMap<Exploit, Double> exploitSet = new HashMap<Exploit, Double>();
			attackOrigins.put(origin, exploitSet);
		}
		
		// Create entry if possible
		final Double exploitCost = attackOrigins.get(origin).get(exploit);
		if (exploitCost == null) {
			attackOrigins.get(origin).put(exploit, (Double) newPathCostToHere);
		} else {
			throw new IllegalArgumentException("Duplicates assignment is not necessary for Martins network construction!");
		}
		if (newPathCostToHere < getMinCostMeanTilHere()) {
			cheapestAttackPathCostTilHere = new Resources(newPathCostToHere, Double.NaN);
			cheapestAttackVectorOrigin = origin;
			cheapestAttackVectorExploit = exploit;
		}
	}

	/**
	 * @return The predecessor of the cheapest attack vector.
	 */
	public CrawlableInterface getAttackPredecessor() {
		return cheapestAttackVectorOrigin;
	}

	/**
	 * @return the exploit used in the cheapest attack vector.
	 */
	public Exploit getUsedExploit() {
		return cheapestAttackVectorExploit;
	}

	/**
	 * Query for exploitable.
	 */
	public abstract Exploitable getExploitable();

	
	/**
	 * Query if this node is already closed by the search algorithm, i.e. has been already expanded.
	 * 
	 * Complexity: O(1)
	 * @return true, if this node has been already expanded.
	 */
	public boolean isClosed() {
		return isClosed;
	}
	
	/**
	 * Close a Crawlable, i.e. mark it as expanded.
	 * Note: There is no way to reset or open a Crawlable again. It's only used once.
	 */
	public void close() {
		isClosed = true;
	}

	
	/**
	 * Get all attached Communication Medium nodes.
	 * This method is overloaded CrawledSoftware.
	 * @return null.
	 * @deprecated Consider the attack direction with {@link #getAttackableComMedia()}.
	 */
	public Set<CommunicationMedium> getReachableComMedia() {
		return null;
	}
	
	/**
	 * Gett attach Communicaton Media someone can write to.
	 * @return null
	 */
	public Set<CommunicationMedium> getAttackableComMedia() {
		return null;
	}
	
	
	/**
	 * Getter
	 * @return Cost for reaching this node so far + the heuristically predicted cost.
	 */
	public double getCostCapitalH() {
		return scenario.costPredicted(this);
	}

	
	/**
	 * @return all exploits that this Crawable can launch against the target.
	 * @param target
	 */
	public abstract Set<Exploit> getAllExploitsAgainstTarget(CrawlableInterface target);

	/**
	 * @return all attack vectors this Crawlable has already been found with.
	 */
	@Deprecated
	public Map<CrawledAttackVector, Double> getAttackVectorsAndCosts() {
		return Collections.unmodifiableMap(attackVectorsAndCost);
	}



	/**
	 * @return
	 */
	public abstract String getName();


	/**
	 * @param crawlable
	 * @return true if crawlable is a registered attack predecessor.
	 */
	public boolean hasPredecessor(CrawlableInterface crawlable) {
		return attackOrigins.containsKey(crawlable);
	}
	
	public int getCreationNumber() {
		return creationNumber;
	}

	/* (non-Javadoc)
	 * @see analysis.CrawlableInterface#getAttackOrigins()
	 */
	public Set<CrawlableInterface> getAttackOrigins() {
		return Collections.unmodifiableSet(attackOrigins.keySet());
	}


	/**
	 * Get the exploitation likelihood against this crawlable coming from another crawlable. 
	 * @see analysis.CrawlableInterface#getExploitationLikelihoodFrom(analysis.CrawlableInterface)
	 */
	public double getExploitationLikelihoodFrom(CrawlableInterface origin) { // TODO
//		Set<Exploit> usedExploits = attackOrigins.get(origin).keySet();
		
		// get costs from the whole path so far (as resources, with proper variance)
		// hard, as many pathes are possible and several have to be unified again.
		
		// get the budget of the attacker
//		for (Exploit)
		// TODO Auto-generated method stub
//		if (origin instanceof CrawledAccess) return 0.1d;
//		else 
		return 0.1d;
	}

	/**
	 * Count a Path.
	 * @param path - The Path that is to be counted.
	 */
	public void recordAttackabilityWith(Path path) {
		final Resources pathCost = path.getCostSoFar();
		recordedExpansionsResources = recordedExpansionsResources.addWith(pathCost);
		recordedExpansionsVulnerabilityAccumulator += path.getVulnerabilityProbability();
		recordedExpansionsNumber++;
		
		// Fusion the results for an overall security evaluation.
		final double pathAttackability = path.getAttackabilityProbability();
		if (pathAttackability > mostAttackablePathAttackability) {
			// Found more attackable path to here. Record that.
			mostAttackablePathAttackability = pathAttackability;
			mostAttackablePathVulnerability = path.getVulnerabilityProbability();
			mostAttackablePathAffordability = path.getAffordabilityProbability();
			mostAttackablePathCost          = pathCost;
		}
		// Poincaré-Sylvester was removed due to unnecessary results.
//		overallAffordabilityProb   = PoincareSylvester.combine2Inputs(overallAffordabilityProb, pathAffordability);
//		overallAttackabilityProb   = PoincareSylvester.combine2Inputs(overallAttackabilityProb, pathAttackability);
//		overallVulnerablePathsProb = PoincareSylvester.combine2Inputs(overallVulnerablePathsProb, path.getVulnerabilityLikelihood());
	}

	/**
	 * @deprecated use most attackablePath instead.
	 */
	@Deprecated
	private void checkAndRecordCheapestPathHit(Path path) {
		// Check whether it is the cheapest attack path so far.
		if (path.getCostSoFar().getMoneyExpectedValue() < getCheapestAttackPathCostTilHere().getMoneyExpectedValue()) {
			cheapestAttackPathCostTilHere = path.getCostSoFar();
			cheapestPathVulnProb = path.getVulnerabilityProbability();
		}
	}


	public Resources getRecordedExpansionsResources() {
		return recordedExpansionsResources;
	}

	public int getRecordedExpansionsNumber() {
		return recordedExpansionsNumber;
	}

	public Resources getMostAttackablePathCost() {
		return mostAttackablePathCost;
	}
	
	public double getMostAttackablePathAffordability() {
		return mostAttackablePathAffordability;
	}
	
	public double getMostAttackablePathAttackability() {
		return mostAttackablePathAttackability;
	}
	
	public double getMostAttackablePathVulnerability() {
		return mostAttackablePathVulnerability;
	}

	public double getRecordedExpansionsVulnerabilityAccumulator() {
		return recordedExpansionsVulnerabilityAccumulator;
	}
	
}