package analysis.multipath.bayes;

import analysis.Crawlable;
import analysis.CrawledAsset;
import attackGraph.AttackScenario;
import systemModel.Asset;

/**
 * 
 * @author Martin Salfer
 * @created 2016-04-04
 *
 */
public class CrawledAssetP3 extends CrawledAsset {

	public CrawledAssetP3(AttackScenario scenario, Asset attractor) {
		super(scenario, attractor);
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
