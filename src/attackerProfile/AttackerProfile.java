package attackerProfile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import systemModel.Asset;
import systemModel.Software;
import types.Resources;
import exploit.PotentialExploit;

/**
 * An Attacker Profile models a typical attacker, considered for the evaluation.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class AttackerProfile {
	
	@XmlAttribute
	private final String name;
	
	/**
	 * How many independent attackers are considered? This number will influence the
	 * overall damage expected from this group as it will be taken as a multiplier for
	 * attacks happening in parallel.
	 * 
	 * In case of a group, it is the number of active attack groups, not their
	 * individuals. The groups' individuals share their skills and resources and are
	 * therefore taken a part of the attacker group. Each attacker group must have
	 * a separate set of targetable vehicles as the analyzer ultimately multiplies
	 * the attacker group number with the number of targetable vehicles.
	 * (Attacking a vehicle several times and getting into a saturation is not considered
	 * in this Analyzer model.)
	 * 
	 * Note: This number is NOT an ID.
	 */
	@XmlAttribute
	private final int expectedNumber;
	
	
	/**
	 * @deprecated Better use motivation. 
	 */
	@Deprecated
	private final Map<Motive,Double> motivationOld = new HashMap<Motive,Double>();
	/**
	 * How much does this attacker value an attractor.
	 */
	private final Map<Asset,Resources> motivationAndValue = new HashMap<Asset,Resources>();
	
	/**
	 * Budget is defined as a mean budget plus a deviation.
	 */
	@XmlElement
	private final Resources budget;
	/**
	 * Budget in case of a two sigma deviation on top. Extra value as it will be queried often.
	 */
	private final double budgetPlusTwoSigma;
	
//	@XmlElementWrapper // (name = "extraExploits")
	private final Set<PotentialExploit> extraExploits = new HashSet<PotentialExploit>();
	
	@XmlElementWrapper (name = "accessSet")
	private final Set<Access> access = new HashSet<Access>();
	/**
	 * Skill proficiency of the attackers. Any not mentioned skill is taken as 1, which means human average.
	 */
	private final Map<Skill, Float> skills = new HashMap<Skill, Float>();

	public AttackerProfile(String name, int expectedNumberOfAttackers, Resources resources) {
		this.name = name;
		expectedNumber = expectedNumberOfAttackers;
		budget = resources;
		budgetPlusTwoSigma = resources.getMoneyExpectedValue() + 2 * resources.getMoneyStandardDeviation();
	}
	
	/**
	 * Implicitly assumes a budget of 100 000.
	 * @param name
	 * @param expectedNumberOfAttackers
	 */
	@Deprecated
	public AttackerProfile(String name, int expectedNumberOfAttackers) {
		this(name, expectedNumberOfAttackers, new Resources(100_000d, 0d));
	}
	
	public AttackerProfile(String string) {
		this(string,0);
	}

	public AttackerProfile(){
		this("noname");
	}
	
	public String toString() {
		return name; // + expectedNumber + motivations + m_Resources + extraExploits + access + skills;
	}

	public void finalize() throws Throwable {
	}

	/**
	 * Add a motive.
	 * @param motive
	 * @see #desires(Asset, Resources)
	 */
	@Deprecated
	public void addMotivation(Motive motive) {
		motivationOld.put(motive, 1d);
	}

	/**
	 * Set how much an attacker is driven by a certain motivation.
	 * @param motive
	 * @param intensity
	 * @see #desires(Asset, Resources)
	 */
	@Deprecated
	public void addMotivation(Motive motive, double intensity) {
		motivationOld.put(motive, intensity);
	}
	
	public void addSkill(Skill skill, Float proficiency) {
		this.skills.put(skill, proficiency);
	}

	public void addAccess(Access access) {
		this.access.add(access);
	}

	public Resources getResources() {
		return budget;
	}

	/**
	 * Returns all software objects an attacker can access directly.
	 * @return The set of directly accessible software objects.
	 */
	public Set<Software> getEntryTasks() {
		Set<Software> entryTasks = new HashSet<Software>();
		for (Access a : access) {
			entryTasks.addAll(a.getSysNode().getControlledTasks());
		}
		return entryTasks;
	}

	public float availableProficiencyForSkill(Skill s) {
		return skills.get(s);
	}

	public Set<Access> getAccesses() {
		return access;
	}

	/**
	 * Get how much an attacker values a certain attractor.
	 * @param attractor The desired attractor.
	 * @return The resources an attacker would spend for this attractor.
	 */
	public Resources getValueforAttractor(Asset attractor) {
		Resources result = motivationAndValue.get(attractor);
		
		if (result == null) { // if no specific value is set, consider the attractor's market value.
			result = attractor.getMarketValue();
		}
		return result;
	}

	/**
	 * Set how much an attacker values a certain attractor.
	 * It defines how much an attacker would spend in time and
	 * money to reach a certain attractor.
	 * @param attractor The attractor the attacker values.
	 * @param value The value for an attacker.
	 */
	public void desires(Asset attractor, Resources value) {
		motivationAndValue.put(attractor, value);
	}

	/**
	 * Set that an attacker values a certain attractor.
	 * The money and time value is null and will therefore be
	 * collected from the attractor's market value.
	 * @param attractor The attractor the attacker values.
	 */
	public void desires(Asset attractor) {
		desires(attractor, null);
	}
	
	/**
	 * Wrapper for desire(Asset) method.
	 * @param assets see desire method.
	 */
	public void desires(Asset[] assets) {
		for (Asset a : assets) {
			desires(a);
		}
	}

	/**
	 * Get how many attackers are considered with this profile.
	 * @return Number of attackers.
	 */
	public int getNumberOfAttackers() {
		return expectedNumber;
	}

	/**
	 * @return The maximum budget of an attacker plus a two sigma deviation on top.
	 */
	public double getMaximumBudgetPlusTwoSigma() {
		return budgetPlusTwoSigma;
	}
	
}//end Attacker Profile