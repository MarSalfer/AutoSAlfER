package attackerProfile;

import java.util.HashMap;

import systemModel.Asset;
import types.Resources;



/**
 * The motivation is a connection object for attack motives. Motivation objects
 * are weighted for associating motives with an attacker and a weight as different
 * attackers have a often at least slightly different motivation and priority for
 * their attack efforts and attractor selection.
 * Deprecated: Use the attacker profiles motivation field variable instead. This
 * class was deprecated to simplify the model.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
@Deprecated
public class Motive {

	/**
	 * The factor is a linear factor that precedes the attractor value.
	 * This factor allows amplifying or lowering the motivation for a motive.
	 * Deprecated: Please use an attack profile's and attractor's map value for assigning
	 * a factor.
	 */
	@Deprecated
	private final float factor;
	private final String name;
	private final HashMap<Asset,Resources> desiredAttractors = new HashMap<Asset,Resources>();

	public Motive(){
		this("<NoName>", 0f);
	}
	
	public Motive(String name) {
		this(name, 0);
	}
	
	
	/**
	 * Deprecated: Please do not use this factor, but set the attractor's or
	 * attack profile's factor in the corresponding map value.
	 * @param name
	 * @param factor
	 */
	@Deprecated  
	public Motive(String name, float factor) {
		this(name, factor, null);
	}

	/**
	 * Deprecated: Please do not use this factor, but set the attractor's or
	 * attack profile's factor in the corresponding map value.
	 * 
	 * @param name
	 * @param factor
	 * @param attractor
	 */
	@Deprecated
	public Motive(String name, float factor, Asset attractor) {
		this.factor = factor;
		this.name = name;
		if (attractor != null) {
			desiredAttractors.put(attractor, new Resources());
		}
	}
	
	public String toString() {
		return this.name + factor;
	}

	/**
	 * Add an attractor that a motivation is to desire.
	 * Rejects null attractors.
	 * Deprecated: Please use method below, which also indicates the 
	 * motivation intensity for an attractor.
	 * @param attractor
	 */
	@Deprecated
	public void desires(Asset attractor) {
		if (attractor != null)
			desiredAttractors.put(attractor, new Resources());
	}

	/**
	 * Define how much an attacker wants a certain attractor.
	 * @param attractor The attractor under consideration.
	 * @param attackerValue How value bears reaching an attractor for an attacker.
	 */
	public void desires(Asset attractor, Resources attackerValue) {
		desiredAttractors.put(attractor, attackerValue);
	}

	
}
//end Motivation
