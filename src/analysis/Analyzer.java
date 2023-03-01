package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import systemModel.Asset;
import types.Resources;
import attackGraph.AttackGraph;
import attackerProfile.Access;
import exploit.Exploit;

/**
 * The analyser examines attack graphs for their economical estimation: attack
 * cost, gain, profit, ROI, attacker probabilities and expected damage.
 * 
 * The overall cost is a sum of access and exploit costs.
 * 
 * This class summarises high-level calculations, so no instantiation
 * or data persistence is considered here.
 * 
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:07
 */
public abstract class Analyzer {

	/** Random Number Generator for speeding up gaussian distribution class instantiation.
	 */
	static RandomGenerator rng = new Well19937c();

    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

	public void finalize() throws Throwable {
	}

	/**
	 * Query for an attackers maximum cost expected.
	 * 
	 * This cost calculation assumes that an attacker will access all
	 * vehicles he can and apply all exploits and catch all attractors.
	 * (The Access object number of vehicles accessible
	 * is taken as an number of accessed vehicles. This also distributes the initial
	 * exploit costs over many targets and makes the business case usually profitable.)
	 *
	 * Logic is delegated to the graph as those knows the internal structure
	 * best for traversing and collecting data.
	 * 
	 * @param graph
	 * @return
	 * @deprecated Use expectedAttackerCostResources() instead.
	 */
	@Deprecated
	public static float costForAllFloat(AttackGraph graph) {
		return graph.calculateAttackerCostExpectedMax();
	}
	

	/**
	 * Calculate the maximum cost deviation: an attacker uses exploits and accesses
	 * for all targetable vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * @param graph The attack graph to be analysed.
	 * @return The cost deviation for all necessary efforts.
	 * @deprecated Use expectedAttackercostResources(x) instead.
	 */
	@Deprecated
	public static float expectedAttackerCostDeviation(AttackGraph graph) {
		return costForAll(graph).getMoneyStandardDeviationFloat();
	}

	/**
	 * Query for an attackers maximum cost expected.
	 * 
	 * This cost calculation assumes that an attacker will access all
	 * vehicles he can and apply all exploits and catch all attractors.
	 * (The Access object number of vehicles accessible
	 * is taken as an number of accessed vehicles. This also distributes the initial
	 * exploit costs over many targets and makes the business case usually profitable.)
	 *
	 * @param graph
	 * @return
	 */
	public static Resources costForAll(AttackGraph graph) {
		List<Resources> costs = new LinkedList<Resources>();
		
		/* Collect applied exploits */
		for (Exploit e : graph.getAllAppliedExploits()) {
			costs.add(graph.getAttackerSpecificAdaptionCostForExploit(e));
		}
		/* Collect used accesses */
		for (Access e : graph.getAllUsedAccesses()) {
			costs.add(e.getMaxmimumAccessCost());
		}

		/* Cost Deviation calculation */
		final Resources result = Resources.addUpCollection(costs);
		return result;
	}
	
	/**
	 * Get the maximum attacker gain for an attacker.
	 * Dissimilar implementation for higher assurance.
	 * @param graph The graph under consideration.
	 * @return The maximum attacker gain.
	 */
	public static Resources gainMaxForAll(AttackGraph graph) {
		Set<Resources> gains = new HashSet<Resources>();
		for (Asset attractor : graph.assets()) {
			final Resources gainSingle = graph.getScenario().getAttackerProfile().getValueforAttractor(attractor);
			final Access access = graph.getUsedAccessForAsset(attractor); 
			gains.add(gainSingle.multiplyWith(access.getNumberOfTargetAbleVehicles()));
		}
		return Resources.addUpCollection(gains);
	}
	
	
	/**
	 * Calculate the maximum cost deviation for a certain attractor: an attacker uses only
	 * exploits and accesses necessary for a certain attractors and applies them
	 * on a maximum number of vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * @param graph The attack graph to be analysed.
	 * @return The cost deviation for all necessary efforts.
	 */
	public static float expectedAttackerCostDeviationForAttractor(AttackGraph ag, Asset attractor) {
		List<Resources> costs = collectCostsForAttractor(ag, attractor);
		return Resources.addUpCollection(costs).getMoneyStandardDeviationFloat();
	}

