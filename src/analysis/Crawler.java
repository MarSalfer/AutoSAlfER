/**
 * 
 */
package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import analysis.comparator.PathBiggestFirstComparator;
import analysis.multipath.bayes.CrawledAccessP3;
import analysis.multipath.bayes.CrawledAssetP3;
import analysis.multipath.bayes.CrawledSoftwareP3;
import analysis.multipath.salfer.Path;
import attackGraph.AttackGraph;
import attackGraph.AttackGraphNode;
import attackGraph.AttackScenario;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.CommunicationMedium;
import systemModel.Exploitable;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;
import unbbayes.prs.Edge;
import unbbayes.prs.bn.PotentialTable;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.prs.exception.InvalidParentException;

/**
 * @author Martin Salfer
 * 2013-11-25 Original base version
 * 2016-04-07 Extension by Phase3-Expansion.
 */
public class Crawler {

	/**
	 * How often the Garbage Collector will be called prior a memory measurement.
	 */
	private static final int GC_ITERATIONS = 0;
	private static final String FUNNEL_NODE_NAME = "Trichterknoten(";
	private final AttackScenario scenario;
	private final Set<Asset> openAttractors = new HashSet<Asset>();
	
	/**
	 * Phase I: Queue of Software nodes that still need to be expanded.
	 */
	private final PriorityQueue<Software> openSoftwareListPI = new PriorityQueue<Software>();
	
	/**
	 * Phase II: Queue of Software nodes, Assets, Accesses AND Communication Media.
	 */
	private final PriorityQueue<Crawlable> openList = new PriorityQueue<Crawlable>();
	
	/**
	 * Phase III-Bayes: Queue of Software nodes, Assets and Accesses, but no Communication Media.
	 */
	private final PriorityQueue<CrawlableInterface> openListP3Bayes = new PriorityQueue<CrawlableInterface>();

	/**
	 * Phase III-Salfer: Queue of Path objects (positioned on software nodes, assets and accesses, but no com media.
	 * Sorted by the biggest Path object first as those closer to destruction (and free memory).
	 */
	private final PriorityQueue<Path> openListPath = new PriorityQueue<Path>(new PathBiggestFirstComparator());
	
	private final AttackGraph attackGraph;
	private final AttackGraph attackGraphMultiPathBayes;
	private final AttackGraph attackGraphMultiPathSalfer;
	private final HashMap<Exploitable,CrawlableGraphNode> exploitable2Cs = new HashMap<Exploitable,CrawlableGraphNode>();
	private final HashMap<Exploitable,CrawlableGraphNode> exploitable2CsP3 = new HashMap<Exploitable,CrawlableGraphNode>();
	private boolean attackGraphP2Done = false;
	private boolean attackGraphMultiPathBayesDone = false;
	private Resources p3SalferAllPathsCostAccumulated = new Resources(0d, 0d);
	private double    p3SalferAllPathsVulnerabilityProbabilityAccumulated = 0d;
	private double    p3SalferAllPathsAttackabilityProbabilityAccumulated = 0d;
	private int       p3SalferAllPathsNumber = 0;
	/**
	 * Nanoseconds the crawled needed to find and create this graph.
	 */
	private long crawlerRunTimeNano;
	/**
	 * Collector for memory statistics.
	 */
	private SummaryStatistics memStatistics;
	
	private static int probabilisticNodeFunnelCounter = 0;
	public static final float ALWAYS = 1f;
	public static final float IMPOSSIBLE = 0f;
	private static final int LOG_LEVEL = 0;

	@Deprecated
	public Crawler(SystemModel sm, AttackerProfile ap, Set<Exploit> ex) {
		this(new AttackScenario("Crawler Attack Scenario", sm, ap, ex));
	}

	public Crawler(AttackScenario scenario) {
		this.scenario = scenario;
		attackGraph = new AttackGraph("CrawledAttackGraph", scenario);
		attackGraphMultiPathBayes = new AttackGraph("CrawledAttackGraphMultiPathBayes", scenario);
		attackGraphMultiPathSalfer = new AttackGraph("CrawledAttackGraphMultiPathSalfer", scenario);
	}

	/**
	 * Asymptotic Complexity: O(S*(log(S)+E)
	 * S: Number of software objects.
	 * E: Number of exploit objects
	 * Asymptotic Complexity assumptions:
	 * 1. Constant number of Capability objects per Software object.
	 * 2. Constant number of Capability objects per Communication Medium node.
	 * 3. Constant number of Communication Medium nodes per Capability node.
	 * 4. Constant number of Software nodes per Capability node.
	 * 5. Constant number of Attacker Profiles, Attractors and Access nodes as those are created manually.
	 * 6. Constant number of Software targets at Exploit objects.
	 * 7. Constant number of Capability objects at Exploit objects.
	 * 8. Constant number of Communication Medium node requirements at Exploit objects.
	 * 
	 */
	public synchronized AttackGraph crawlAndGetAttackForest() {
		if (attackGraphP2Done == false) {
			prepareCrawl();  // reset and prepare. O(1)
			runPhase1Crawl(); // run graph and annotate hop count. // Complexity: ? - O(S*(log(S)+E).
			runPhase2Crawl(); // A*-traverse graph with hop count. // Complexity: O(S*(log(S)+E)
			attackGraphP2Done = true;
		}
		return attackGraph;
	}

