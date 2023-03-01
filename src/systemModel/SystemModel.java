package systemModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import systemModel.com.Signal;



/**
 * A system model comprises a car's basic software structure. It serves a risk
 * analysis, is intentionally kept concise and contains only attacker-agnostic
 * features.
 * The system model comprises also search graph data (for implementation simplicity)
 * and is therefore not multi threading safe.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public class SystemModel {

	public final String name;
	
	/**
	 * @deprecated
	 */
	public final Set<Node> nodes = new HashSet<Node>();

	/**
	 * Set of all Software nodes in our system model.
	 */
	private final Set<Software> softwareSet = new HashSet<Software>();
	
	/**
	 * All ECUs of a System Model.
	 */
	private final Set<ECU> ecuSet = new HashSet<ECU>();
	

	public SystemModel(){
		name = "<NoName>";
	}

	public SystemModel(String string) {
		this.name = string;
	}

	public void finalize() throws Throwable {
	}

	public void addNode(Node node) {
		nodes.add(node);
		if (node instanceof Software) {
			softwareSet.add((Software) node);
		}
		if (node instanceof ECU) {
			ecuSet.add((ECU)node);
		}
	}

	/**
	 * Complexity: O(|S|);
	 * @return
	 */
	public Set<Asset> getAttractors() {
		Set<Asset> attractors = new HashSet<Asset>();
		
		// preferred way for collecting the assets.
		for (Software sw : softwareSet) {
			attractors.addAll(sw.getAttractors());
		}
		
		// deprecated part, but kept for compatibility so far.
		for (Node n : nodes) { // O(|S|)
			if (n instanceof Asset) {
				attractors.add((Asset)n);
			}
		}
		return attractors;
	}

	public void addNodes(Collection<Software> collection) {
		for (Software software : collection) {
			this.addNode(software);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SystemModel [name=");
		builder.append(name);
		builder.append("\nNodes:\n");
		for (Node n: nodes) {
			builder.append("* ");
			builder.append(n);
			builder.append("\n");
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Get the Software node with the corresponding name.
	 * @param name of the Software node.
	 * @return The Software node; node if no applicaple software found.
	 */
	public Software getSoftwareWithName(String name) {
		for (Software sw : softwareSet){
			if (sw.name.equals(name)) {
				return sw;
			}
		}
		return null;
	}

	/**
	 * Get a set of all Software nodes in this system model.
	 * @return a set of all software nodes in this system model.
	 */
	public Set<Software> getSoftwareSet() {
		return Collections.unmodifiableSet(softwareSet);
	}

	/** Return all ECUs of a System Model. 
	 * 
	 * @return all ECUs of a System Model.
	 */
	public Set<ECU> getEcus() {
		return Collections.unmodifiableSet(ecuSet);
	}

	/**
	 * Return an ECU by its name.
	 * @param ecuName - the name of the ECU.
	 * @return the ECU object. Null if not found.
	 */
	public ECU getEcuByName(String ecuName) {
		for (ECU ecu : ecuSet) {
			if (ecu.name.equals(ecuName)) {
				return ecu;
			}
		}
		return null;
	}

	/**
	 * Get the number of all signals used in this system model.
	 */
	public int getSignalsTotalNumber() {
		Set<Signal> signals = new HashSet<Signal>();
		for (Software s : softwareSet) {
			signals.addAll(s.getUsedSignals());
		}
		return signals.size();
	}
	
	



	
}//end System Model