	/**
	 * Collect all resources necessary for exploiting an attractor.
	 * @param ag The attack graph to analyse.
	 * @param attractor - The attractor to look for.
	 * @return costs
	 */
	public static List<Resources> collectCostsForAttractor(AttackGraph ag, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return costsForAttractors(ag, attractors);
	}

	/**
	 * Collect all resources necessary for exploiting a set of attractors.
	 * @param ag - The attack graph to analyse.
	 * @param attractor - The attractor to look for.
	 * @return costs
	 */
	private static List<Resources> costsForAttractors(AttackGraph ag, Set<Asset> assets) {
		List<Resources> costs = new ArrayList<Resources>(ag.getNumberOfEdges());
		Set<Exploit> exploits = new HashSet<Exploit>();
		Set<Access> access = new HashSet<Access>();

		/* Collect all necessary exploits and accesses. */
		for (Asset asset : assets) {
			for (Exploit e : ag.getAllAppliedExploitsForAsset(asset)) {
				exploits.add(e);
			}
			
			for (Access a : ag.getAllUsedAccessesForAsset(asset)) {
				access.add(a);
			}
		}
		
		/* Collect all costs for necessary exploits. */
		for (Exploit e : exploits) {
			costs.add(ag.getAttackerSpecificAdaptionCostForExploit(e));
		}
		
		/* Collect all costs for necessary accesses for an attractor and consider a maximum target number */
		for (Access a : access) {
			costs.add(a.getAccessCost().multiplyWith(a.getNumberOfTargetAbleVehicles()));
		}
		
		return costs;
	}
	

	/**
	 * Calculate the maximum expected cost for a certain attractor: an attacker uses only
	 * exploits and accesses necessary for a certain attractors and applies them
	 * on a maximum number of vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * @param graph The attack graph to be analysed.
	 * @return The expected cost at max for all necessary efforts.
	 * @deprecated use costMaxForAttractor() instead.
	 */
	@Deprecated
	public static float costMaxForAttractorFloat(AttackGraph ag, Asset attractor) {
		return costMaxForAttractor(ag, attractor).getMoneyExpectedValueFloat();
	}

	/**
	 * Calculate the maximum expected cost for a certain attractor: an attacker uses only
	 * exploits and accesses necessary for a certain attractors and applies them
	 * on a maximum number of vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * @param graph The attack graph to be analysed.
	 * @return The expected cost at max for all necessary efforts.
	 */
	public static Resources costMaxForAttractor(AttackGraph ag, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		final Resources result = Resources.addUpCollection(costsForAttractors(ag, attractors));
		return result;
	}

	/**
	 * Calculate the maximum expected cost for a certain set of attractor: an attacker uses only
	 * exploits and accesses necessary for reaching those attractors
	 * and applies them on a maximum number of vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * 
	 * @param graph The attack graph to be analysed.
	 * @param attractors The attractors the be exploited.
	 * @return The expected cost at max for all necessary efforts.
	 */
	public static Resources costMaxForAttractors(AttackGraph graph, Set<Asset> attractors) {
		List<Resources> costs = costsForAttractors(graph, attractors);
		return Resources.addUpCollection(costs);
	}

	/**
	 * Calculate the maximum expected cost for a certain set of attractor: an attacker uses only
	 * exploits and accesses necessary for reaching those attractors
	 * and applies them on a maximum number of vehicles.
	 * This approach needs only one square root operation (instead of square rooting
	 * it at every resources addition).
	 * @param graph The attack graph to be analysed.
	 * @param attractors The attractors the be exploited.
	 * @return The expected cost at max for all necessary efforts.
	 * @deprecated Use costMaxForAttractors() instead.
	 */
	@Deprecated
	public static float costMaxForAttractorsFloat(AttackGraph ag, Set<Asset> attractors) {
		List<Resources> costs = costsForAttractors(ag, attractors);
		return Resources.addUpCollection(costs).getMoneyExpectedValueFloat();
	}

