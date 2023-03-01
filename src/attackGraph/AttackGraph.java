package attackGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import analysis.Analyzer;
import analysis.Crawlable;
import analysis.CrawlableGraphNode;
import analysis.CrawledAccess;
import analysis.CrawledAsset;
import analysis.CrawledComMedia;
import analysis.CrawledSoftware;
import analysis.Crawler;
import analysis.multipath.salfer.Path;
import attackerProfile.Access;
import exploit.Exploit;
import systemModel.Asset;
import systemModel.Exploitable;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;
import unbbayes.prs.bn.ProbabilisticNetwork;



/**
 * Attack Graphs cover attack paths of an attacker within a system model and hence
 * are attacker-specific.
 * 
 * Note: Several paths can be considered within one attack graph.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class AttackGraph {

	private final String name;
	private final AttackScenario scenario; // obligatory at construction.
	
	private final Set<AttackGraphNode> m_AttackGraphNode = new HashSet<AttackGraphNode>();
	private final Map<Access, AttackGraphNodeAccess> m_entryNode = new HashMap<Access, AttackGraphNodeAccess>(5);
	private final Set<AttackGraphEdge> m_AttackGraphEdge = new HashSet<AttackGraphEdge>();
	private final HashMap<Exploitable, AttackGraphNode> exploitable2agNode = new HashMap<Exploitable, AttackGraphNode>();
	private final HashMap<AttackGraphNode, Set<AttackGraphEdge>> edgesCache = new HashMap<AttackGraphNode, Set<AttackGraphEdge>>();
	
	private final Map<Asset, ProbabilisticNetwork> probabilisticNetworks = new HashMap<Asset, ProbabilisticNetwork>(5);
	private Resources allPathsCostCombined = new Resources(0d, 0d);
	private double    allPathsVulnerabilityAccumulated = 0d;
	private int       allPathsNumber = 0;
	
	public AttackGraph(AttackScenario scenario) {
		this("<NoName>", scenario);
	}

	public AttackGraph(String name, AttackScenario scenario) {
		this.name = name;
		this.scenario = scenario;
	}

	public void finalize() throws Throwable {
	}
	
	/**
	 * Get all Assets in this graph.
	 * @return All Assets in this graph.
	 */
	public Set<Asset> assets(){
		Set<Asset> assets = new HashSet<Asset>();
		for (AttackGraphNode n : m_AttackGraphNode) {
			if (n instanceof AttackGraphNodeAsset) {
				assets.add(((AttackGraphNodeAsset)n).asset());
			}
		}
		return assets;
	}

	/**
	 * Add a system model node to an attack graph. The originating
	 * node must be an access node, as those can be a root of a graph.
	 * 
	 * Access objects become entry nodes of a graph. Any graph must start with an
	 * access object. The access node refers to system nodes,
	 * which are reused here for AttackGraphNode creation. Those objects are included
	 * in separate AttackGraphNodes for consistency of the graph
	 * for latter traversal as merely edges and nodes. 
	 *  
	 * @param access The attacker profile's access object with system nodes.
	 */
	public void addAccessNode(Access access) {
		AttackGraphNodeAccess attackGraphNodeAccess = m_entryNode.get(access);
		if (attackGraphNodeAccess == null) {
			attackGraphNodeAccess = new AttackGraphNodeAccess(access);
		}
		AttackGraphNodeAccess node = attackGraphNodeAccess;
		m_AttackGraphNode.add(node);
		m_entryNode.put(access, node);
	}

	/**
	 * Add a system model node to an attack graph. The originating
	 * node must be an access node, as those can be a root of a graph.
	 * @param access
	 * @param exploit
	 * @param t1
	 */
	public void addAgNodeFromAccess(Access access, Exploit exploit, Exploitable t1) {
		AttackGraphNode a = getAgNodeForAccess(access); 		// TODO is t1 accessible from access? Check!
		if (a != null) {
			addAgNodeToExploitable(a, exploit, t1);
		}
		else
			throw new IllegalArgumentException("Access node " + a + " not in graph " + this);
	}
	
	/**
	 * Add a system model node to an attack graph. The originating
	 * node must be an access node, as those can be a root of a graph.
	 * @param origin - The originating access node for an attack. 
	 * @param exploit - The exploit used for an attack.
	 * @param target - The target exploitable of an attack.
	 */
	public void addAgNodeToAccess(Exploitable target, Exploit exploit, Access origin) {
		AttackGraphNode originAgNode = getAgNodeForAccess(origin);
		if (originAgNode != null) {
			addAgNodeToExploitable(originAgNode, exploit, target);
		}
		else
			throw new IllegalArgumentException("Access node " + originAgNode + " not in graph " + this);
	}
	
	

	private AttackGraphNode getAgNodeForAccess(Access access) {
		return m_entryNode.get(access);
	}

	/**
	 * Add an graph node from an exploitable to an exploitable object.
	 * 
	 * @param origin - The originating exploitable; It must already exist in the graph.
	 * @param exploit - The exploit used for this new graph edge.
	 * @param target - The target exploitable; it may be new and not exist in the graph, yet.
	 */
	public void addNodeExplToExpl(Exploitable origin, Exploit exploit, Exploitable target) { // TODO muss noch auf Task + Asset eingeschränkt werden.
		AttackGraphNode a = getAgNodeForSysNode(origin);
		addAgNodeToExploitable(a, exploit, target);
	}
	
	/**
	 * Add an graph node and create a connecting edge to an existing attack graph node.
	 * 
	 * @param origin - The originating exploitable; It must already exist in the graph.
	 * @param exploit - The exploit used for this new graph edge.
	 * @param targetSysNode - The target exploitable; it may be new and not exist in the graph, yet.
	 */
	private void addAgNodeToExploitable(AttackGraphNode origin, Exploit exploit, Exploitable targetSysNode) { // TODO muss noch auf Task+ Asset eingeschränkt werden.
		AttackGraphNode targetAgNode = getAgNodeForSysNode(targetSysNode); // TODO vermutlich kann man die Einfügeoperation auch so machen ohne Suche dank Idempotenz.
		if (targetAgNode == null)  {
			targetAgNode = new AttackGraphNodeSoftware(targetSysNode);
			this.m_AttackGraphNode.add(targetAgNode);
			this.exploitable2agNode.put(targetSysNode, targetAgNode);
		}
		// check for already existing edge:
		if (!containsEdge(origin, exploit, targetAgNode)) {
			createAndInsertEdgeIntoGraph(origin, exploit, targetAgNode);
		}
	}
	
	/**
	 * Create and Insert an attack graph edge into the graph.
	 * 
	 * @param origin
	 * @param exploit
	 * @param target
	 */
	private void createAndInsertEdgeIntoGraph(AttackGraphNode origin, Exploit exploit, AttackGraphNode target) {
		if (origin != null && target != null && exploit != null)
			if (origin != target) {
				final AttackGraphEdge edge = new AttackGraphEdge(origin, exploit, target);
				m_AttackGraphEdge.add(edge);
				target.setPredecessor(origin);
				Set<AttackGraphEdge> edgesSet = edgesCache.get(origin);
				if (edgesSet == null)  {
					edgesSet = new HashSet<AttackGraphEdge>();
					edgesCache.put(origin, edgesSet);
				}
				edgesSet.add(edge);
			}
		
			else
				throw new IllegalArgumentException("Origin and Destination are identical, but must be different.");
		else
			throw new NullPointerException("One argument is null. Please debug.");
	}

	/**
	 * Query for an attack graph node representing a certain exploitable node.
	 * 
	 * @param sysNode
	 * @return null, if the graph does not contain sysNode.
	 */
	private AttackGraphNode getAgNodeForSysNode(Exploitable sysNode) {
		return exploitable2agNode.get(sysNode);
	}
	



	public boolean contains(Access a) {
		return m_entryNode.containsKey(a);
	}

	/**
	 * Does the attack graph contain the system node.
	 * @param sysNode The system node to look for in the attack graph.
	 * @return true if the system node is used in this attack graph.
	 */
	public boolean contains(Exploitable sysNode) {
		return this.getAgNodeForSysNode(sysNode) != null;
	}
	
	/**
	 * Gets the system model of an graph as reference.
	 */
	public SystemModel getSystemModel() {
		return scenario.getSystemModel();
	}

	public void reset() {
		m_AttackGraphEdge.clear();
		m_entryNode.clear();
		m_AttackGraphNode.clear();
		exploitable2agNode.clear();
		allPathsCostCombined = new Resources(0d, 0d);
		allPathsNumber = 0;
	}

	/**
	 * Checks if the Asset is already in the graph and will reuse the existing object.
	 * @param asset
	 */
	public void addAsset(Asset asset) {
		AttackGraphNode node = getAgNodeForSysNode(asset);
		if (node == null) {
			node = new AttackGraphNodeAsset(asset);
		}
		m_AttackGraphNode.add(node);
		exploitable2agNode.put(asset, node);
	}

	/**
	 * Insert nodes and edges into an graph starting from the target.
	 * The phase-II
	 * Used for attack graph construction from phase-II crawler, which starts construction at the Asset.
	 *
	 * @param target
	 * @param usedExploit
	 * @param origin
	 */
	public void addNodeInReverse(Crawlable target, Exploit usedExploit, Crawlable origin) { // TODO Der Crawlable sollte langfristig raus, da Analysis-Paket, nicht AG-Paket.
		AttackGraphNode targetAgNode = getAgNodeForSysNode(target.getExploitable()); // must work as node is to be added by addAsset().
		if (targetAgNode == null)
			throw new IllegalArgumentException("Node " + target + " could not be found in the attack graph.");

		AttackGraphNode originAgNode = null;
		if (origin instanceof CrawledComMedia) {
			origin = (Crawlable)origin.getAttackPredecessor(); // For a Communicaton Medium, not the medium is considered the origin, but the previous Software node.
		}
		if (origin instanceof CrawledSoftware) {
			originAgNode = getAgNodeForSysNode(origin.getExploitable()); // can fail with a null.
			if (originAgNode == null)  {
				originAgNode = new AttackGraphNodeSoftware(origin.getExploitable());
				this.m_AttackGraphNode.add(originAgNode);
				this.exploitable2agNode.put(origin.getExploitable(), originAgNode);
			}
		}
		if (origin instanceof CrawledAccess) {
			final Access access = ((CrawledAccess)origin).getAccess();
			addAccessNode(access);
			originAgNode = getAgNodeForAccess(access);
		}
		//Check for already existing edge
		if (!containsEdge(originAgNode, usedExploit, targetAgNode)) {
			createAndInsertEdgeIntoGraph(originAgNode, usedExploit, targetAgNode);
		} else {
			return; // edge already in the graph.
		}
	}
	
	
	
	
	

	/**
	 * @param originAgNode
	 * @param usedExploit
	 * @param targetAgNode
	 * @return
	 */
	private boolean containsEdge(AttackGraphNode originAgNode, Exploit usedExploit, AttackGraphNode targetAgNode) {
		final Set<AttackGraphEdge> edgesSet = edgesCache.get(originAgNode);
		if (edgesSet == null)
			return false; // the cache does not even contain an entry for this origin node.
		else {
			for (AttackGraphEdge edge: edgesSet) {
				if (edge.origin == originAgNode && edge.exploit == usedExploit && edge.target == targetAgNode) {
					return true; // edge found!
				}
			}
		}
		return false; // no edge found with all parameters identically.
	}

	public boolean hasPredecessors(Crawlable lastNode) {
		AttackGraphNode agNode = getAgNodeForSysNode(lastNode.getExploitable());
		
		for (AttackGraphEdge edge : m_AttackGraphEdge) {
			if (edge.target == agNode)
				return true; // found an edge that leads to last node, i.e. last node must have a predecessor.
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("AttackGraph[" + name + "]:");
		if (m_entryNode.size() > 0) {
			for (AttackGraphNode entryNode : m_entryNode.values()) {
				string.append("\n");
				// Start the graph output.
				// Special handling for access node.
				
				for (AttackGraphEdge e : m_AttackGraphEdge) {
					if (e.origin == entryNode) {
						string.append(scenario.getAttackerProfile().toString() + " access by " + entryNode + " with " + e.exploit + ":\n");
						// Regular traversal over Task nodes.
						try {
							toStringTraverseTaskNode(string, e.target, 0);
						} catch (StackOverflowError error) {
							System.out.println("AttackGraph too deep for recurisve display. :-(");
						}
					}
				}
				if (entryNode.getSysNode() instanceof Asset) {
					string.append("-> " + entryNode.getSysNode().toString());
				}
			}
		} else {
			string.append("<empty>");
		}
		return string.toString();
	}

	/**
	 * Iterative implementation not necessary, as too deep attack graphs are not printable anyways.
	 * @param string
	 * @param n
	 * @param depth
	 */
	private void toStringTraverseTaskNode(StringBuilder string, AttackGraphNode n, int depth) {
		for (AttackGraphEdge e : m_AttackGraphEdge) {
			if (e.origin == n) {
				for (int i = 1 ; i <= depth; i++)
					string.append(" ");
				string.append("-> ");
				string.append(n.toString());
				string.append(" exploits further with ");
				string.append(e.exploit);
				string.append(".\n");
				toStringTraverseTaskNode(string, e.target, depth+1);
			}
		}
	}

	
	/**
	 * Query for an attackers maximum cost expected.
	 * 
	 * This cost calculation assumes that an attacker will access all
	 * vehicles he can and apply all exploits and catch all Assets.
	 * (The Access object number of vehicles accessible
	 * is taken as an number of accessed vehicles. This also distributes the initial
	 * exploit costs over many targets and makes the business case usually profitable.)
	 *
	 * @return An attacker expected cost at max (apply all exploits, catching all Assets and accessing all targetable vehicles).
	 * @deprecated
	 */
	public float calculateAttackerCostExpectedMax() {
		float cost = 0f;
		for (AttackGraphNodeAccess node : m_entryNode.values()) {
			cost += expandNodesAndSumExploitCosts(node);
			cost += node.access.getAccessCostMax();
		}
		return cost;
	}

	private double expandNodesAndSumExploitCosts(AttackGraphNode node) {
		double cost = 0f;
		for (AttackGraphEdge edge : m_AttackGraphEdge) {
			if (node == edge.origin) {
				cost += scenario.cost(edge.exploit).getMoneyExpectedValue(); // cost of this exploit.
				cost += expandNodesAndSumExploitCosts(edge.target); // cost of child exploits.
			}
		}
		return cost;
	}

	/**
	 * Query for the number of edges in this graph.
	 * Useful for testing.
	 * @return
	 */
	public int getNumberOfEdges() {
		return m_AttackGraphEdge.size();
	}

	/**
	 * Query for the number of nodes in this graph.
	 * Useful for testing.
	 * @return
	 */
	public int getNumberOfNodes() {
		return m_AttackGraphNode.size();
	}

	/**
	 * Query for all exploits used in an attack graph.
	 * @return A set of all used exploits.
	 */
	public Set<Exploit> getAllAppliedExploits() {
		Set<Exploit> appliedExploits = new HashSet<Exploit>(m_AttackGraphEdge.size());
		for (AttackGraphEdge edge : m_AttackGraphEdge) {
			appliedExploits.add(edge.exploit);
		}
		return appliedExploits;
	}

	/**
	 * Query for all used access objects in this attack graph.
	 * @return A set of all used access objects in this attack graph.
	 */
	public Set<Access> getAllUsedAccesses() {
		Set<Access> usedAccesses = new HashSet<Access>(m_entryNode.size());
		for (AttackGraphNodeAccess access : m_entryNode.values()) {
				usedAccesses.add(access.getAccessObject());
		}
		return usedAccesses;
	}


	/**
	 * Query for an exploit's cost regarding this scenario.
	 * This method delegates the query to the scenario object.
	 * @param e - The exploit to be queried for
	 * @return The cost of the mentioned exploit regarding the surrounding scenario / attacker.
	 */
	public Resources getAttackerSpecificAdaptionCostForExploit(Exploit e) {
		return scenario.cost(e);
	}
//
//	/**
//	 * Query for the exploit cost for a specific attacker.
//	 * @param e Exploit to query for.
//	 * @return The cost the specific attacker has to budget.
//	 */
//	public float getAttackerSpecificExploitCostForExploit(Exploit e) {
//		return scenario.cost(e);
//	}

	/**
	 * Query for all exploits a Asset uses.
	 * It starts traversing from an Asset. Only one path is possible per Asset.
	 * Like a lightning, it selects the path with the lowest resistance.
	 * @param Asset to look for.
	 * @return set of exploits used for finding a certain Asset.
	 */
	public Set<Exploit> getAllAppliedExploitsForAsset(Asset asset) {
		Set<Exploit> appliedExploits = new HashSet<Exploit>();
		AttackGraphNode node = getAgNodeForSysNode(asset);
		nodeIteration: while (node != null) { // continue label.
			AttackGraphNode predecessor = node.getPredecessor();
			for (AttackGraphEdge e : m_AttackGraphEdge) {
				if (e.target == node && e.origin == predecessor) {
					/* expand edge */
					appliedExploits.add(e.exploit);
					
					/* next iteration */
					node = predecessor;
					predecessor = node.getPredecessor();
					if (predecessor == null) {
						break nodeIteration; // end when no node pair can be found any more.
					}
					continue nodeIteration; // restart at while loop.
				}
			}
		}
		return appliedExploits;
	}

	public Set<Access> getAllUsedAccessesForAsset(Asset asset) { // TODO Rückgabe auf Einzel-Attraktor verringern.
		Set<Access> accesses = new HashSet<Access>(1);
		AttackGraphNode node = getAgNodeForSysNode(asset);
		nodeIteration: while (node != null) { // continue label.
			AttackGraphNode predecessor = node.getPredecessor();
			for (AttackGraphEdge e : m_AttackGraphEdge) {
				if (e.target == node && e.origin == predecessor) {
					/* next iteration */
					node = predecessor;
					predecessor = node.getPredecessor();
					if (predecessor == null) {
						break nodeIteration; // end when no node pair can be found any more.
					}
					continue nodeIteration; // restart at while loop.
				}
			}
		}
		if (node instanceof AttackGraphNodeAccess) {
			accesses.add(((AttackGraphNodeAccess) node).getAccessObject());
			return accesses;
		} else {
			return null;
		}
	}

	/** Get attack scenario considered for this attack graph.
	 * 
	 * @return The attack scenario for this attack graph.
	 */
	public AttackScenario getScenario() {
		return scenario;
	}

	/**
	 * Count the number of available access nodes in this graph.
	 * @return the number of available access nodes in this graph.
	 */
	public int getNumberOfAccesses() {
		return m_entryNode.size();
	}

	/**
	 * Get the access node that is used for exploiting a certain Asset. 
	 * @param asset
	 * @return
	 */
	public Access getUsedAccessForAsset(Asset asset) {
		final Set<Access> access = this.getAllUsedAccessesForAsset(asset);
		if (access != null) {
			switch (access.size()) {
			case 0:
				return null; // Asset not reachable.
			case 1:
				return access.iterator().next(); // optimal case.
			default:
				throw new NoSuchElementException("Set must have one or zero elements.");
			}
		} else 
			return null;
	}

	
	/**
	 * Get the access nodes that is used for exploiting certain Assets. 
	 * @param assets
	 * @return
	 */
	public Set<Access> getUsedAccessForAssets(Set<Asset> assets) {
		final Set<Access> access = new HashSet<Access>(assets.size());
		for (Asset asset : assets) {
			access.add(getUsedAccessForAsset(asset));
		}
		return access;
	}

	/**
	 * Get the chain of attack graph edges from an access object to
	 * a certain Asset.
	 * The fitting access object will be searched for.
	 * @param asset The goal asset.
	 * @return A chain of edges starting from the access exploit to the asset exploit.
	 */
	public List<AttackGraphEdge> getAttackPathTo(Asset asset) {
		List<AttackGraphEdge> attackPath = new ArrayList<AttackGraphEdge>();
		AttackGraphNode node = getAgNodeForSysNode(asset);
		nodeIteration: while (node != null) { // continue label.
			AttackGraphNode predecessor = node.getPredecessor();
			for (AttackGraphEdge e : m_AttackGraphEdge) {
				if (e.target == node && e.origin == predecessor) {
					/* expand edge */
					attackPath.add(e);
					
					/* next iteration */
					node = predecessor;
					predecessor = node.getPredecessor();
					if (predecessor == null) {
						break nodeIteration; // end when no node pair can be found any more.
					}
					continue nodeIteration; // restart at while loop.
				}
			}
		}
		Collections.reverse(attackPath);
		return attackPath;
	}

	/**
	 * Get all attack graph edges from access objects to
	 * a certain set of Assets.
	 * The ordering is undefined; It depends on the internal hashcode() method.
	 * @param assets The goal Assets.
	 * @return A list of involved edges with no particular order.
	 */
	public List<AttackGraphEdge> getAttackNodesTo(Set<Asset> assets) {
		Set<AttackGraphEdge> attackEdges = new HashSet<AttackGraphEdge>();
		for (Asset asset : assets) {
			attackEdges.addAll(getAttackPathTo(asset));
		}
		return new ArrayList<AttackGraphEdge>(attackEdges);
	}

	/**
	 * @param v1 - Asset that shall be the final target in the probabilistic network
	 * @return the probabilistic network for Asset v1.
	 */
	public ProbabilisticNetwork getProbabilisticNetworkFor(Asset v1) {
		return probabilisticNetworks.get(v1);
	}

	/**
	 * @param net
	 * @param asset
	 */
	public void addProbabilisticNetworkForAsset(ProbabilisticNetwork net, Asset asset) {
		probabilisticNetworks.put(asset, net);
	}

	public ProbabilisticNetwork getAProbabilisticNetwork() {
		return probabilisticNetworks.values().iterator().next();
	}

	/**
	 * Export to an AttackGraph object via a Path.
	 * Will construct also beyond already existing graph nodes as a difference in former nodes might occur.
	 * @param path
	 */
	public void exportPathIntoAttackGraph(Path path) {
		// Insert attractor as final graph node.
		addAsset(path.getPosition().getAsset());
	
		// loop backwards by exploit and path directions.
		ListIterator<CrawlableGraphNode> nodesBackward = path.getNodesIteratorAtEnd();
		ListIterator<Exploit> exploitsBackward = path.getExploitsIteratorAtEnd();
		assert exploitsBackward.hasPrevious();
		CrawlableGraphNode target = nodesBackward.previous(); // consumes position element.
		assert exploitsBackward.hasPrevious();
		CrawlableGraphNode origin = nodesBackward.previous(); // consumes first predecessor element.
		assert origin instanceof CrawledSoftware || origin instanceof CrawledAccess;
		assert target instanceof CrawledSoftware || target instanceof CrawledAsset;
		while (origin != null) {
			assert exploitsBackward.hasPrevious();
			// Add new elements to graph.
			addNodeInReverse(target, exploitsBackward.previous(), origin);
			
			if (!nodesBackward.hasPrevious()) {
				// Stop when no more previous nodes are available.
				break;
			} else {
				// fetch next otherwise.
				target = origin;
				origin = nodesBackward.previous();
				assert origin instanceof CrawledSoftware || origin instanceof CrawledAccess || origin == null;
				assert target instanceof CrawledSoftware || target instanceof CrawledAsset || target instanceof CrawledAccess;
			}
		}
	}

	
	public Resources getCostForOptimalPathTo(Exploitable n) {
		assert n != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(n);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getCostForOptimalPath();
		} else {
			return null;
		}
	}

	/**
	 * The vulnerability probability mean of all traversed attack surfaces to here.
	 * @param n - the target.
	 * @return the probability as described above.
	 */
	public double getVulnerabilityProbabilityMeanForPathsThrough(Exploitable n) {
		assert n != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(n);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getVulnerabilityProbabilityMeanForPathsThroughHere(); // TODO
		} else {
			return 0d;
		}
	}

	/**
	 * The attackability probability mean of all traversed attack surfaces on the paths to here.
	 * attackable means vulnerable and affordable.
	 * @param n - the target.
	 * @return the probability as described above.
	 */
	public double getAttackabilityProbabilityMeanForPathsThrough(Exploitable n) {
		assert n != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(n);
		if (agNodeForSysNode != null) {
			final double vulnerabilityProbability = agNodeForSysNode.getVulnerabilityProbabilityMeanForPathsThroughHere();
			final double affordabilityProbability = getAffordabilityProbabilityMeanForPathsThrough(n);
			return vulnerabilityProbability * affordabilityProbability;
		} else {
			return 0d;
		}
	}

	/**
	 * The affordability probability mean of all traversed attack surfaces on the paths to here.
	 * A path is affordable if an attacker profile bears enough resources
	 * for affording the predicted exploitation costs.
	 * The cost basis is the expected value of the successful paths.
	 * @param n - the target.
	 * @return the probability as described above.
	 */
	public double getAffordabilityProbabilityMeanForPathsThrough(Exploitable n) {
		Resources cost = getCostExpectedValueForPathsThrough(n);
		return Analyzer.calculateAttackerAffordability(scenario.getAttackerProfile().getResources(), cost);
	}

	/**
	 * @return all AttackGraphNode objects of this graph.
	 */
	public Set<AttackGraphNode> getAllAttackGraphNodes() {
		return Collections.unmodifiableSet(m_AttackGraphNode);
	}

	public String getName() {
		return name;
	}

	public Set<AttackGraphEdge> getAllAttackGraphEdges() {
		return Collections.unmodifiableSet(m_AttackGraphEdge);
	}

	/**
	 * @param sw
	 * @return
	 */
	public Resources getCostExpectedValueForPathsThrough(Exploitable sw) {
		if (sw != null) {
			final AttackGraphNode n = getAgNodeForSysNode(sw);
			if (n != null) {
				return n.getCostExpectedValueForSuccessfulPathsCombined();
			}
		}
		return null; // not found in attack graph.
	}

	/**
	 * @return
	 */
	public Resources getCostExpectedValueForAllPaths() {
		return allPathsCostCombined.divideBy(allPathsNumber);
	}

	/**
	 * Poincaré-Sylvester combination: At least one path.
	 * @param sw
	 * @return
	 * @deprecated Poincaré-Sylvester is no more useful for my stochastic model.
	 */
	public double getOverallAffordabilityProbabilityAtLeastOnePath(Software sw) {
		throw new UnsupportedOperationException();
	}

	public double getAffordabilityProbabilityForOptimalPathTo(Software sw) {
		assert sw != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(sw);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getAffordabilityProbabilityForOptimalPath();
		} else {
			return 0d;
		}
	}

	public double getAffordabilityProbabilityMeanForAllPaths() {
		Resources cost = allPathsCostCombined.divideBy(allPathsNumber);
		return Analyzer.calculateAttackerAffordability(scenario.getAttackerProfile().getResources(), cost);
	}

	/**
	 * @param sw
	 * @return
	 */
	public double getVulnerabilityProbabilityForOptimalPathTo(Exploitable sw) {
		assert sw != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(sw);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getVulnerabilityProbabilityForOptimalPath();
		} else {
			return 0d;
		}
	}

	public double getVulnerabilityProbabilityMeanForAllPaths() {
		return allPathsVulnerabilityAccumulated / allPathsNumber;
	}

	/**
	 * @param sw
	 * @return
	 * @deprecated Poincaré-Sylvester combination is not useful for my stochastic model.
	 */
	public double getVulnerabilityProbabilityForAtLeastOnePath(Software sw) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 */
	public double getAttackabilityProbabilityMeanForAllPaths() {
		return allPathsVulnerabilityAccumulated / allPathsNumber * getAffordabilityProbabilityMeanForAllPaths();
	}

	/**
	 * @param sw
	 * @return
	 */
	public double getAttackabilityProbabilityForOptimalPathTo(Exploitable e) {
		assert e != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(e);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getAttackabilityProbabilityForOptimalPath();
		} else {
			return 0d;
		}
	}

	/**
	 * @deprecated - Poincaré-Sylvester is no more regarded useful here.
	 */
	public double getOverallAttackabilityProbabilityForAtLeastOnePathsTo(Asset as) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param crawler TODO
	 * 
	 */
	public void importP3SalferResults(Crawler crawler) {
		allPathsCostCombined = crawler.getCostForAllPathsCombinedCost();
		allPathsVulnerabilityAccumulated = crawler.getAllPathVulnerabilityAccumulated();
		allPathsNumber    = crawler.getConsideredPathsNumber();
		for (AttackGraphNode nodeAttackGraph : getAllAttackGraphNodes()) {
			CrawlableGraphNode nodeCrawlable = crawler.getCrawlableGraphNodeForExploitable(nodeAttackGraph);
			if (nodeCrawlable != null) {
				nodeAttackGraph.transferMultipathResultsFromCrawlable(nodeCrawlable);
			} else {
				; // certain system nodes do not have an attack graph node, e.g. access nodes.
			}
		}
	}

	public int getNumberOfConsideredPaths() {
		return allPathsNumber;
	}

	public int getAmountOfRecordedPathExpansionsIn(Exploitable e) {
		assert e != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(e);
		if (agNodeForSysNode != null) {
			return agNodeForSysNode.getNumberOfRecordedPathExpansionsHere();
		} else {
			return 0;
		}
	}

	/**
	 * Get the number of successful paths to the Asset a.
	 * For an asset, the number of recorded path expansions equals the number of successful paths.
	 */
	public int getNumberOfSuccessfulPaths(Asset a) {
		assert a != null;
		final AttackGraphNode agNodeForSysNode = getAgNodeForSysNode(a);
		if (agNodeForSysNode != null && agNodeForSysNode instanceof AttackGraphNodeAsset) {
			return ((AttackGraphNodeAsset)agNodeForSysNode).getNumberOfRecordedPathExpansionsHere();
		} else {
			return 0;
		}
	}

	
}//end Attack Graph