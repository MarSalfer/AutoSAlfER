package systemModel;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;



/**
 * A system model node is the basic element of the general representation of an
 * automotive E/E software task. It needs to be specialized as it is only an
 * abstract class for grouping and summarizing system model elements.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public abstract class Node {

	public static final int UNSET = -1;
	/**
	 * The name of the system model element. The name is useful for references to a
	 * node in human readable representations and is even mandatory in some sub
	 * classes: Task objects require the name and version attributes for identifying
	 * known exploits and matching them to applicable tasks.
	 */
	@XmlAttribute
	protected final String name;
	/**
	 * Number of minimally necessary exploits to the nearest Asset.
	 * This information is prepared by the graph construction heuristic preparation
	 * run and annotated in the system model.
	 * Initially, this value is unset.
	 */
	@XmlTransient
	protected int hopDistanceToAssets = UNSET;

//	public Integer hopDistanceToAttractors = null;
	public Node(){
		this("<noName>");
	}

	public Node(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + "]";
	}

	public void finalize() throws Throwable {
	}

	/**
	 * @return Software objects with unclosed Communication Medium nodes in between. 
	 */
	public abstract Set<Software> getReachableSoftwareComplete(); // TODO implement for all subclasses correctly!

	/**
	 * Necessary for Tasks under control of this node.
	 * @return
	 */
	public abstract Set<Software> getControlledTasks();

	/**
	 * Query for the software incorporated into this system node.
	 * 
	 * @return - an empty set as only a few system nodes contain software.
	 */
	public Set<Software> getIncorporatedSoftware() {
		return new HashSet<Software>(); // Usually, a node contains no software.
	}

	/**
	 * Initialise the hop distance counter.
	 * @param hopDistanceToAttractors Positive number of minimally necessary exploits to the nearest Asset.
	 */
	public void initHopDistance(int hopDistanceToAttractors) {
		assert hopDistanceToAttractors >= 0;
		assert hopDistanceToAttractors >= this.hopDistanceToAssets || hopDistanceToAttractors == UNSET;  // In Phase-I, the nearest nodes should init first.
		if (this.hopDistanceToAssets == UNSET && (hopDistanceToAttractors > 0)) {
			this.hopDistanceToAssets = hopDistanceToAttractors;
		}
	}
	
	/**
	 * Getter
	 * @return How many Exploits / "hops" are necessary for reaching the next Asset.
	 */
	public int getHopsToAssets() {
		return hopDistanceToAssets;
	}
	/**
	 * Getter
	 * @return Name of the node.
	 */
	public String getName() {
		return name;
	}
	
}//end Node