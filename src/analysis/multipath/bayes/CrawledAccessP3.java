/**
 * 
 */
package analysis.multipath.bayes;

import analysis.Crawlable;
import analysis.CrawledAccess;
import attackGraph.AttackScenario;
import attackerProfile.Access;

/**
 * @author Martin Salfer
 * @created 2016-04-04
 *
 */
public class CrawledAccessP3 extends CrawledAccess {

	/**
	 * @param scenario
	 * @param access
	 */
	public CrawledAccessP3(AttackScenario scenario, Access access) {
		super(scenario, access);
	}

	/**
	 * Compare according to costs having had so far.
	 * That allows a breadth-first search.
	 */
	@Override
	public int compareTo(Crawlable cs) {
		if (cs == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return (int) (getMinCostMeanTilHere() - cs.getMinCostMeanTilHere()); 
	}

}