	/**
	 * Run Phase III for getting an attack graph based on Bayes networks.
	 * @return
	 */
	public AttackGraph crawlAndGetAttackGraphOnBayes() {
		if (attackGraphMultiPathBayesDone  == false) {
			resetBenchmarkStatistics();
			long start = System.nanoTime();
			runPhase3CrawlBayes(); // Breadth-first traversal for Bayes network construction.
			long stop = System.nanoTime();
			crawlerRunTimeNano = stop - start;
			attackGraphMultiPathBayesDone = true;
		}
		return attackGraphMultiPathBayes;
	}

	/**
	 * Run Phase III for getting an attack graph based on Salfer networks.
	 * @param memoryMeasurment Shall a memory measurement be taken (consumes cpu power).
	 * @return
	 */
	public AttackGraph crawlAndGetAttackGraphOnSalfer(boolean memoryMeasurment) {
		attackGraphMultiPathSalfer.reset();
		clearLists();
		resetBenchmarkStatistics();
		long start = System.nanoTime();
		runPhase3CrawlSalfer(memoryMeasurment); // Heuristic traversal for Salfer network construction.
		long stop = System.nanoTime();
		crawlerRunTimeNano = stop - start;
		return attackGraphMultiPathSalfer;
	}
	
	public AttackGraph getAttackGraph() {
		return attackGraph;
	}

	public AttackGraph getAttackGraphP3Salfer() {
		return attackGraphMultiPathSalfer;
	}

	/** 
	 * Zero the attack graph and the lists.
	 * Complexity: Theta(1).
	 */
	private void prepareCrawl() {
		attackGraph.reset();
		clearLists();
	}

	/**
	 * Complexity: Theta(1)
	 */
	private void clearLists() {
		openList.clear();
		openSoftwareListPI.clear();
		openListP3Bayes.clear();
		openListPath.clear();
		openAttractors.clear();
	}

	private void resetBenchmarkStatistics() {
		memStatistics = new SummaryStatistics();
		crawlerRunTimeNano = 0;
	}

	/**
	 * Run the first phase of the system model crawl.
	 * 
	 * This phase annotates all nodes breadth-first
	 * with distance information for the heuristic.
	 * Complexity: ?  O(S^2) TODO simplify to O(S*log(S))
	 * TODO: Do not terminate before having visited all system nodes.
	 */
	void runPhase1Crawl() {
		clearLists(); // Complexity: Theta(1)
		loadAttractorSoftwareNodesIntoOpenSoftwareList(); // Complexity: O(|A|*|S|)
		while (openSoftwareListPI.size() > 0) { // size: O(1); loop: O(S) as every loop will convert one s of S to the closed list.
			Software expandingSw = openSoftwareListPI.poll(); // Complexity: O(log|S|)
			assert expandingSw != null;
			expandingSw.closeNodeForPhaseI(); // O(1)
			expandSoftwareNode(expandingSw); // Complexity: ? O(|S|)
		}
		clearLists(); // Complexity: O(1)
	}

	/**
	 * Expand a Software node in Phase I for heuristics preparation.
	 * Complexity: ? O(|S|)
	 * @param expandingSw The Software node to be expanded.
	 * @return 
	 */
	private void expandSoftwareNode(Software expandingSw) {
		expandingSw.initSurroundingComMediaDistance();
		Set<Software> reachableSoftware = expandingSw.getReachableSoftwareOverOpenComNodes(); // Complexity: O(1) as of 1,2,3,4.
		expandingSw.closeAttachedComMediaForPhaseI();
		for (Software t : reachableSoftware) { // Complexity: O(1) as of 1,2,3,4.
			if (t.isPhaseIClosed())	
				continue; // Closed Task cannot be reached earlier any more. The priority queue ordering ensures having to shortest path as soon as a task is closed..
			if (openSoftwareListPI.contains(t)) { // Complexity: O(|S|) TODO simplify to O(1).
				// Previously known software gets rechecked. Will change distance by 1 at max.
				checkAndUpdateTaskAnnotation(expandingSw, t); // Complexity: O(log|S|)
				continue;
			}
			else {
				// Newly found Task.
				t.setHopDistanceToAssets(expandingSw.getHopsToAssets() + 1);
				openSoftwareListPI.add(t); // Complexity: O(log|S|)
				continue;
			}
		}
	}

	/**
	 * Complexity: O(log|S|).
	 * @param previousNode
	 * @param cs
	 */
	private void checkAndUpdateTaskAnnotation(Software previousNode, Software cs) {
		int newHopDistance = previousNode.getHopsToAssets() + 1;
		if (newHopDistance < cs.getHopsToAssets()) {
			cs.setHopDistanceToAssets(newHopDistance);
			openSoftwareListPI.add(cs); // make others recheck, too.
//			cs.setSearchPrecessorNode(previousNode);
		}
	}

//	// TODO umschreiben auf pull()-Operation der Queue.
//	private void moveToCloseList(Crawlable cs) {
//		if (openList.contains(cs) && !closedList.contains(cs)) {
//			closedList.add(cs);
//			openList.remove(cs);
//		}
//	}
//
//	private Crawlable chooseAnyOpenNode() {
//		return openList.element();  // TODO: Possible performance increase: choose best node, instead of first.
//	}

