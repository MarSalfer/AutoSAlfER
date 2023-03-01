/**
 * 
 */
package metric;

import java.math.BigInteger;

import org.apache.commons.math3.util.ArithmeticUtils;


/**
 * @author Martin Salfer
 * 2015-03-17
 *
 */
public class NumericEvCalculation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		double p = 0.000_5d; // Vulnerability density.
		double q = 1d - p;
		int n = 140;
//		int n = 1;
		int fi = 10;
		int f0 = 5_000;
		
		double evYInt = 0;
		for (int i=1; i<=n ; i++) {
//			evY += i * 	binonmialCoefficientDouble(n, i) * Math.pow(p, i) * Math.pow(q, n-i);
//			evYInt += i * 	Math.pow(q, i-1) * p;
			evYInt += i * binonmialCoefficientDouble(n, 1) * Math.pow(q, i-1) * p;
			System.out.println("" + i + ": evYInt = " + evYInt + " P: " + Math.pow(q, i-1));
		}
		double evY = evYInt;
		
		
		double evF = f0 + fi * evY;

		System.out.println();
		System.out.println("p = " + p);
		System.out.println("q = " + q);
		System.out.println("n = " + n);
		System.out.println("f0 = " + f0);
		System.out.println("fi = " + fi);
		System.out.println("EV(Y) = " + evY);
		System.out.println("EV(f) = " + evF);
		
		System.out.println("P(X=0) = " + bernoulliProcess(n, p, 0));
		System.out.println("P(X>0) = " + (1d - bernoulliProcess(n, p, 0)));
		System.out.println("bino(n,1) = " + binonmialCoefficientDouble(n, 1));
		
		
		
	}

	/**
	 * @param n
	 * @return
	 */
	private static double bernoulliProcess(int n, double p, int x) {
		double q = 1d - p;
		return binonmialCoefficientDouble(n, x) * Math.pow(p, x) * Math.pow(q, n-x);
	}

	/**
	 * @param n
	 * @param i
	 * @return
	 */
	private static long binonmialCoefficient(int n, int i) {
		return ArithmeticUtils.binomialCoefficient(n, i);
	}

	
	/**
	 * @param n
	 * @param i
	 * @return
	 */
	private static double binonmialCoefficientDouble(int n, int k) {
		BigInteger binomCoeff = faculty(n).divide(faculty(k)).divide(faculty(n-k));
		return binomCoeff.doubleValue();
	}

	/**
	 * @param n
	 * @return
	 */
	private static BigInteger faculty(int n) {
		BigInteger nFaculty = new BigInteger("1"); 
		for (int i=1 ; i<=n ; i++) {
			nFaculty.multiply(new BigInteger(Integer.toString(i)));
		}
		return nFaculty;
	}


}
