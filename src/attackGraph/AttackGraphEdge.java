package attackGraph;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import exploit.Exploit;



/**
 * Each edge represents an applied exploit within an attack graph.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class AttackGraphEdge {

	@XmlElement
	public AttackGraphNode origin;
	/**
	 * The destination node for an attack step.
	 */
	
	@XmlElement
	public Exploit exploit;
	
	/**
	 * The originating node for an attack step.
	 */
	@XmlElement
	public AttackGraphNode target;

	public AttackGraphEdge(AttackGraphNode a, Exploit ex, AttackGraphNode b) {
		origin = a;
		target = b;
		exploit = ex;
	}

	public void finalize() throws Throwable {

	}
	
	public String toString() {
		return "AttackGraphEdge [ " + origin.toString() + " -> " + target.toString() + "]";
	}
}//end Attack Graph Edge