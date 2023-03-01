package analysis.comparator;

import java.io.Serializable;
import java.util.Comparator;

import analysis.Crawlable;

/**
 * @author Martin Salfer
 * @created 2016-04-07
 */
public class CostSoFarComparator implements Comparator<Crawlable>, Serializable {

	private static final long serialVersionUID = -1851450076765602903L;

	/** Compare according to costs having had so far.
	 * That allows a breadth-first search.
	 */
	@Override
	public int compare(Crawlable left, Crawlable right) {
		if (left == null || right == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return (int) (left.getMinCostMeanTilHere() - right.getMinCostMeanTilHere()); 
	}

}