	/**
	 * Complexity: O(|A|*|S|).
	 */
	private void loadAttractorSoftwareNodesIntoOpenSoftwareList() {
		for (Asset a : scenario.getSystemModel().getAttractors()) { // Complexity: O(|S|)
			Software t = a.getParentTask();
			t.setHopDistanceToAssets(1);  // This is the last step to an Asset => distance 1.
			assert t != null;
			if (!openSoftwareListPI.contains(t) && !t.isPhaseIClosed()) // Complexity: O(|S|)
				openSoftwareListPI.add(t); // Complexity: O(log(|S|)).
		}
	}

	/**
	 * Run the second phase of the system model crawl.
	 * This phase finds the shortest path and acts depth-first.
	 * The heuristic must be consistent, as nodes are expanded
	 * only once.
	 * Complexity: O(S*(log(S)+E).
	 */
	private void runPhase2Crawl() {
		clearLists(); // Complexity: O(1)
		loadAccessNodesIntoOpenList(); // Complexity O(1) as of assumption 5.
		loadOpenAttractors(); // Complexity O(1) as of assumption 5.
		while (openAttractors.size() > 0 && openList.size() > 0) { // Complexity: O(S) as a software node is closed each round.
			Crawlable c = openList.poll(); // Complexity: O(logS).
			c.close(); // Complexity: O(1).
//			System.out.println("Expandcost: " + c.getCostCapitalH() + "Expand: " + c);
			expand(c); // Complexity: O(log(S)+E) as of *
			checkAssertions(); // Complexity O(1) when in non-debug/assertion-mode.
		}
		clearLists(); // Complexity: O(1)
	}

	/**
	 * Run the third phase of the system model crawl.
	 * This phase find many pathes and acts breadth-first for constructing a Bayes network.
	 */
	private void runPhase3CrawlBayes() {
		clearLists(); // Complexity: Theta(1)
		loadAccessNodesIntoOpenListP3(); // Complexity O(1) as of assumption 5.
		while (openListP3Bayes.size() > 0) { // TODO: still correct? size: O(1); loop: O(S) as every loop will convert one s of S to the closed list.
			CrawlableInterface c = openListP3Bayes.poll(); // Complexity: O(log|S|)
			assert c != null;
			assert c.getMinCostMeanTilHere() <= scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma();  
			expandP3Bayes(c);
			c.close(); // O(1)
		}
		clearLists(); // Complexity: O(1)
	}

	/**
	 * Run the third phase of the system model crawl.
	 * This phase find many pathes for constructing a multipath attack graph.
	 * @param withMemMeasurement true for measuring the memory usage. (Beware, consumes CPU time.)
	 */
	private void runPhase3CrawlSalfer(boolean withMemMeasurement) {
		loadAccessNodesIntoOpenListAsPaths(); // Complexity O(1) as of assumption 5???
		while (!openListPath.isEmpty()) { // size: O(1); loop: O(S) as every loop will convert one s of S to the closed list???
			Path path = openListPath.poll(); // Complexity: O(log|S|)???
			assert path != null;
			assert path.getCostSoFar().getMoneyExpectedValue() <= scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma();  
			expandP3Salfer(path);
			if (withMemMeasurement) {
//				for (int i = 1; i <= Crawler.GC_ITERATIONS; i++) {
//					System.gc();
//				}
				final long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				memStatistics.addValue(usedMemory);
			}
		}
		attackGraphMultiPathSalfer.importP3SalferResults(this);
	}

	/**
	 * @param nodeAttackGraph
	 * @return
	 */
	public CrawlableGraphNode getCrawlableGraphNodeForExploitable(AttackGraphNode nodeAttackGraph) {
		return exploitable2Cs.get(nodeAttackGraph.getSysNode());
	}

	/**
	 * Load access nodes into the crawler's open list for phase 3 salfer.
	 */
	private void loadAccessNodesIntoOpenListAsPaths() {
		for (Access access : scenario.m_AttackerProfile.getAccesses()) { // Complexity: O(1) as of assumption 5???
			final CrawledAccess crawlable = new CrawledAccess(scenario, access);
			ArrayList<CrawlableGraphNode> visitedNodes = new ArrayList<CrawlableGraphNode>();
			visitedNodes.add(crawlable);
			openListPath.add(new Path(new Resources(), 1d, visitedNodes, new ArrayList<Exploit>())); // Complexity: O(1) as list is previously empty. ???
		}
	}
	
	/**
	 * Expand a Software node in Phase III Salfer.
	 * @param origin The Software node to be expanded.
	 * @return 
	 */
	private void expandP3Salfer(Path path) {
		Crawlable origin = path.getPosition(); // Changing the static type to CrawlableInterface causes an interesting method binding issue in AttackScenario.chooseCheapest... .
		if (origin.isAttractor()) {
			if (path.isCheaperAttackAgainst(origin)) {
				((CrawledAsset)origin).setAttackPathAndUsedExploits(path);
			}
			// In Angriffsgraph exportieren.
			attackGraphMultiPathSalfer.exportPathIntoAttackGraph(path);
			recordPathCostsInCrawlerAccumulator(path); // path was successful and must be recorded, too.
			return; // Assets are the last step of an attack path. No more collection and evaluation necessary for this Path.
		}

		boolean pathContinues = false;
		// Collect targetable Attractors.
		if (origin.hasAttractors()) { 
			for (Asset a : origin.getAttractors()) {
				pathContinues |= expandP3SalferEvaluate(path, origin, Exploitable2CSAndRegistration(a), false);
			}  
		}

		// Collect targetable Software nodes, convert and evaluate.
		Iterator<Software> it = origin.getIndirectlyReachableSoftwareNodes().iterator();
		while (it.hasNext()) {
			pathContinues |= expandP3SalferEvaluate(path, origin, Exploitable2CSAndRegistration(it.next()), !it.hasNext());
		}
		if (pathContinues == false) {
			// path is considered failed and finished and thus must be recorded.
			recordPathCostsInCrawlerAccumulator(path);
		}
	}

