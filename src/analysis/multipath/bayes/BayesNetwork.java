/**
 * 
 */
package analysis.multipath.bayes;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import unbbayes.prs.Edge;
import unbbayes.prs.Node;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.prs.bn.ProbabilisticNode;

/**
 * Analysis of Bayesian Networks.
 * 
 * @author Martin Salfer
 * @created 15.04.2016 16:04:20
 * @deprecated Use analysis.multipath.poincare instead!
 */
@Deprecated
public class BayesNetwork {

	private BayesNetwork() { // No instantiation allowed/necessary.
	}

	/**
	 * @param pn1
	 * @return
	 */
	public static String getGraphInDot(ProbabilisticNetwork pn) {
		return getGraphInDot(pn, false);
	}

	/**
	 * @param withProbabilities TODO
	 * @param pn1
	 * @return
	 */
	public static String getGraphInDot(ProbabilisticNetwork pn, boolean withProbabilities) {
		if (pn == null) return "";
		StringBuilder str = new StringBuilder();
		
		/* Network */
		str.append("digraph V2Evo" + sanitize(pn.getName()) + " {\n");
		
		/* Nodes */
		for (Node n : pn.getNodes()) {
			str.append("node " + getNameWithProbability(n, withProbabilities)); 
			str.append(";\n");
		}

		/* Edges */
		for (Edge e : pn.getEdges()) {
			str.append(getNameWithProbability(e.getOriginNode(), withProbabilities));
			str.append(" -> ");
			str.append(getNameWithProbability(e.getDestinationNode(), withProbabilities));
			str.append(";\n");
		}
		
		/* Closing */
		str.append("}");
		return str.toString();
	}

	/**
	 * @param n
	 * @param withProbability TODO
	 * @return
	 */
	private static String getNameWithProbability(Node n, boolean withProbability) {
		if (withProbability) {
			return sanitize(n.getName()) + Float.toString(((ProbabilisticNode)n).getMarginalAt(0)).replace("-", "m");
		} else {
			return sanitize(n.getName());
		}
	}

	private static String sanitize(String str) {
		return str.replaceAll("\\s","").replaceAll("-", "");
	}
	
	/**
	 * @param fileCounter
	 * @param dotGraph
	 * @return
	 */
	public static void writeToFile(ProbabilisticNetwork pn, String outputFileName) {
		writeToFile(pn, outputFileName, false);
	}

	/**
	 * @param withProb TODO
	 * @param fileCounter
	 * @param dotGraph
	 * @return
	 */
	public static void writeToFile(ProbabilisticNetwork pn, String outputFileName, boolean withProb) {
		try {
			PrintWriter out = new PrintWriter(outputFileName);
			out.write(getGraphInDot(pn, withProb));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
}
