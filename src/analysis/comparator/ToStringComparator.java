/**
 * 
 */
package analysis.comparator;

import java.io.Serializable;
import java.util.Comparator;

import analysis.Crawlable;
/**
 * @author Martin Salfer
 * @created 2016-04-07
 *
 */
public class ToStringComparator implements Comparator<Crawlable>, Serializable {

	private static final long serialVersionUID = -4445850700845865725L;

	/** Compare according to names.
	 * Testing purposes.
	 */
	@Override
	public int compare(Crawlable left, Crawlable right) {
		if (left == null || right == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return left.toString().compareTo((right.toString())); 
	}
}