	/**
	 * @param path
	 */
	private void recordPathCostsInCrawlerAccumulator(Path path) {
		p3SalferAllPathsCostAccumulated = p3SalferAllPathsCostAccumulated.addWith(path.getCostSoFar());
		p3SalferAllPathsVulnerabilityProbabilityAccumulated += path.getVulnerabilityProbability();
		p3SalferAllPathsNumber++;
	}


	/**
	 * 
	 * @param path
	 * @param origin
	 * @param target
	 * @param reuse
	 * @returns true if the path goes on. False means that no expansion was successful here.
	 */
	private boolean expandP3SalferEvaluate(Path path, Crawlable origin, final CrawlableGraphNode target, boolean reuse) {
		if (origin == target) {
			return false; // special case: Do not try to exploit yourself.
		}
		if (path.hasVisited(target)) {
			return false;  // No cycles within a path allowed as a real attack would not need to do this.
		}
		Exploit e = scenario.chooseCheapestExploit(origin, target);
		if (e == null) {
			return false; // no exploit available for target.
		}
		final Resources exploitCost = scenario.cost(e);
		final double totalPathCostTilTarget = path.getCostSoFar().addWith(exploitCost).getMoneyExpectedValue();  // Complexity: O(1) as of 5
		if (totalPathCostTilTarget <= scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma()) { // Test if attack can afford this step.
			// Exploit is affordable and must be considered.
			final Path cloneAndJumpTo;
			if (reuse) {
				// path can be reused, no more clones necessary.
				cloneAndJumpTo = path.jumpTo(target, e, exploitCost, scenario.getAttackerProfile().getResources());
			} else {
				// path clone necessary.
				cloneAndJumpTo = path.cloneAndJumpTo(target, e, exploitCost, scenario.getAttackerProfile().getResources());
			}
			if (cloneAndJumpTo != null) {
				openListPath.add(cloneAndJumpTo); // Prioritiy Queue cannot handle null input.
				return true; // the expansion was successful and the path will continue.
			}
		}
		return false;
	}

	/**
	 * Expand a Software node in Phase III.
	 * @param origin The Software node to be expanded.
	 * @return 
	 */
	private void expandP3Bayes(CrawlableInterface origin) {
		if (origin.isAttractor()) {
			// In Angriffsgraph exportieren.
			exportPathIntoAttackGraph((CrawledAsset) origin);
			exportProbabilisticNetworkForAssetBayes((CrawledAsset) origin);
		}
		// Collect targetable Software nodes.
		final Set<Exploitable> reachableExploitables = new HashSet<Exploitable>();
		reachableExploitables.addAll(origin.getDirectlyReachableSoftwareNodes()); // Complexity: O(1) as of 1,2,3,4. // TODO still?

		// Collect targetable Attractors.
		if (origin.hasAttractors()) { // Complexity: O(1) as of 5. TODO still?
			reachableExploitables.addAll(origin.getAttractors()); // Complexity: O(1) as of 5 TODO still? 
		}
		
		// Convert to CrawlableP3
		final Set<CrawlableInterface> crawlables = new HashSet<CrawlableInterface>(batchSw2Csp3AndRegistration(reachableExploitables)); // Complexity: O(1) as of 1,2,3,4,5.

		// Evaluate all targetable exploitables.
		for (CrawlableInterface target : crawlables) { // Complexity: O(1) as of 1,2,3,4,5.
			if (origin == target) {
				continue; // special case: Do not try to exploit yourself.
			}
//			if (target.isClosed()) // Testing closed nodes is necessary as several paths should be found.
			if (origin.hasPredecessor(target)) {
				continue;  // No backward edges allowed on Bayesian networks!
			}
			for (Exploit e: origin.getAllExploitsAgainstTarget(target)) { // Complexity presumably O(E) as of 1,2,3,4,6,7,8
				final double totalPathCostTilTarget = origin.getMinCostMeanTilHere() + scenario.cost(e).getMoneyExpectedValue();  // Complexity: O(1) as of 5
				if (totalPathCostTilTarget > scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma()) // Test if attack can afford this step.
					continue; // Exploit is way too expensive for attacker and hence not affordable.
				else { // Exploit is affordable and must be considered.
					// Success, put onto openList. Test whether a better path has been found.
					final boolean openListNotificationNecessary = totalPathCostTilTarget < scenario.costInvestedSoFar(target); // OpenList needs to be notified about a new low cost possibility.
					target.addNewExploitOrigin(origin, e, totalPathCostTilTarget); 
					if (openListNotificationNecessary) {
						// hit: found a cheaper way to approach a task. works with new and previously known CSes.
						openListP3Bayes.remove(target); // will fail silently at unknown objects, good for handling also new CS objects.
						openListP3Bayes.add(target); // re-add for updated cost comparison. // Complexity: O(logS) as of *.
					}
				}
			}
		}
	}