	/** Calculate what probability of attackers can afford attacking
	 * a certain set of attractors.
	 * 
	 * @param ag - The considered attack graph.
	 * @param attractors - The considered attractors.
	 * @return The ratio of attackers able to afford attacking all given attractors completely.
	 */
	public static double howProbableAttackersCanAffordTheAttacksOnto(AttackGraph ag, Set<Asset> attractors) {
		/* Collect costs. */
		List<Resources> costs = costsForAttractors(ag, attractors);
		return calculateAttackerAffordability(ag, costs);
	}

	/** Calculate how probable an attacker can afford the attacks.
	 * @param graph - The graph under consideration.
	 * @param costs - Each cost for all exploits and accesses.
	 * @return The ratio of attackers that can afford given costs.
	 */
	private static double calculateAttackerAffordability(AttackGraph graph, List<Resources> costs) {
		/* Collect budget. */
		Resources budget = graph.getScenario().getAttackerProfile().getResources();

		return calculateAttackerAffordability(costs, budget);
	}

	/** Calculate how probable an attacker can afford the costs.
	 * @param costs - Each cost for all exploits and accesses.
	 * @param budget - an attackers budget.
	 * @return The probability that an attacker can afford given costs.
	 */
	public static double calculateAttackerAffordability(List<Resources> costs, Resources budget) {
		final Resources cost = Resources.addUpCollection(costs);
		
		return calculateAttackerAffordability(budget, cost);
	}

	/** Calculate how probable an attacker can afford the costs.
	 * @param budget - an attackers budget.
	 * @param cost cost for all exploits and accesses
	 * @return The probability that an attacker can afford given costs.
	 */
	public static double calculateAttackerAffordability(final Resources budget, final Resources cost) {
		if (budget == null || cost == null) {
			return 0d; // IllegalArgumentException made JUnit-friendly.
		}
		/* Balance resources. */
		final Resources balanceRes = budget.substractBy(cost);
		
		/* Statistical evaluation. */
		return probabilityOfPositiveOutcome(balanceRes.getMoneyExpectedValue(), balanceRes.getMoneyStandardDeviation());
	}

