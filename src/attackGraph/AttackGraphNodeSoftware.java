/**
 * 
 */
package attackGraph;

import systemModel.Exploitable;

/**
 * @author Martin Salfer
 *
 */
public class AttackGraphNodeSoftware extends AttackGraphNode {

	protected final Exploitable exploitable;

	/**
	 * @param exploitable
	 */
	public AttackGraphNodeSoftware(Exploitable exploitable) {
		super();
		this.exploitable = exploitable;
	}
	
	public Exploitable getSysNode() {
		return exploitable;
	}
	
	@Override
	public String getName() {
		return exploitable.toString();
	}

	@Override
	public String toString() {
		return "AG-" + exploitable.toString();
	}

	/* (non-Javadoc)
	 * @see attackGraph.AttackGraphNode#getIdentificationObject()
	 */
	@Override
	public Object getIdentificationObject() {
		return exploitable;
	}
	
	
	
}