	/**
	 * Expand a crawlable node.
	 * Complexity: O(log(S)+E) as of *.
	 * @param c
	 */
	private void expand(Crawlable c) {
		if (c.isAttractor())  {
			openAttractors.remove(c.getAsset()); // Complexity: O(1) as of 5.
			// In Graphen exportieren.
				exportPathIntoAttackGraph((CrawledAsset) c); // Complexity: TODO
		} else {
			expandCrawlable(c); // Complexity: O(log(S)+E) as of *
		}
	}

	/**
	 * @param hitAsset
	 */
	private void exportProbabilisticNetworkForAssetBayes(CrawledAsset hitAsset) {
		// Input validation
		if (hitAsset == null) throw new IllegalArgumentException();
		
		// Preparation
		ProbabilisticNetwork net = new ProbabilisticNetwork("Probabilistic Network for Asset " + hitAsset.getAsset().getName() + ".");
		attackGraphMultiPathBayes.addProbabilisticNetworkForAsset(net, hitAsset.getAsset());
		LinkedList<CrawlableInterface> crawlablesWorkingList = new LinkedList<CrawlableInterface>();
		crawlablesWorkingList.add(hitAsset);
		Set <CrawlableInterface> closedCrawlables = new HashSet<CrawlableInterface>();
		closedCrawlables.add(hitAsset);

		/* Iteration over all involved nodes and export into the probabilistic network. */
		while (crawlablesWorkingList.size() > 0) {
			ListIterator<CrawlableInterface> it = crawlablesWorkingList.listIterator();
			while (it.hasNext()) {
				CrawlableInterface c = it.next();
				it.remove();
				ProbabilisticNode pNode = getOrCreateProbabilisticNodeBayes(net, c);
				
				// Create the predecessor nodes.
				Set<CrawlableInterface> origins = c.getAttackOrigins();
				Map<ProbabilisticNode, Double> predecessorsForFunnel = new HashMap<ProbabilisticNode, Double>();
				for (CrawlableInterface origin : origins) {
					final ProbabilisticNode inputNode = getOrCreateProbabilisticNodeBayes(net, origin);
					predecessorsForFunnel.put(inputNode, c.getExploitationLikelihoodFrom(origin));	// Likelihoods for the precedessors
					if (!closedCrawlables.contains(origin)) {
						it.add(origin);
						closedCrawlables.add(origin);
					}
				}
				
				/* Interconnect with the funnel algorithm */
				// TODO evtl. für nur einen Eingang den Trichter entfallen lassen und gleich hier abfangen anstatt im Trichteralgo.
				if (predecessorsForFunnel.size() >= 1) {
					// still predecessors existing, so connect them.
					ProbabilisticNode funnelTip = funnelToOneNodeBayes(net, predecessorsForFunnel); // works also for only 1 node.
					try {
						net.addEdge(new Edge(funnelTip, pNode));
					} catch (InvalidParentException e) {
						e.printStackTrace();
						throw new IllegalArgumentException("The linking between " + funnelTip + " and " + pNode + " fails. Please debug.");
					}
				}
				PotentialTable table = pNode.getProbabilityFunction();
//				double vulnProb = c.getVulnerabilityLikelihood();
				double vulnProb = 1d; // TODO exchange against useful method; something like in the line above.
				double exploitationProb = 1d; // default value in case of a funnel being connected.
				if (predecessorsForFunnel.size() == 1) { // was a funnel necessary, i.e. was a funnel built?
					// only one means a funnel was not built.
					exploitationProb = predecessorsForFunnel.values().iterator().next(); // Must be a crawlable node probability value.
				}
				table.setValue(0, (float) (vulnProb*exploitationProb));
				table.setValue(1, (float) (1d - vulnProb*exploitationProb));
				if (predecessorsForFunnel.size() > 0) {
					// apparently we have predecessors.
					table.setValue(2, IMPOSSIBLE); // never hacked, as predecessor is unhacked.
					table.setValue(3, ALWAYS); // always intact, as predecessor is unhacked.
				}
				if (net.hasCycle()) {
					System.out.println("Found a cycle!");
				}
				// TODO Vektoren vereinheitlichen.
				// TODO Eingangsknoten müssen noch die Exploitwahrscheinlichkeit richtig kriegen! (Die für die Vektoren)
			}
		}
	}

	/**
	 * Gets an according ProbabiliticNode for a Crawlable, and creates one if necessary.
	 * @param net
	 * @param crawlable
	 * @return
	 */
	private ProbabilisticNode getOrCreateProbabilisticNodeBayes(ProbabilisticNetwork net, final CrawlableInterface crawlable) {
		ProbabilisticNode probabilisticNode = (ProbabilisticNode) net.getNode(getNameForProbabilisticNetwork(crawlable)); // cast is allowed as only Probabilistic Nodes will be inserted.
		if (probabilisticNode == null) {
			probabilisticNode = createProbabilisticNodeBayes(crawlable);
			net.addNode(probabilisticNode);
		}
		return probabilisticNode;
	}

	/**
	 * @param c
	 */
	private ProbabilisticNode createProbabilisticNodeBayes(CrawlableInterface c) {
		/* Create a probabilistic node out of the Crawlable. */
		ProbabilisticNode node = new ProbabilisticNode();
		node.setName(getNameForProbabilisticNetwork(c));
//		node.setDescription(c + " gets compromised.");
		node.appendState("Compromised");
		node.appendState("Intact");
		PotentialTable table = node.getProbabilityFunction();
		table.addVariable(node);
		return node;
	}