	/** Calculate how probable a positive outcome is.
	 * @param balance - Usually the difference "budget - costs".
	 * @param standardDeviation - The standard deviation of the balance. 
	 * @return
	 */
	public static double probabilityOfPositiveOutcome(double balance, double standardDeviation) {
		NormalDistribution dist = new org.apache.commons.math3.distribution.NormalDistribution(rng, balance, standardDeviation, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		return 1 - dist.cumulativeProbability(0);
	}

	/** Calculate what ration of attackers can afford attacking
	 * a certain attractor.
	 * 
	 * @param ag - The considered attack graph.
	 * @param attractor - The considered attractor.
	 * @return The ratio of attackers able to afford attacking the given attractor.
	 */
	public static double howManyAttackersCanAffordTheAttacksOnto(AttackGraph ag, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return howProbableAttackersCanAffordTheAttacksOnto(ag, attractors);
	}

	/** Calculate what ration of attackers can afford attacking
	 * everything the graph offers.
	 * Note: This methods implements the cost collection differently from 
	 * howManyAttackersCanAffordTheAttacksOnto(Graph, Attractor) for
	 * a higher assurance of the graphs and results correctness.
	 * @param graph - The attack graph to be considered.
	 * @return The ration of attackers that could afford all attacks in the graph.
	 */
	public static double howManyAttackersCanAffordTheAttacks(AttackGraph graph) {
		/* Dissimilar collection of cost resources */
		List<Resources> costs = new ArrayList<Resources>(graph.getNumberOfEdges()+graph.getNumberOfAccesses());
		for (Exploit e : graph.getAllAppliedExploits()) {
			costs.add(graph.getAttackerSpecificAdaptionCostForExploit(e));
		}
		for (Access access : graph.getAllUsedAccesses()) {
			costs.add(access.getAccessCost().multiplyWith(access.getNumberOfTargetAbleVehicles()));
		}
		
		/* Statistical evaluation */
		return calculateAttackerAffordability(graph, costs);
	}

	/**
	 * Get how much an attacker would spend for reaching an attractor.
	 * @param graph The attack graph to be analyzed
	 * @param attractor The attractor.
	 * @return Expected attacker gain for an attractor for this attacker.
	 */
	public static Resources expectedAttackerGain1ForSingleAttractor(AttackGraph graph, Asset attractor) {
		return graph.getScenario().getAttackerProfile().getValueforAttractor(attractor);
	}
	
	/**
	 * Get how much an attacker would spend for reaching this attractor at all available cars.
	 * @param graph The attack graph to be analysed
	 * @param attractor The attractor.
	 * @return Expected money for an attractor for this attacker.
	 */
	public static Resources gainMaxForAttractor(AttackGraph graph, Asset attractor) {
		/* Get relevant access attractor. */
		final int numberOfTargetableVehicles = graph.getUsedAccessForAsset(attractor).getNumberOfTargetAbleVehicles();
		
		return expectedAttackerGain1ForSingleAttractor(graph, attractor).multiplyWith(numberOfTargetableVehicles);
	}

	/**
	 * Get how much an attacker would spend for reaching this attractor at all available cars.
	 * @param graph The attack graph to be analysed
	 * @param attractor The attractor.
	 * @return Expected money for an attractor for this attacker.
	 */
	public static Resources gainMaxForAttractors(AttackGraph graph, Set<Asset> attractors) {
		Set<Resources> gainSet = new HashSet<Resources>(attractors.size());
		for (Asset attractor : attractors) {
			gainSet.add(gainMaxForAttractor(graph, attractor));
		}
		return Resources.addUpCollection(gainSet);
	}

	/**
	 * How much would an attacker profit from an attractor.
	 * Cost stems from exploits and accesses, revenue from attractor exploitation. 
	 * @param graph The graph to be analysed.
	 * @param attractor The attacked attractor.
	 * @return The profit.
	 */
	public static Resources profitForAttractor(AttackGraph graph, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return profitForAttractors(graph, attractors);
	}

	/**
	 * How much would an attacker profit from an attractor.
	 * Cost stems from exploits and accesses, revenue from attractor exploitation. 
	 * @param graph The graph to be analysed.
	 * @param attractors The attacked attractor.
	 * @return The profit.
	 */
	public static Resources profitForAttractors(AttackGraph graph, Set<Asset> attractors) {
		final Resources gain = gainMaxForAttractors(graph, attractors);
		final Resources cost = costMaxForAttractors(graph, attractors);
		return gain.substractBy(cost);
	}


	/**
	 * Calculate the return on investment for a certain attractor.
	 * 
	 * @param graph The attack graph under consideration.
	 * @param attractor The attractor begin seized.
	 * @return
	 */
	public static Resources roiForAttractor(AttackGraph graph, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return roiForAttractors(graph, attractors);
	}

	/**
	 * Calculate the return on investment for attacking a set of attractors.
	 * 
	 * @param graph The attack graph under consideration.
	 * @param attractors The attractors begin seized.
	 * @return
	 */
	public static Resources roiForAttractors(AttackGraph graph, Set<Asset> attractors) {
		final Resources profit = profitForAttractors(graph, attractors);
		final Resources cost = costMaxForAttractors(graph, attractors);
		return profit.divideBy(cost);
	}

	/**
	 * Calculate the probability that this attractor is profitable to seize.
	 * @param graph The attack graph under consideration.
	 * @param attractor The attractor being seized.
	 * @return The probability that this attractor seizure is profitable for the attacker.
	 */
	public static double profitPositiveProbability(AttackGraph graph, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		final double posProfProb = profitPositiveProbability(graph, attractors);
		return posProfProb;
	}

	/**
	 * Calculate the probability that these attractors are profitable to seize.
	 * @param graph The attack graph under consideration.
	 * @param attractor The attractors being seized.
	 * @return The probability that these attractors seizure is profitable for the attacker.
	 */
	public static double profitPositiveProbability(AttackGraph graph, Set<Asset> attractors) {
		final Resources profit = profitForAttractors(graph, attractors);
		final double posProfProb = profitProbability(profit);
		return posProfProb;
	}
	
	/**
	 * Calculate the probability that the profit is positive at a given Gaussian distribution.
	 * 
	 * @param profit The attackers profit, i.e., revenue - cost.
	 * @return Probability that the profit is positive.
	 */
	private static double profitProbability(final Resources profit) {
		final double expectedValue = profit.getMoneyExpectedValue();
		final double standardDeviation = profit.getMoneyStandardDeviation();
		if (standardDeviation > 0.0) {
			final NormalDistribution normalDistribution = new NormalDistribution(expectedValue, standardDeviation);
			return 1 - normalDistribution.cumulativeProbability(0);
		} else {
			return 0.0;  // TODO check for correctness.
		}
	}

	/**
	 * Calculate the probability of a successful attack onto given attractors.
	 * Probability("Attack") = P("Affordable") * P ("Profitable").
	 * 
	 * @param graph The attack graph under consideration.
	 * @param attractors The attractors to be seized.
	 * @return The probability of a successful attack onto given attractors.
	 */
	public static double attackProbability(AttackGraph graph, Set<Asset> attractors) {
		final double probAffordable = Analyzer.howProbableAttackersCanAffordTheAttacksOnto(graph, attractors);
		final double probProfitable = Analyzer.profitPositiveProbability(graph, attractors);
		return probAffordable * probProfitable;
	}

	/**
	 * Calculate the probability of a successful attack onto a given attractor.
	 * Probability("Attack") = P("Affordable") * P ("Profitable").
	 * 
	 * @param graph The attack graph under consideration.
	 * @param attractor The attractor to be seized.
	 * @return The probability of a successful attack onto a given attractor.
	 */
	public static double attackProbability(AttackGraph graph, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return attackProbability(graph, attractors);
	}

	/**
	 * How big is the expected damage, i.e., the revenue of an attacker in total.
	 * Damage = Revenue/attacker * numberOfAttackers * attackProbability.
	 * 
	 * @param attackGraph The attack graph under consideration.
	 * @param attractors The attractors to be seized.
	 * @return The expected revenue in total.
	 */
	public static double damageExpected(AttackGraph attackGraph, Set<Asset> attractors) {
		final double damageSingle = gainMaxForAttractors(attackGraph, attractors).getMoneyExpectedValue();
		final int numberOfAttackers = attackGraph.getScenario().getAttackerProfile().getNumberOfAttackers();
		final double attackProbability = attackProbability(attackGraph, attractors);
		return damageSingle * numberOfAttackers * attackProbability;
	}

	/**
	 * How big is the expected damage, i.e., the revenue of an attacker in total.
	 * Damage = Revenue/attacker * numberOfAttackers * attackProbability.
	 * 
	 * @param attackGraph The attack graph under consideration.
	 * @param attractors The attractor to be seized.
	 * @return The expected revenue in total.
	 */
	public static double damageExpected(AttackGraph attackGraph, Asset attractor) {
		Set<Asset> attractors = new HashSet<Asset>(1);
		attractors.add(attractor);
		return damageExpected(attackGraph, attractors);
	}

	
}//end Analyzer