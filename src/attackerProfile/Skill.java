package attackerProfile;



/**
 * The skills class models features of attackers that are necessary for creating
 * and applying exploits.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
public class Skill {

	private String name;
	/**
	 * Each effort is divided by the skill proficiency for influencing costs according
	 * to their skill quality.
	 * Preferred Range is ]0;1]. 1 models average expert knowledge. Numbers above 1
	 * are allowed, too, and model more than average expert knowledge, in case of
	 * extraordinarily skilled attackers.
	 */
	private float proficiency;

	public Skill(){
		this("<noname>");
	}
	
	public Skill(String name) {
		this.name = name;
	}

	public String toString() {
		return name + proficiency;
	}

	public void finalize() throws Throwable {

	}
}//end Skills