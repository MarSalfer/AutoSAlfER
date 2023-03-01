package analysis.report;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import systemModel.Asset;
import analysis.Analyzer;
import attackGraph.AttackGraph;
import attackerProfile.AttackerProfile;

/**
 * A report contains the full text calculation and results for attack graphs.
 * @author Martin Salfer
 * @version 1.0
 * @created 12-Nov-2013 16:18:09
 */
@XmlRootElement(name = "AttackRiskReport")
@XmlType //(propOrder = {"time", "title", "attackPath", "content"})
public class Report {

	@XmlAttribute
	public String date;
	
	@XmlAttribute
	public String title;
	
	@XmlElement
	public AttackerProfile attackerProfile;
	
	@XmlElement
	private final List<ReportedAttackGraph> attackGraphReport = new ArrayList<ReportedAttackGraph>();
	
	
	public Report(){
	}
	
	public List<ReportedAttackGraph> getList() {
		return attackGraphReport;
	}

	public void finalize() throws Throwable {

	}

	/**
	 * A given attack graph is analysed regarding the attack cost, gain, profit,
	 * ROI, attacker probabilities and expected damage. A report is generated
	 * and returned.
	 * 
	 * @param graph
	 * @deprecated Use {@link #analyzeAndGenerateReport(AttackGraph,String)} instead
	 */
	public static String analyzeAndGenerateReport(AttackGraph graph) {
		return analyzeAndGenerateReport(graph, null);
	}

	/**
	 * A given attack graph is analysed regarding the attack cost, gain, profit,
	 * ROI, attacker probabilities and expected damage. A report is generated
	 * and returned.
	 * 
	 * @param graph
	 * @param outputFile TODO
	 */
	public static String analyzeAndGenerateReport(AttackGraph graph, String outputFile) {
		Report r = new Report();
		List<ReportedAttackGraph> attackPaths = r.getList();
		r.title = "Generated Report for Attack Risk Evaluation";
		r.date = Calendar.getInstance().getTime().toString();
		r.attackerProfile = graph.getScenario().getAttackerProfile();

		/* Generate a report of each attractor. */
		final Set<Asset> attractors = graph.assets();
		for (Asset attractor : attractors) {
			final ReportedAttackGraph attackPath = new ReportedAttackGraph();
			attackPath.name = "Report for Attractor '" + attractor.getName() + "'";
//			attackPath.access.add(graph.getUsedAccessForAttractor(attractor));
//			attackPath.attractor.add(attractor);
			attackPath.attackStep.addAll(graph.getAttackPathTo(attractor));
			attackPath.cost = Analyzer.costMaxForAttractor(graph, attractor);
			attackPath.gain = Analyzer.gainMaxForAttractor(graph, attractor);
			attackPath.profit = Analyzer.profitForAttractor(graph, attractor);
			attackPath.roi = Analyzer.roiForAttractor(graph, attractor);
			attackPath.probAffordability = Analyzer.attackProbability(graph, attractor);
			attackPath.probProfit = Analyzer.profitPositiveProbability(graph, attractor);
			attackPath.probAttack = Analyzer.attackProbability(graph, attractor);
			attackPath.expectedDamage = Analyzer.damageExpected(graph, attractor);
			attackPaths.add(attackPath);
		}

		/* Generate an overall report, i.e., for all attractors at once */
		final ReportedAttackGraph attackPath = new ReportedAttackGraph();
		attackPath.name = "Report for All Attractors";
		attackPath.access.addAll(graph.getUsedAccessForAssets(attractors));
		attackPath.attractor.addAll(attractors);
		attackPath.attackStep.addAll(graph.getAttackNodesTo(attractors));
		attackPath.cost = Analyzer.costMaxForAttractors(graph, attractors);
		attackPath.gain = Analyzer.gainMaxForAttractors(graph, attractors);
		attackPath.profit = Analyzer.profitForAttractors(graph, attractors);
		attackPath.roi = Analyzer.roiForAttractors(graph, attractors);
		attackPath.probAffordability = Analyzer.attackProbability(graph, attractors);
		attackPath.probProfit = Analyzer.profitPositiveProbability(graph, attractors);
		attackPath.probAttack = Analyzer.attackProbability(graph, attractors);
		attackPath.expectedDamage = Analyzer.damageExpected(graph, attractors);
		attackPaths.add(attackPath);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10_000);
		try {
			try {
				JAXBContext context = JAXBContext.newInstance(Report.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(r, outputStream);
				if (outputFile != null) { // optionally output to file.
					FileOutputStream fileOS = new FileOutputStream(outputFile);
					m.marshal(r, fileOS);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return outputStream.toString();
	}
}//end Report