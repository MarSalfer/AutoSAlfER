/**
 * 
 */
package attackGraph.visualization;

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
import it.uniroma1.dis.wsngroup.gexf4j.core.viz.EdgeShape;
import it.uniroma1.dis.wsngroup.gexf4j.core.viz.NodeShape;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;

import org.junit.Test;
/**
 * @author Martin Salfer
 * @created 13.03.2017 10:50:44
 *
 */
public class Gexf4jTest {

	private static final String SAMPLE_FILE = "output/test/Gexf4jTest.gexf";

	@Test
	public void exportStaticTest() {
		/* Create Graph */
		Gexf gexf = new GexfImpl();
		gexf.getMetadata()
			.setLastModified(Calendar.getInstance().getTime())
			.setCreator("Martin Salfer")
			.setDescription("GefxExportStaticTest-Graph");
		gexf.setVisualization(true);

		Graph graph = gexf.getGraph().setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.STATIC);

		/* Attributes */
		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(attrList);
		Attribute attOvVulProb = attrList.createAttribute("ovVulnProb", AttributeType.FLOAT, "Lückenwahrscheinlichkeit");
		Attribute attOvAffProb = attrList.createAttribute("ovAffordProb", AttributeType.FLOAT, "Bezahlbarkeits-Wahrscheinlichkeit");
		Attribute attOvAttProb = attrList.createAttribute("ovAttProb", AttributeType.FLOAT, "Angreifbarkeits-Wahrscheinlichkeit");
	 
		/* Nodes */
		Node access = graph.createNode("OsmoRadio");
		access
			.setLabel("Mobilfunk-Transceiver")
			.setSize(20)
			.setColor(new ColorImpl(255, 0, 0))
			.getAttributeValues()
				.addValue(attOvVulProb, "1.0")
				.addValue(attOvAffProb, "1.0")
				.addValue(attOvAttProb, "1.0");
		access.getShapeEntity().setNodeShape(NodeShape.TRIANGLE);
		
		Node ecuA = graph.createNode("ECUA");
		ecuA
			.setLabel("ECU A")
			.setColor(new ColorImpl(0, 255, 0))
			.getAttributeValues()
				.addValue(attOvVulProb, "0.5")
				.addValue(attOvAffProb, "0.4")
				.addValue(attOvAttProb, "0.3");
		
		Node ecuB = graph.createNode("ECUB");
		ecuB
			.setLabel("ECU B")
			.setColor(new ColorImpl(0, 255, 0))
			.getAttributeValues()
				.addValue(attOvVulProb, "0.3")
				.addValue(attOvAffProb, "0.3")
				.addValue(attOvAttProb, "0.2");
		
		Node ecuC = graph.createNode("ECUC");
		ecuC
			.setLabel("ECU C")
			.getAttributeValues()
				.addValue(attOvVulProb, "0.1")
				.addValue(attOvAffProb, "0.1")
				.addValue(attOvAttProb, "0.1");
		
		/* Edges */
		final String exploit = "via Exploit";
		int i = 1;
		access.connectTo(exploit + i++, ecuA)
			.setEdgeType(EdgeType.DIRECTED)
			.setWeight(10f/i)
			.setColor(new ColorImpl(0, 0, 255))
			.setLabel(exploit + i)
			.setShape(EdgeShape.DASHED) // will be ignored by Gephi-Importer.
			.setThickness(i*3); // will be ignored by Gephi-Importer.
		ecuA.connectTo(exploit + i++, ecuB)
			.setEdgeType(EdgeType.DIRECTED)
			.setWeight(10f/i)
			.setColor(new ColorImpl(0, 255, 255))
			.setLabel(exploit + i)
			.setShape(EdgeShape.DASHED) // will be ignored by Gephi-Importer.
			.setThickness(i*3); // will be ignored by Gephi-Importer.
		ecuA.connectTo(exploit + i++, ecuC)
			.setEdgeType(EdgeType.DIRECTED)
			.setWeight(10f/i)
			.setColor(new ColorImpl(255, 0, 255))
			.setLabel(exploit + i)
			.setShape(EdgeShape.DASHED) // will be ignored by Gephi-Importer.
			.setThickness(i*3); // will be ignored by Gephi-Importer.
		ecuB.connectTo(exploit + i++, ecuA)
			.setEdgeType(EdgeType.DIRECTED)
			.setWeight(10f/i)
			.setColor(new ColorImpl(0, 0, 0))
			.setLabel(exploit + i)
			.setShape(EdgeShape.DASHED) // will be ignored by Gephi-Importer.
			.setThickness(i*3); // will be ignored by Gephi-Importer.
		ecuB.connectTo(exploit + i++, ecuC)
			.setEdgeType(EdgeType.DIRECTED)
			.setWeight(10f/i)
			.setColor(new ColorImpl(22, 22, 255))
			.setLabel(exploit + i)
			.setShape(EdgeShape.DASHED) // will be ignored by Gephi-Importer.
			.setThickness(i*3); // will be ignored by Gephi-Importer.

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File(Gexf4jTest.SAMPLE_FILE);
		Writer out;
		try {
			out =  new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(2_920, f.length());
	}


}
