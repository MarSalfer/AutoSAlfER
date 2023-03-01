/**
 * @author Martin Salfer
 *
 */
open module AutoSAlfER {
	exports attackGraph.visualization;
	exports systemModel.attackSurface;
	exports types;
	exports benchmarking;
	exports analysis.multipath.salfer;
	exports systemModel;
	exports systemModel.com;
	exports attackGraph;
	exports analysis.plotWriter;
	exports analysis.report;
	exports analysis;
	exports systemModel.fibexImporter;
	exports analysis.multipath.bayes;
	exports analysis.comparator;
	exports modelEvaluation;
	exports supplement;
	exports systemModel.importer;
	exports systemModel.statistics;
	exports attackerProfile;
	exports metric;
	exports systemModel.subTypes;
	exports exploit;
	exports analysis.multipath;
	exports systemModel.fibexImporter.xsd31;

	requires gexf4j;
	requires java.sql;
	requires java.xml;
	requires java.xml.bind;
	requires junit;
	requires unbbayes;
	requires org.apache.poi.poi;
	requires org.apache.poi.ooxml;
	requires commons.math3;
}