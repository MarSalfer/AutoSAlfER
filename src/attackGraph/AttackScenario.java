package attackGraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import systemModel.Asset;
import systemModel.Exploitable;
import systemModel.Software;
import systemModel.SystemModel;
import types.Resources;
import analysis.Crawlable;
import analysis.CrawlableInterface;
import analysis.CrawledAccess;
import analysis.CrawledComMedia;
import analysis.CrawledSoftware;
import attackerProfile.Access;
import attackerProfile.AttackerProfile;
import exploit.Exploit;



/**
 * Attack Scenarios are concrete situations a crawler considers for the
 * construction of attack graphs for a system model.
 * 
 * An attack scenario contains one concrete attack situation. Several paths are
 * possible, but also none are possible, e.g., if the budget is too little.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class AttackScenario {

	public final Set<Exploit> availableExploits;
	public final SystemModel m_SystemModel;
	public final AttackerProfile m_AttackerProfile;
	private final double cheapestExploit;
	private final Map<Exploitable, Set<Exploit>> exploitsCache;
	
	private final String name;
	private final Map<Crawlable, Exploit> cheapestExploitCache;

	/**
	 * A Scenario is tied to a specific system model, attacker profile and exploit set.
	 * @param name TODO
	 * @param sysModel The system model under consideration for an attacker.
	 * @param ap An attacker profile for a system model.
	 * @param exploits A set of exploits for an attacker.
	 */
	public AttackScenario(String name, SystemModel sysModel, AttackerProfile ap, Set<Exploit> exploits){
		this.name = name;
		availableExploits = exploits;
		m_SystemModel = sysModel;
		m_AttackerProfile = ap;
		exploitsCache = new HashMap<Exploitable, Set<Exploit>>(exploits.size());
		cheapestExploitCache = new HashMap<Crawlable, Exploit>(sysModel.getSoftwareSet().size() + sysModel.getAttractors().size());
		
		// Determine cheapest Exploit.
		double cheapest = Double.POSITIVE_INFINITY;
		for (Exploit e : exploits) {
			double cost = cost(e).getMoneyExpectedValue();
			if (cost < cheapest) {
				cheapest = cost;
			}
		}
		cheapestExploit = cheapest;
	}

	public void finalize() throws Throwable {
	}

//	public AttackGraph getGraph() {
//			return m_AttackGraph;
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AttackScenario [name=" + name + "]";
	}

//	public void reset() {
//		attackGraphDone = false;
//		m_AttackGraph = new AttackGraph(name + " Attack Graph", this);
//	}
//
//	public boolean isAttackGraphDone() {
//		return attackGraphDone;
//	}
//
//	public void setAttackGraphDone() {
//		attackGraphDone = true;
//		// TODO Auto-generated method stub
//	}

	public SystemModel getSystemModel() {
		return m_SystemModel;
	}

	public Set<Software> getEntryTasks() {
		return m_AttackerProfile.getEntryTasks();
	}

	public Set<Asset> getAttractors() {
		return m_SystemModel.getAttractors();
	}

	
