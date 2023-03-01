/**
 * 
 */
package benchmarking;

class MeasurementResults {
	/**
	 * In milliseconds.
	 */
	public long runtime = 0;
	/**
	 * In bytes.
	 */
	public long memory = 0;
	
	/**
	 * How often an java.lang.OutOfMemoryError occurred during measurement. 	
	 */
	public int outOfMemoryErrorOccurences = 0;
	
	/**
	 * How many nodes were in the system model.
	 */
	final public int nodesNumber;
	
	public int tries;
	
	public MeasurementResults(int numberOfNodes) {
		nodesNumber = numberOfNodes;
	}
	
	public long getMB() {
		return memory / 1_000_000;
	}
	
	public long getMilli() {
		return runtime;
	}
}