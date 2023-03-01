/**
 * 
 */
package attackGraph.visualization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import attackGraph.AttackGraph;
import attackGraph.AttackGraphEdge;
import attackGraph.AttackGraphNode;
import attackGraph.AttackGraphNodeAccess;
import attackGraph.AttackGraphNodeAsset;
import attackGraph.AttackGraphNodeSoftware;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.viz.ColorImpl;

/**
 * @author Martin Salfer
 * @created 13.03.2017 16:13:08
 *
 */
public class GexfExporter {


	private Gexf gexf;
	private Attribute attOvVulProb;
	private Attribute attOvAffProb;
	private Attribute attOvAttProb;
	private Map<AttackGraphNode, Node> gexfNodes;

	public void exportAttackGraphToGexfFile(AttackGraph ag, String fileName) {
		/* Initalize graph */
		initGraph(ag);
		
		/* Import nodes. */
		importNodes(ag, gexf.getGraph());

		/* Edges */
		importEdges(ag);
		
		/* Write output */
		writeToFile(fileName);
	}

	/**
	 * @param fileName
	 */
	private void writeToFile(String fileName) {
		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File(fileName);
		Writer out;
		try {
			out =  new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println("GEXF written to file " + f.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param ag
	 */
	private void importEdges(AttackGraph ag) {
		for (AttackGraphEdge e: ag.getAllAttackGraphEdges()) {
			Node from = gexfNodes.get(e.origin);
			Node to = gexfNodes.get(e.target);
			from.connectTo(Integer.toHexString(e.hashCode()), to)
				.setEdgeType(EdgeType.DIRECTED)
				.setWeight((float) e.target.getAttackabilityProbabilityForOptimalPath())
				.setColor(new ColorImpl(255, 168, 0)) // orange.
				.setLabel("via " + e.exploit)
				//.setShape(EdgeShape.DASHED) // will be ignored by Gephi importer.
				//.setThickness(i*3); // will be ignored by Gephi importer.
			;
		}
	}

	/**
	 * @param ag
	 * @param graph
	 */
	private void importNodes(AttackGraph ag, Graph graph) {
		gexfNodes = new HashMap<AttackGraphNode, Node>(ag.getNumberOfNodes());
		for (AttackGraphNode agNode : ag.getAllAttackGraphNodes()) {
			Node n = graph.createNode(Integer.toHexString(agNode.hashCode()));
			gexfNodes.put(agNode, n);
			n.setLabel(agNode.getName());
			//.setSize(20) // not needed, kept for later swift use.
			//n.getShapeEntity().setNodeShape(NodeShape.TRIANGLE); // ignored by Gephi.

			n.getAttributeValues()
				.addValue(attOvVulProb, Double.toString(agNode.getVulnerabilityProbabilityForOptimalPath()))
				.addValue(attOvAffProb, Double.toString(agNode.getAffordabilityProbabilityForOptimalPath()))
				.addValue(attOvAttProb, Double.toString(agNode.getAttackabilityProbabilityForOptimalPath()));

			// Colour nodes.
			if (agNode instanceof AttackGraphNodeAsset) {
				n.setColor(new ColorImpl(0, 90, 255)); // blue
			}
			if (agNode instanceof AttackGraphNodeAccess) {
				n.setColor(new ColorImpl(255, 0, 0)); // red.
			}
			if (agNode instanceof AttackGraphNodeSoftware) {
				n.setColor(new ColorImpl(0, 200, 0)); // green
			}
		}
	}

	/**
	 * @param ag
	 */
	private void initGraph(AttackGraph ag) {
		/* Generic */
		gexf = new GexfImpl();
		gexf.getMetadata()
			.setLastModified(Calendar.getInstance().getTime())
			.setCreator("Martin Salfer")
			.setDescription("Attack Graph Export of " + ag.getName());
		gexf.setVisualization(true);
		gexf.getGraph()
			.setDefaultEdgeType(EdgeType.DIRECTED)
			.setMode(Mode.STATIC);
		
		/* Attributes */
		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		gexf.getGraph().getAttributeLists().add(attrList);
		attOvVulProb = attrList.createAttribute("ovVulnProb", AttributeType.FLOAT, "Lückenwahrscheinlichkeit");
		attOvAffProb = attrList.createAttribute("ovAffordProb", AttributeType.FLOAT, "Bezahlbarkeits-Wahrscheinlichkeit");
		attOvAttProb = attrList.createAttribute("ovAttProb", AttributeType.FLOAT, "Angreifbarkeits-Wahrscheinlichkeit");
	}
	
	
}