//	/**
//	 * Choose the cheapest exploit.
//	 * And check for exploit conditions being met for attacking a certain attractor.
//	 * @param target The target attractor being exploited.
//	 */
//	public Exploit chooseCheapestExploit(CrawledSoftware origin, Attractor target) {
//		// collect possible exploits.
//		Set<Exploit> consideredExploits = new HashSet<Exploit>(); 
//		for (Exploit e : getAllExploitsAgainstAttractor(target)) {
//			if (e.areSwCapComConditionsMet(origin.getTask(), target))
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


	/**
	 * Choose the cheapest exploit.
	 * Automatically choose an appropriate method and cast the parameter.
	 * Complexity: O(E) and ? as of 1,2,3,4,6,7,8.
	 * @param origin The originating node, either CrawledSoftware or CrawledAccess
	 * @param target The newly reachable node about being exploited.
	 */
	public Exploit chooseCheapestExploit(Crawlable origin, Crawlable target) {
		if (origin instanceof CrawledComMedia) {
			return chooseCheapestExploit((CrawledComMedia)origin, target); // Complexity: ?
		}
		if (origin instanceof CrawledSoftware) {
			return chooseCheapestExploit((CrawledSoftware)origin, target); // Complexity: O(E) as of 1,2,3,4,6,7,8.
		} else
		if (origin instanceof CrawledAccess) {
			return chooseCheapestExploit((CrawledAccess)origin, target);
		} else
		throw new IllegalArgumentException("Origin must be a object of CrawledComMedia, CrawledSoftware or CrawledAccess!");
	}

	
	/**
	 * Choose the cheapest exploit.
	 * And check for exploit conditions being met for attacking a certain task.
	 * Complexity: O(E) as of 1,2,3,4,6,7,8.
	 * @param target The newly reachable node about being exploited.
	 */
	public Exploit chooseCheapestExploit(CrawledSoftware origin, Crawlable target) {
		Exploit cheapestExploit = cheapestExploitCache.get(target);
		if (cheapestExploit == null) {
			cheapestExploit = chooseCheapestExploit(origin.getAllExploitsAgainstTarget(target));
			cheapestExploitCache.put(target, cheapestExploit);
		}
		return cheapestExploit;	}

	/**
	 * Choose the cheapest exploit.
	 * And check for exploit conditions being met for attacking a certain Software node.
	 * Complexity: O(E) as of 1,2,3,4,6,7,8.
	 * @param target The newly reachable node about being exploited.
	 */
	public Exploit chooseCheapestExploit(CrawledComMedia origin, Crawlable target) {
		Exploit cheapestExploit = cheapestExploitCache.get(target);
		if (cheapestExploit == null) {
			cheapestExploit = chooseCheapestExploit(origin.getAllExploitsAgainstTarget(target));
			cheapestExploitCache.put(target, cheapestExploit);
		}
		return cheapestExploit;
	}

	/**
	 * Choose the cheapest exploit.
	 * And check for exploit conditions being met for attacking a certain task.
	 * @param target The newly reachable node about being exploited.
	 */
	public Exploit chooseCheapestExploit(CrawledAccess origin, Crawlable target) {
		Exploit cheapestExploit = cheapestExploitCache.get(target);
		if (cheapestExploit == null) {
			cheapestExploit = chooseCheapestExploit(getAllExploitsAgainstTask(target.getExploitable()));
			cheapestExploitCache.put(target, cheapestExploit);
		}
		return cheapestExploit;
	}

	/**
	 * Choose the cheapest exploit among a set of applicable exploits.
	 * Complexity: O(consideredExploits)
	 * @param consideredExploits - must be applicable against target. No further checks here intended.
	 * @return The cheapest exploit among the set of exploits.
	 */
	private Exploit chooseCheapestExploit(Set<Exploit> consideredExploits) {
		// TODO: Cost calculation should be done less often! Cache somewhere!
		Exploit cheapestExploit = null;
		double cheapestExploitCost = Double.POSITIVE_INFINITY;
		for (Exploit e : consideredExploits) {
			double cost = e.costsForAttacker(m_AttackerProfile).getMoneyExpectedValue();
			// TODO Es sollten die Exploit-Kosten mit Unsicherheit verarbeitet werden, weniger die Angreifer.
			if (cost < cheapestExploitCost) {
				cheapestExploit = e;
				cheapestExploitCost = cost; 
			}
		}
		return cheapestExploit;
	}

	
	
	/**
	 * Return all exploits that have a certain task as a target.
	 * Complexity: O(E) as of 6.
	 * @param exploitable
	 * @return
	 */
	public Set<Exploit> getAllExploitsAgainstTask(Exploitable exploitable) {
		Set<Exploit> exploits = exploitsCache.get(exploitable); // query cache.
		if (exploits == null) { // cache miss?
			// create cache entry
			exploits = new HashSet<Exploit>();
			exploitsCache.put(exploitable, exploits);
			for (Exploit e : availableExploits) { // Complexity: O(E)
				if (e.isApplicableAgainstSw(exploitable)) // Complexity: O(1) as of 6.
					exploits.add(e); // Complexity: O(1).
			}
		}
		return Collections.unmodifiableSet(exploits);
	}

	/**
	 * Complexity: O(1) as of 5.
	 * @param expandingCrawlable
	 * @return
	 */
	public double costInvestedSoFar(CrawlableInterface expandingCrawlable) {
		return expandingCrawlable.getMinCostMeanTilHere();
		// TODO Großer Umbau zu einem Suchgraphen, damit das SystemModell frei von Fremddaten wird und nicht bereinigt werden muss. 
	}


	/**
	 * Query costs for an exploit.
	 * @param e The exploit to be queried for.
	 * @return The cost for creating (if necessary) and adapting the exploit.
	 */
	public Resources cost(Exploit e) {
		return e.costsForAttacker(m_AttackerProfile);
	}

	/**
	 * Query for the minimum cost left over to get to an attractor.
	 * The minimum cost prediction is required for making an A* search expansion-optimal.
	 * @param target
	 * @return
	 */
	public double costMinimallyRemaining(Crawlable target) {
		return cheapestExploit * target.getHopDistanceToAttractors();
	}

	/**
	 * 
	 * @param cs
	 * @return path costs up to this crawled software including a prediction for the rest.
	 */
	public double costPredicted(Crawlable cs) {
		return costInvestedSoFar(cs) + costMinimallyRemaining(cs);
	}

	public Access getAccessNodeFor(Crawlable crawlable) {
		if (crawlable instanceof CrawledSoftware) {
			Software t = ((CrawledSoftware) crawlable).getSoftware();
			for (Access a : m_AttackerProfile.getAccesses()) {
				if (a.getSysNode().getControlledTasks().contains(t))
					return a;
			}
		} 
		return null;
	}

	public AttackerProfile getAttackerProfile() {
		return m_AttackerProfile;
	}
	
	
	
}//end Attack Scenario