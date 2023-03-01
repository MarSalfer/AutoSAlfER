package systemModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import types.Resources;
import attackerProfile.Motive;



/**
 * An Attractor represents something attractive for attackers.
 * 
 * This value assumes how much worth would be inflicted if one attacker would
 * reach this attractor.
 * 
 * Examples are financially exploitable credentials or car unlock codes.
 * 
 * The value sums up the potential damage for all stakeholders: customers, OEM,
 * third parties.
 * Comprises financially exploitable assets, e.g., saved payment credentials.
 * 
 * 
 * Vehicles require a high level of safety and security.
 * We assume a mixture of safety and security goals within one system.
 * The safety security mix implies forming goals compatible for security and also
 * safety considerations.
 * The safety security mix implies considering possible effects onto safety goals
 * when security incidents happen, even though this thesis is primarily about
 * security.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class Asset extends Node implements Exploitable {

	/**
	 * It categorizes attractors according to the motive they might be attractive for.
	 * 
	 * Example motives are financial gain, reputation, inflicting injury or unlocking
	 * features.
	 * 
	 * The value of the map shows the monetary value for the free market.
	 * 
	 * The classification helps attack profiler finding and associating attractors to
	 * attacker motivation.
	 * 
	 * The standard deviation represents an uncertainty in the value for potential,
	 * overall damage for all stakeholders, customers, OEM, third parties. The
	 * standard deviation supports the expression of the expected value of an
	 * attractor for a Gaussian distribution.
	 * @deprecated Use marketValue instead.
	 */
	@Deprecated
	private final Map<Motive, Resources> possibleMotivesAndValue = new HashMap<Motive,Resources>();

	@XmlElement
	private final Resources marketValue;
	/**
	 * What ratio of cars is equipped with this attractor?
	 * 
	 * For example: Almost 100 % of cars are equipped with a vehicle immobiliser, but
	 * only a small percentage is equipped with rear seat entertainment.
	 */
//	@XmlAttribute
	private double takeRate = 1d; // TODO include in Analyzer calculations.
	
	/**
	 * How many Assets can be exploitet presumably.
	 * Restrictions apply as only a limited number of cars has a certain asset or
	 * as after a certain amount of exploitations some counter measures will become effective.
	 */
	private int maxPossibleExploitations; // TODO include in Analyser calculations.
	
	private final static int UNLIMITED_NUMBER = -1;
	/**
	 * The software component onto which this attractor is attached to.
	 */
	@XmlElement
	private final Software parent;

	public Asset(Software parent){
		this("<noname>", parent);
	}

	public Asset(String name, Software parent) {
		this(name, parent, null);
	}

	public Asset(String name, Software parent, Resources marketValue) {
		this(name, parent, marketValue, UNLIMITED_NUMBER);
	}
	
	
	public Asset(String name, Software parent, Resources marketValue, int maxExploitations) {
		super(name);
		this.parent = parent;
		if (parent != null) {
			parent.registerAsset(this);
		} else {
			throw new IllegalArgumentException("Asset constructor was called with a null parameter. Specifcy software parent.");
		}
		this.marketValue = marketValue;
		parent.registerAsset(this);
	}

	@Override
	public String toString() {
		return "Asset [" + name + "]"; //expectedValue=" + expectedValue
//				+ ", possibleMotive=" + possibleMotive + ", standardDeviation="
//				+ standardDeviation + ", takeRatio=" + takeRatio + "]";
	}


	public void finalize() throws Throwable {
		super.finalize();
	}


	/* (non-Javadoc)
	 * @see systemModel.Node#getAssociatedTasks()
	 */
	@Override
	public Set<Software> getControlledTasks() {
		Set<Software> s = new HashSet<Software>();
		s.add(getParentTask());
		return s;
	}


	public Software getParentTask() {
		return parent;
	}

	/**
	 * Get all tasks this node can reach: None. An attractor is considered a passive entity.
	 */
	@Override
	public Set<Software> getReachableSoftwareComplete() {
		return null;
	}


	@Override
	public Set<Capability> getCapabilities() {
		return null; // intentionally null as attractors are passive and do not possess capabilities.
	}


	/**
	 * An attractor is passive and therefore has no capabilities.
	 * @return An empty set.
	 */
	@Override
	public Set<CommunicationMedium> getComNodesWith(Software origin) {
		return new HashSet<CommunicationMedium>(); // Intentionally empty, an attractor has no capabilities.
	}

	/**
	 * Get the (black) market value of an attractor.
	 * 
	 * This potential value expresses the potential, overall damage for all stakeholders,
	 * customers, OEMs, third parties. The expected value represents an average damage
	 * value (and is supported by the standard deviation).
	 * 
	 * @return A resources' potential value.
	 */
	public Resources getMarketValue() {
		return marketValue;
	}

	/**
	 * Get the possible motives for an attractor including the expected intensity.
	 * @return An unmodifiable mapping from motive to intensity.
	 * @deprecated No motives were opted out. Use only getMarketValue() instead.
	 */
	@Deprecated
	public Map<Motive, Resources> getPossibleMotives() {
		return Collections.unmodifiableMap(possibleMotivesAndValue);
	}

	public String getName() {
		return super.name;
	}
	
	
}//end Attractor