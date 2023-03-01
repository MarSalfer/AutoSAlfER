package systemModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



/**
 * A capability models a certain feature, privilege or right a task possesses.
 * A typical capability of tasks is the right to access a certain communication
 * node or the privilege to issue ring 0 CPU commands.
 * A capability can be a requirement for exploits.
 * 
 * A capability models also ingress/egress firewalls. An exploit can have certain
 * capabilities as a requirement. An egress firewall would be an extra capability
 * and a exploit would require a certain capability.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 * @deprecated Use Software and Communication Media instead.
 */
public class Capability extends Node {



//	private final Set<ECU> m_ECU = new HashSet<ECU>();
	/**
	 * Communication Nodes are can be access-only or access+control nodes.
	 */
	private final Set<CommunicationMedium> comNodes = new HashSet<CommunicationMedium>();
	private final Set<Software> m_controlledTask = new HashSet<Software>();
	private final Set<Software> parents = new HashSet<Software>();
	private final Set<Software> controlledTasks = new HashSet<Software>(); // TODO unify with m_controlledTask!

	public Capability() {
		this("<noname>");
	}

	public Capability(String name) {
		super(name);
	}
	
//	public Capability(Task parent){
//		this("<noname>", parent, (Set<Node>) null);
//	}

//	public Capability(String name, Task parent) {
//		this(name, parent, (Set<Node>) null);
//	}
//
//	public Capability(String string, Task parent, CommunicationNode node) {
//		this(string, parent, encapsulate(node));
//	}
//
//	// TODO Umbau des Sets auf Controllable und Accessible.
//	public Capability(String name, Task parent, Set<? extends Node> nodes) {
//		super(name);
//		for (Node n : nodes) { // TODO reengineer this quick hack.
//			if (n instanceof Task)
//				m_controlledTask.add((Task) n);
//			if (n instanceof CommunicationNode) 
//				m_CommunicationNodeAccess.add((CommunicationNode) n);
//			if (n instanceof ECU)
//				m_ECU.add((ECU) n);
//		}
//		// TODO implement control com node feature.
//	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Complexity: O(1) as of assumption 2, 3 and 4.
	 */
	@Override
	public Set<Software> getReachableSoftwareComplete() {
		return getReachableSoftwareOverComNodes(comNodes);
	}

	/**
	 * @param comNodes
	 * @return
	 */
	private Set<Software> getReachableSoftwareOverComNodes(final Set<CommunicationMedium> comNodes) {
		Set<Software> reachableTasks = new HashSet<Software>();
		reachableTasks.addAll(m_controlledTask); // Complexity: const as of assumption 4
		for (CommunicationMedium c : comNodes) {  // Complexity const as of assumption 3.
			reachableTasks.addAll(c.getReachableSoftwareComplete()); // Complexity const as of assumption 2 and 4
		}
// Rausgenommen, da die access-controll-modellierung nochmals überarbeitet werden muss.
//		for (CommunicationNode c : comNodesControlled) {
//			reachableTasks.addAll(c.getReachableTasks());
//		}
		return reachableTasks;
	}

	public Set<Software> getParentTasks() {
		return parents; // Complexity: const as of assumption 4.
	}

	public Set<CommunicationMedium> getCommunicationNodesForTask(Software target) {
		Set<CommunicationMedium> attachedComNodes = new HashSet<CommunicationMedium>();
		for (CommunicationMedium c : attachedComNodes) {
			Set<Software> controlledTasks = c.getControlledTasks();
			if (controlledTasks != null)
				if (controlledTasks.contains(target))
					attachedComNodes.add(c);	
		}
		return attachedComNodes;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Capability [" + name + "]";
	}

	/**
	 * Get all Tasks under control by this Capability.
	 * @return a copied set.
	 */
	@Override
	public Set<Software> getControlledTasks() {
		return new HashSet<Software>(controlledTasks);
	}

	public void grantAccessTo(CommunicationMedium comNode) {
		comNodes.add(comNode);
		comNode.registerForAccess(this);
	}

	public void grantControlOver(Set<Software> tasks) {
		m_controlledTask.addAll(tasks);
	}

	public void addOwnerSoftware(Software owner) {
		parents.add(owner);
	}

	public Set<CommunicationMedium> getCommunicationNodes() {
		return Collections.unmodifiableSet(comNodes);
	}

	public Set<? extends Software> getReachableSoftwareOverOpenComNodes() {
		Set<CommunicationMedium> unclosedComNodes = new HashSet<CommunicationMedium>(comNodes.size());
		for (CommunicationMedium c : comNodes) {
			if (!c.isClosedForPhaseI()) {
				unclosedComNodes.add(c);
			}
		}
		return getReachableSoftwareOverComNodes(unclosedComNodes);
	}

	
}//end Capability