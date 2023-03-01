package systemModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import systemModel.subTypes.InputType;



/**
 * A communication node represents an ether for exchanging information.
 * This comprises both physical and virtual protocols, e.g. inter-process
 * communication.
 * 
 * 
 * Vehicles require a low carbon foot print or energy consumption.
 * This implies a minus for complex monitoring security measures, e.g. IDS or
 * introspection techniques.
 * We assume therefore a system design without any IDS or dynamic measures and set
 * focus on a well designed passive network, and a few simple filter techniques.
 * We assume that security is greatly dependent onto a system's architecture,
 * which acts as a passive protection, similar to a castle, which profits greatly
 * from a well designed wall layout.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:08
 */
public class CommunicationMedium extends Node {

	/**
	 * This attribute helps connecting com nodes with attacker profile accesses.
	 */
	private boolean isAccessibleWirelessly;
	/**
	 * This attribute helps connecting com nodes with attacker profile accesses.
	 */
	private boolean isAccessibleWithoutModification;
	/**
	 * This attribute helps connecting com nodes with attacker profile accesses.
	 */
	private boolean isAccessibleWithoutOEMAuth;
	/**
	 * This attribute helps connecting com nodes with attacker profile accesses.
	 */
	private boolean isAccessibleWithoutOwnerAuth;
	/**
	 * 0 := Not likely, e.g., internal and isolated Com Nodes.
	 * 10 := very accessible, e.g., wirelessly above > 100 m.
	 * 
	 * This attribute helps connecting com nodes with attacker profile accesses.
	 */
	private int typicalAccessibility;
	
	/**
	 * @deprecated 
	 */
	private final Set<Capability> m_Capability = new HashSet<Capability>();
	
	private boolean isClosedForPhaseI = false;

	/**
	 * Software that is reading from this Communication Media.
	 */
	private final Set<Software> softwareReadingFrom = new HashSet<Software>();
	
	/**
	 * Software that is writing into this Communication Media.
	 */
	private final Set<Software> softwareWritingOnto = new HashSet<Software>();

	
	public CommunicationMedium(){
		super();
	}

	
	
	public CommunicationMedium(String string) {
		super(string);
	}





	@Override
	public String toString() {
		if (isClosedForPhaseI() == true) {
			return "CommunicationMedium [" + name + ", P1Closed]";
		} else{
			return "CommunicationMedium [" + name + ", P1Open]";
		}
	}
	
	public String toString2(){
		StringBuffer str = new StringBuffer();
		str.append("Signal: " + name+ "\t\t");
		str.append("Sending SWs:\t");
		for (Software sw : softwareWritingOnto){
			str.append(sw + "\t");
		}
		str.append("Receiving SWs:\t");
		for (Software sw : softwareReadingFrom){
			str.append(sw + "\t");
		}
		
		str.append("\n----------------------------------------");
		
		return str.toString();
	}


	/**
	 * Complexity: Const as of assumption 2 and 4. TODO Redo calc.
	 */
	@Override
	public Set<Software> getReachableSoftwareComplete() {
		return Collections.unmodifiableSet(softwareReadingFrom);
	}

	

	public void finalize() throws Throwable {
		super.finalize();
	}



	/**
	 * @return always null as communication nodes do not control software.
	 */
	@Override
	public Set<Software> getControlledTasks() {
		return null; // intentionally null as a com node does not controll any other software.
	}

	/**
	 * @deprecated
	 */
	public void registerForAccess(Capability c) {
		softwareReadingFrom.addAll(c.getParentTasks());
		softwareWritingOnto.addAll(c.getParentTasks());
		m_Capability.add(c);
	}
	

	/**
	 * Close this Communication Node.
	 * Phase I marks as closed for not re-expanding this Communication Node.
	 */
	public void closeForPhaseI() {
		isClosedForPhaseI = true;
	}

	public boolean isClosedForPhaseI() {
		return isClosedForPhaseI;
	}


	/**
	 * Register a Software node as a Reader.
	 * Only to be called by Software! 
	 * @param software - Reading on this Communication Media.
	 */
	void registerBeingReadBy(Software software) {
		softwareReadingFrom.add(software);
		software.addAttackSurface(new IncomingSignal(name, InputType.UNKNOWN));  // TODO differentiate here by extending the FIBEX importer.
	}


	/**
	 * Register a Software node as a writer for this Communication Media.
	 * @param software - Writing via this Communication Media.
	 */
	void registerBeingWrittenBy(Software software) {
		softwareWritingOnto.add(software);
	}


	/**
	 * Check whether this Communication Media has a software attached to that is writing to here.
	 * It uses a local set for speed up.
	 * @param software
	 * @return
	 */
	public boolean isWrittenBy(Software software) {
		return softwareWritingOnto.contains(software);
	}


	/**
	 * Check whether this Communication Media has a software attached to that is reading from here.
	 * It uses a local set for speed up.
	 * @param software
	 * @return
	 */
	public boolean isReadBy(Software software) {
		return softwareReadingFrom.contains(software);
	}


	/**
	 * Get the set of Software that reads from this Communication Medium
	 * @return the set of Software that reads from this Communication Medium.
	 */
	public Set<? extends Software> getReadingSoftware() {
		return Collections.unmodifiableSet(softwareReadingFrom);
	}



	/**
	 * Get the set of Software that writes to this Communication Medium.
	 * @return The set of Software that write to this Communication Medium.
	 */
	public Set<Software> getSoftwareWritingOnto() {
		return Collections.unmodifiableSet(softwareWritingOnto);
	}

	
	
}//end Communication Node