	/**
	 * Get the name as the ProbabilisticNetwork would see it.
	 * Needs to be stable for naming nodes initially and also referring to them later.
	 * @param c
	 * @return
	 */
	private String getNameForProbabilisticNetwork(CrawlableInterface c) {
		return c.getName() + "(" + c.getCreationNumber() + ")";
	}
	

	/**
	 * Generate the funnel.
	 * The funnel avoids a distribution table explosion by
	 * distributing all input nodes over a set of generated
	 * intermediary nodes and slowly funneling to a result.
	 * The funnel is neutral to the network. It is a pure yes/no 
	 * decision tree. If any of the inputNode is positive
	 * (here means being 'compromised', the funnel end node
	 * turns to 'compromised' to.
	 * @param net is the network to generate the funnel into.
	 * @param inputNodes are the nodes being read, combined with each 'compromised' risk.
	 * 
	 * @return the last node in the funnel, the tip.
	 */
	public static ProbabilisticNode funnelToOneNodeBayes(ProbabilisticNetwork net, Map<ProbabilisticNode, Double> inputNodes) {
		// Check for correct inputs and return early if applicable.
		if (inputNodes.size() <= 0) 
			throw new IllegalArgumentException("At least 1 input node necessary!");
		if (inputNodes.size() == 1) return inputNodes.keySet().iterator().next(); // No funnel necessary for only one input node
		assert (inputNodes.size() >= 2);
		
		// Load input nodes for processing.
		LinkedList<ProbabilisticNode> nodesWorkList = new LinkedList<ProbabilisticNode>(inputNodes.keySet());
		
		// Start iteration.
		while (nodesWorkList.size() >= 2) {
			ListIterator<ProbabilisticNode> iterator = nodesWorkList.listIterator();
			while (iterator.hasNext()) {
				ProbabilisticNode in1 = iterator.next();
				iterator.remove();  // Working on the list and consuming the processed element.
				if (iterator.hasNext() == false) {
					// if only one node is left for processing, it can be left for the next iteration.
					iterator.add(in1); // but put the last node back to the list. Do not lose it.
					break;
				}
				ProbabilisticNode in2 = iterator.next();
				iterator.remove();
				ProbabilisticNode out = new ProbabilisticNode();
				out.setName(Crawler.FUNNEL_NODE_NAME + Crawler.probabilisticNodeFunnelCounter++ + ")");
				out.appendState("Compromised");
				out.appendState("Intact");
				PotentialTable table = out.getProbabilityFunction();
				table.addVariable(out); // Must be set before the edges! Otherwise the table will be interpreted wrongly!
				try {
					net.addEdge(new Edge(in1, out)); // Must be set as first edge! Otherwise the table will be interpreted wrongly!
					net.addEdge(new Edge(in2, out));  // Must be set as second edge! Otherwise the table will be interpreted wrongly!
				} catch (InvalidParentException e) {
					e.printStackTrace();
				}
				final Double aObject = inputNodes.get(in1);
				final Double bObject = inputNodes.get(in2);
				final double a = (aObject != null) ? (double) aObject : ALWAYS; // "null" means it must be a funnel node. Such nodes have likelihood "always".
				final double b = (bObject != null) ? (double) bObject : ALWAYS; // "null" means it must be a funnel node. Such nodes have likelihood "always".
				// A and B compromised.
				table.setValue(0, (float)(a + b - a * b));
				table.setValue(1, (float)(1d - (a + b - a * b)));
				// only A, not B
				table.setValue(2, (float)(a));
				table.setValue(3, (float)(1d - a));
				// only B, not A
				table.setValue(4, (float)(b));
				table.setValue(5, (float)(1d - b));
				// neither A or B
				table.setValue(6, IMPOSSIBLE);
				table.setValue(7, ALWAYS);
				net.addNode(out);
				iterator.add(out);
			}
			if (LOG_LEVEL == 3)
				System.out.println("Trichterbreite = " + nodesWorkList.size() + " Knoten.");
		}
		assert (nodesWorkList.size() == 1);
		return nodesWorkList.getFirst();
	}

	public static int getFunnelNodeCounter() {
		return probabilisticNodeFunnelCounter;
	}

	private void exportPathIntoAttackGraph(CrawledAsset a) {
		// Insert attractor as final graph node.
		Asset attractor = a.getAsset();
		attackGraph.addAsset(attractor);

		// loop backwards by exploit and path directions.
		CrawlableGraphNode node = a;
		CrawlableGraphNode lastNode = node.getLastCrawlableGraphNode();
		assert lastNode instanceof CrawledSoftware || lastNode instanceof CrawledAccess;
		assert node instanceof CrawledSoftware || node instanceof CrawledAsset;
		
		while (lastNode != null) {
			// Add new elements to graph.
			attackGraph.addNodeInReverse(node, node.getUsedExploit(), lastNode);
			
			// Stop when able to connect to another path.
			if (attackGraph.hasPredecessors(lastNode)) {
				break; // Last node already is connected in the graph and does not need further graph construction.
			}
		
			// expand otherwise.
			node = node.getLastCrawlableGraphNode();
			lastNode = node.getLastCrawlableGraphNode();
			assert lastNode instanceof CrawledSoftware || lastNode instanceof CrawledAccess || lastNode == null;
			assert node instanceof CrawledSoftware || node instanceof CrawledAsset || node instanceof CrawledAccess;
		}
	}

