/**
 * 
 */
package analysis.multipath.salfer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 * Implementation of the Poincaré-Sylvester formula. (also known as
 * "Sieb" or inclusion-exclusion principle formula.)
 * 
 * This class computes the probability that at least one incoming event is happening.
 * @author Martin Salfer
 * @created 29.05.2016 18:41:49
 *
 */
public class PoincareSylvester {

	final private Map<String, Double> incomingEventProbabilities = new HashMap<String, Double>();
	/**
	 * Number of events to be unified at once.
	 * Initialised with -1.
	 */
	private int unificationNumber = -1;

	public PoincareSylvester() {
	}

	/**
	 * Add data for processing with the Poincaré-Sylvester formula.
	 * 
	 * @param eventName - Name of the event.
	 * @param eventProbability - Probability of the event to occur.
	 */
	void addEventAndProbability(String eventName, Double eventProbability) {
		if (eventProbability < 0d) throw new IllegalArgumentException("Probability " + eventProbability + " is below [0;1]!");
		if (eventProbability > 1d) throw new IllegalArgumentException("Probability " + eventProbability + " is over [0;1]!");
		incomingEventProbabilities.put(eventName, eventProbability);
	}

	/**
	 * @return probability for at least one of the events.
	 */
	public double getTotalProbability() {
		final int n = incomingEventProbabilities.size();
		switch (n) {
		case 0:
			throw new IllegalArgumentException("Keine Eingabe spezifiziert. Zuerst mit addEventProbability() etwas hinzufügen.");
		case 1:
			return incomingEventProbabilities.values().iterator().next();
		}
		assert n >= 2;
		
		for (Double d: incomingEventProbabilities.values()) {
			assert d >= 0d : "Probability " + d + " is below [0;1]!";
			assert d <= 1d : "Probability " + d + " is over [0;1]!";
		}
		switch (unificationNumber) {
		case -1:
			throw new IllegalArgumentException("Missing unification number. Use setUnificationAmount(int)");
		case 1:
			return unifyBy1();
		case 2:
			return unifyBy2();
		case 3:
			return unifyBy3();
		default:
			return unifyByK();
		}
	}

	/**
	 * @return
	 */
	private double unifyBy1() {
		LinkedList<Double> probabilities = new LinkedList<Double>(incomingEventProbabilities.values());
		assert probabilities.size() >= 1;
		ListIterator<Double> iterator = probabilities.listIterator();
		double acc = iterator.next();
		while (iterator.hasNext()) {
			Double newD = iterator.next();
			acc = acc + newD - acc * newD;
		}
		return acc;
	}
	
	
	/**
	 * @return
	 */
	private double unifyBy2() {
		LinkedList<Double> probabilities = new LinkedList<Double>(incomingEventProbabilities.values());
		assert probabilities.size() >= 2;
		while (probabilities.size() >= 2) {
			ListIterator<Double> iterator = probabilities.listIterator();
			while (iterator.hasNext()) {
				Double d1 = iterator.next();
				iterator.remove();  // Working on the list and consuming the processed element.
				if (iterator.hasNext() == false) {
					// if only one node is left for processing, it can be left for the next iteration.
					iterator.add(d1); // but put the last node back to the list. Do not lose it!
					break; // Now, only one result is left, which must be the result.
				}
				Double d2 = iterator.next();
				iterator.remove();
				iterator.add(combine2Inputs(d1, d2));
			}
		}
		assert probabilities.size() == 1 : "Only one result is allowed after the looping computation!";
		return probabilities.element();
	}

	
	/**
	 * @return
	 */
	private double unifyBy3() {
		LinkedList<Double> probabilities = new LinkedList<Double>(incomingEventProbabilities.values());
		assert probabilities.size() >= 3;
		while (probabilities.size() >= 3) {
			ListIterator<Double> iterator = probabilities.listIterator();
			while (iterator.hasNext()) {
				Double d1 = iterator.next();
				iterator.remove();  // Working on the list and consuming the processed element.
				if (iterator.hasNext() == false) {
					// if only one node is left for processing, it can be left for the next iteration.
					iterator.add(d1); // but put the last node back to the list. Do not lose it!
					break; // Now, only one result is left, which must be the result.
				}
				Double d2 = iterator.next();
				iterator.remove();
				if (iterator.hasNext() == false) {
					// Now, only two input variables are left. Special processing required.
					iterator.add(combine2Inputs(d1, d2));
					break; // Now, only one result is left, which must be the result.
				}
				Double d3 = iterator.next();
				iterator.remove();
				iterator.add(combine3Inputs(d1, d2, d3));
			}
		}
		if (probabilities.size() == 2) {
			double d1 = probabilities.poll();
			double d2 = probabilities.poll();
			probabilities.add(combine2Inputs(d1, d2));
		}
		assert probabilities.size() == 1 : "Only one result is allowed after the looping computation!";
		return probabilities.element();
	}

	/**
	 * @param d1
	 * @param d2
	 * @param d3
	 * @return
	 */
	private double combine3Inputs(Double d1, Double d2, Double d3) {
		return d1 + d2 + d3 - d1*d2 - d2*d3 - d1*d3 + d1*d2*d3;
	}

	/**
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double combine2Inputs(Double d1, Double d2) {
		return d1 + d2 - d1 * d2;
	}

	
	
	/**
	 * @return
	 */
	private Double unifyByK() {
		throw new UnsupportedOperationException("Noch nicht implementiert!");
//		double totalProbability = 0d;
//		int k; // Number of factors per summand.
//		for (k = 1; k <= n; k++) { // outer summation
//			double lineSum = 0d;
//
//			// inner summation
//			double[] factors = new double[k];
//			double[] products = new double[k];
//			double product = 1d;
//			for (double factor : factors) {
//				product *= factor;
//			}
//
//			for (int numberOfFactors = 1; numberOfFactors <= n; numberOfFactors++) {
//			}
//
//			lineSum += DoubleStream.of(products).sum();
//
//			// TODO Produkt der einzelnen Teile.
//
//			if (k % 2 == 0) { // even lines are supposed to be summarized
//								// negatively.
//				lineSum = -lineSum;
//			}
//			totalProbability += lineSum;
//		}
//		return totalProbability; // TODO
	}

	/**
	 * @param k - number of incoming events to be unified at once. 
	 */
	public void setUnificationAmount(int k) {
		unificationNumber = k;
	}

}
