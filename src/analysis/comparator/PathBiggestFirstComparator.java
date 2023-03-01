/**
 * 
 */
package analysis.comparator;

import java.io.Serializable;
import java.util.Comparator;

import analysis.multipath.salfer.Path;
/**
 * Inverse the natural ordering of Path objects.
 * This is useful for ordering the biggest to the front.
 * 
 * @author Martin Salfer
 * @created 26.02.2017 22:14:52
 *
 */
public class PathBiggestFirstComparator implements Comparator<Path>, Serializable {

	private static final long serialVersionUID = -858080676195722365L;

	@Override
	public int compare(Path left, Path right) {
		if (left == null || right == null)
			throw new NullPointerException("Comparision is disallowed for null.");
		return - left.compareTo(right); 
	}

}