	/**
	 * Complexity: O(1) as of assumption 5.
	 */
	private void loadOpenAttractors() {
		openAttractors.addAll(scenario.getAttractors()); // Complexity: List is initially empty, so O(1).
	}

	/**
	 * Load access nodes into the crawler's open list.
	 * 
	 * Complexity: O(1) as of assumption 5.
	 */
	private void loadAccessNodesIntoOpenList() {
		for (Access access : scenario.m_AttackerProfile.getAccesses()) { // Complexity: O(1) as of assumption 5.
			openList.add(new CrawledAccess(scenario, access)); // Complexity: O(1) as list is previously empty.
		}
	}

	/**
	 * Load access nodes into the crawler's open list for phase 3.
	 * 
	 * Complexity: O(1) as of assumption 5.
	 */
	private void loadAccessNodesIntoOpenListP3() {
		for (Access access : scenario.m_AttackerProfile.getAccesses()) { // Complexity: O(1) as of assumption 5.
			openListP3Bayes.add(new CrawledAccessP3(scenario, access)); // Complexity: O(1) as list is previously empty.
		}
	}
	

	/**
	 * The cheapest exploit is considered for expanding a task.
	 * Some expansions yield no exploit at all, and don't make it into the open list.
	 * Complexity: O(log(S)+E) as of *.
	 * @param origin
	 */
	private void expandCrawlable(Crawlable origin) {
		// evaluate ComNodes
		evaluateReachableComNodes(origin);
		
		// Collect targetable Software nodes.
		final Set<Exploitable> exploitables = new HashSet<Exploitable>();
		exploitables.addAll(origin.getDirectlyReachableSoftwareNodes()); // Complexity: O(1) as of 1,2,3,4.

		// Collect targetable Attractors.
		if (origin.hasAttractors()) { // Complexity: O(1) as of 5. TODO ?
			exploitables.addAll(origin.getAttractors()); // Complexity: O(1) as of 5 TODO ? 
		}

		// Convert to Crawlables.
		Set<Crawlable> crawlables = new HashSet<Crawlable>(batchTask2CSAndRegistration(exploitables)); // Complexity: O(1) as of 1,2,3,4,5.
		
		// Evaluate all targetable exploitables.
		for (Crawlable target : crawlables) { // Complexity: O(1) as of 1,2,3,4,5.
			if (origin == target) {
				continue; // special case: Do not try to exploit yourself.
			}
			if (target.isClosed()) // Complexity: O(1).
				continue;   // Testing closed nodes is not necessary as the heuristic is consistent.
			for (Exploit e: origin.getAllExploitsAgainstTarget(target)) { // Complexity presumably O(E) as of 1,2,3,4,6,7,8
				final double totalPathCostTilTarget = origin.getMinCostMeanTilHere() + scenario.cost(e).getMoneyExpectedValue();  // Complexity: O(1) as of 5
				if (totalPathCostTilTarget > scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma()) // Test if attack can afford this step.
					continue; // Exploit is way too expensive for attacker and hence not affordable.
				else { // Exploit is affordable and must be considered.
					// Success, put onto openList. Test whether a better path has been found.
					final boolean openListNotificationNecessary = totalPathCostTilTarget < scenario.costInvestedSoFar(target); // OpenList needs to be notified about a new low cost possibility.
					target.addNewExploitOrigin(origin, e, totalPathCostTilTarget); 
					if (openListNotificationNecessary) {
						// hit: found a cheaper way to approach a task. works with new and previously known CSes.
						// TODO annotateNewFoundNodesWithPredecessorDirections();
						openList.remove(target); // will fail silently at unknown objects, good for handling also new CS objects.
						openList.add(target); // re-add for updated cost comparison. // Complexity: O(logS) as of *.
					}
				}
			}
		}
	}	
	
	
//	/**
//	 * Choose the cheapest exploit.
//	 * And check for exploit conditions being met for attacking a certain task.
//	 * @param newNode The newly reachable node about being exploited.
//	 */
//	public Exploit chooseCheapestExploit(Crawlable origin, Crawlable target) {
//		// collect possible exploits.
//		Set<Exploit> consideredExploits = new HashSet<Exploit>(); 
//		for (Exploit e : scenario.getAllExploitsAgainstTask(target.getTask())) {
//			if (e.areSwCapComConditionsMet(origin.getTask(), target.getTask()))
//				consideredExploits.add(e);
//		}
//		// TODO: Cost calculation should be done less often! Cache somewhere!
//		// choose cheapest exploit.
//		Exploit cheapestExploit = null;
//		float cheapestExploitCost = Float.POSITIVE_INFINITY;
//		for (Exploit e : consideredExploits) {
//			float cost = costForExploitApplication(e);
//			// TODO Es müssen noch Kosten für Exploit-Creation rein!
//			// TODO Es sollten die Exploit-Kosten mit Unsicherheit verarbeitet werden, weniger die Angreifer.
//			if (cost < cheapestExploitCost) {
//				cheapestExploit = e;
//				cheapestExploitCost = cost; 
//			}
//		}
//		return cheapestExploit;
//	}

	
	private void evaluateReachableComNodes(Crawlable origin) {
		final Set<CommunicationMedium> reachableComMedia = origin.getAttackableComMedia();
		if (reachableComMedia != null) {
			for (CommunicationMedium comNode : reachableComMedia) { // only a origin of type CrawledSoftware can yield com media. 
				CrawledComMedia targetedCrawledComNode = CrawledComMedia.convertToCrawlable(comNode);
				if (targetedCrawledComNode == null) { // unknown ComNode, so insert.
					targetedCrawledComNode = new CrawledComMedia(scenario, comNode);
					CrawledComMedia.register(comNode, targetedCrawledComNode);
					targetedCrawledComNode.addNewExploitOrigin(origin, Exploit.COM_MEDIUM_TRAVERSAL_EXPLOIT, origin.getMinCostMeanTilHere()); 
					openList.add(targetedCrawledComNode);
					continue; // next node.
				} else { // previously known, so update
					if (targetedCrawledComNode.isClosed()) {
						continue; // no need to reevaluate a closed node.
					} else { // ComNode is still open
						final double newPathCostToTargetCom = scenario.costInvestedSoFar(origin);
						if (newPathCostToTargetCom <= scenario.getAttackerProfile().getMaximumBudgetPlusTwoSigma()) {
							// hit: attacker can afford going into this com node.
							final double pathCostToTargetComPreviously = scenario.costInvestedSoFar(targetedCrawledComNode);
							targetedCrawledComNode.addNewExploitOrigin(origin, Exploit.COM_MEDIUM_TRAVERSAL_EXPLOIT, newPathCostToTargetCom); 
							if (newPathCostToTargetCom < pathCostToTargetComPreviously) {
								// lower bound has been surpased, so an update of the openList is necessary.
								openList.remove(targetedCrawledComNode); // will fail silently at unknown objects, good for handling also new CS objects.
								openList.add(targetedCrawledComNode); // re-add for updated cost comparison. // Complexity: O(logS) as of *.
							}
						}
					}
				}
			}
		}
	}

	
	
