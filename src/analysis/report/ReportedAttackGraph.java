package analysis.report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

import systemModel.Asset;
import types.Resources;
import attackGraph.AttackGraphEdge;
import attackerProfile.Access;

public class ReportedAttackGraph {

	@XmlAttribute
	public String name;
	
	@XmlElementWrapper (name = "accessSet")
	public final Set<Access> access = new HashSet<Access>();
	
	@XmlElementWrapper (name = "attractorSet")
	public final Set<Asset> attractor = new HashSet<Asset>();

	@XmlElementWrapper (name = "attackSteps")
	public final List<AttackGraphEdge> attackStep = new ArrayList<AttackGraphEdge>();
	
	public Resources cost;
	
	public Resources gain;
	
	public Resources profit;

	public Resources roi;
	
	public double probAffordability;
	
	public double probProfit;
	
	public double probAttack;
	
	public double expectedDamage;
	
	public ReportedAttackGraph() {
	}


}
