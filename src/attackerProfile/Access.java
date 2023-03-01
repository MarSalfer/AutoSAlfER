package attackerProfile;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import systemModel.Node;
import systemModel.Software;
import types.Resources;



/**
 * The access class models the possibilities that attacker have to reach vehicles.
 * The main attributes are the set of system nodes that is accessible to the
 * attackers and a number of how many vehicles are affected or accessible.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:07
 */
public class Access {

	@XmlAttribute
	private final String name;
	/**
	 * How many vehicles are accessible with this access method?
	 */
	@XmlAttribute
	private final int numberOfTargetableVehicles;
	/**
	 * What system node is accessible by this access method?
	 */
	private final Node accessibleSysNode;
	/**
	 * How much costs an access per vehicle?
	 */
	@XmlElement
	private final Resources accessCostPerVehicle;
	/**
	 * How does the access work?
	 */
	private String accessDescription;

	public Access(){
		this("<noname>", null, 0);
	}
	
	public Access(String name, Node node, int accessibleNodes) {
		this(name, node, accessibleNodes, new Resources(0,0));
	}
	
	public Access(String name, Node node, int numberOfTargetableVehicles, Resources accessCostPerVehicle) {
		this.name = name;
		this.accessibleSysNode = node;
		this.numberOfTargetableVehicles = numberOfTargetableVehicles;
		this.accessCostPerVehicle = accessCostPerVehicle;
		assert (name != null && node != null && accessCostPerVehicle != null);
	}

	public String toString() {
		return "Access [" + name + "]"; // + accessCostDescription + numberOfTargetableVehicles;
	}

	public void finalize() throws Throwable {
	}

	public Node getSysNode() {
		return accessibleSysNode;
	}

	public Resources getAccessCost() {
		return accessCostPerVehicle;
	}
	
	public int getNumberOfTargetAbleVehicles() {
		return numberOfTargetableVehicles;
	}

	public String getName() {
		return name;
	}

	/**
	 * Get all Software that is represented by this Access object.
	 * It can return a full set of Software in the case of a full ECU being the Access Object.
	 * @return
	 */
	public Set<Software> getAccessibleSoftware() {
		return accessibleSysNode.getIncorporatedSoftware();
	}

	/**
	 * Calculate the maximum access cost, i.e. access all targetable vehicles.
	 * @return The cost for targeting all vehicles.
	 */
	public float getAccessCostMax() {
		return numberOfTargetableVehicles * accessCostPerVehicle.getMoneyExpectedValueFloat();
	}

	/**
	 * Gets the cost for accessing all targetable vehicles.
	 * @return Access cost per vehicle * number of targetable vehicles.
	 */
	public Resources getMaxmimumAccessCost() {
		return accessCostPerVehicle.multiplyWith(numberOfTargetableVehicles);
	}
	
}//end Access