	/**
	 * @param reachableExploitables1
	 * @return
	 */
	private CrawlableGraphNode Exploitable2CSAndRegistration(Asset a) {
		checkAssertions();
		CrawlableGraphNode cs = exploitable2Cs.get(a);
		if (cs == null) {
			cs = new CrawledAsset(scenario, a);
			exploitable2Cs.put(a, cs);
		}
		return cs;
	}
	
	/**
	 * @param reachableExploitables1
	 * @return
	 */
	private CrawlableGraphNode Exploitable2CSAndRegistration(Software sw) {
		checkAssertions();
		CrawlableGraphNode cs = exploitable2Cs.get(sw);
		if (cs == null) {
			cs = new CrawledSoftware(scenario, sw);
			exploitable2Cs.put(sw, cs);
		}
		return cs;
	}

	
	@Deprecated
	private Set<CrawlableGraphNode> batchTask2CSAndRegistration(Set<Exploitable> exploitables) {
		Set<CrawlableGraphNode> cses = new HashSet<CrawlableGraphNode>();
		checkAssertions();
		for (Exploitable e : exploitables) {
			CrawlableGraphNode cs = exploitable2Cs.get(e);
			if (cs == null) {
				// newly found exploitable.
				if (e instanceof Software) {
					cs = new CrawledSoftware(scenario, (Software) e);
				}
				if (e instanceof Asset) {
					cs = new CrawledAsset(scenario, (Asset) e);
				}
				exploitable2Cs.put(e, cs);
			}
			cses.add(cs);
		}
		return cses;
	}

	/**
	 * Batch convert Software to Phase 3 Crawlables.
	 * @param exploitables
	 * @return
	 */
	private Set<CrawlableGraphNode> batchSw2Csp3AndRegistration(Set<Exploitable> reachableExploitables) {
		HashSet<CrawlableGraphNode> cses = new HashSet<CrawlableGraphNode>();
		checkAssertions();
		for (Exploitable e : reachableExploitables) {
			CrawlableGraphNode cs = exploitable2CsP3.get(e);
			if (cs == null) {
				// newly found exploitable.
				if (e instanceof Software) {
					cs = new CrawledSoftwareP3(scenario, (Software) e);
				}
				if (e instanceof Asset) {
					cs = new CrawledAssetP3(scenario, (Asset) e);
				}
				exploitable2CsP3.put(e, cs);
			}
			cses.add(cs);
		}
		return cses;
	}

	/**
	 * Complexity: O(S) as of *.
	 */
	private void checkAssertions() {
		assert !exploitable2Cs.containsKey(null); // Complexity: O(S).
		assert !openAttractors.contains(null); // Complexity: O(1) as of *.
		assert !openList.contains(null); // Complexity: O(S).
	}

	/**
	 * @return
	 */
	public Resources getCostForAllPathsCombinedCost() {
		return p3SalferAllPathsCostAccumulated;
	}

	/**
	 * @return
	 */
	public int getConsideredPathsNumber() {
		return p3SalferAllPathsNumber;
	}

	public double getAllPathVulnerabilityAccumulated() {
		return p3SalferAllPathsVulnerabilityProbabilityAccumulated;
	}

	public double getAllPathAttackabilityAccumulated() {
		return p3SalferAllPathsAttackabilityProbabilityAccumulated;
	}
	
	public long getCrawlerRunTimeNano() {
		return crawlerRunTimeNano;
	}

	public long getCrawlerRunTimeMilli() {
		return crawlerRunTimeNano / 1_000_000;
	}
	
	public long getCrawlerRunTimeSeconds() {
		return crawlerRunTimeNano / 1_000_000_000;
	}

	public double getCrawlerMemAvg() {
		return memStatistics.getMean();
	}
	
	public double getCrawlerMemMax() {
		return memStatistics.getMax();
	}
	
}
