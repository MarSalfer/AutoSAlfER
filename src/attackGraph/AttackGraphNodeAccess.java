package attackGraph;

import systemModel.Exploitable;
import attackerProfile.Access;


public class AttackGraphNodeAccess extends AttackGraphNode {

	public final Access access;

	public AttackGraphNodeAccess(Access a) {
		this.access = a;
		setOverallAffordabilityProb(1d);
		setAttackabilityProbabilityForOptimalPath(1d);
		setVulnerabilityProbabilityForOptimalPath(1d);
	}
	
	public void finalize() throws Throwable {
	}

	/**
	 * Get the represented system node, but an access node is not part of the system model
	 * and should not return the accessible system model nodes to get not confused with
	 * attack graph nodes that directly represent system model nodes.
	 * 
	 * @return null as it does not represent a system node.
	 */
	@Override
	public Exploitable getSysNode() {
		return null;
	}

	@Override
	public String getName() {
		return access.toString();
	}
	
	public Access getAccessObject() {
		return access;
	}
	
	@Override
	public String toString() {
		return access.toString();
	}

	/* (non-Javadoc)
	 * @see attackGraph.AttackGraphNode#getIdentificationObject()
	 */
	@Override
	public Object getIdentificationObject() {
		return access;
	}
	
	
}//end Attack Graph Node