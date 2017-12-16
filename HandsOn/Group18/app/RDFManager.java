package linkeddata;
import java.util.LinkedList;

import java.io.InputStream;


import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.LiteralImpl;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

public class RDFManager {
	
	private OntModel model;

	public RDFManager() {
		
		String filename = "C:\\Users\\Alvaro\\Desktop\\UPM\\Semestre7\\WebSemantica\\practica\\buenoGrupo18.rdf";
		
		model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
		
		InputStream in = FileManager.get().open(filename);
		
		if (in == null)
			throw new IllegalArgumentException("File: "+filename+" not found");
		
		model.read(in, null);
	}

	public LinkedList<RDFResult> searchByName(String name) {
		//Listar todos los parkings que tengan el parametro name en su nombre
		
		LinkedList<RDFResult> nombres = new LinkedList<RDFResult>();
		String queryString =
				"PREFIX vcard: <http://www.w3.org/2006/vcard/ns#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX park: <http://www.semanticweb.org/grupo18/ontologies#> "
				+ "SELECT ?direccion ?zona "
				+ "WHERE {  "
				+ "?s rdfs:label \""+name+"@fr\" . "
				+ "?s vcard:hasAddress ?a . "
				+ "?a rdfs:label ?direccion . "
				+ " ?a park:belongsTo ?zona . "
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,this.model);
		ResultSet results = qexec.execSelect();
		
		while(results.hasNext()){
			
			QuerySolution binding = results.nextSolution();
			RDFNode zona = (RDFNode) binding.get("zona");
			RDFNode direccion = (RDFNode) binding.get("direccion");
			RDFResult element = new RDFResult(name, direccion.toString(), zona.toString());
			nombres.add(element);
		}
		
		return nombres;
	}

	public LinkedList<RDFResult> searchByParkingSpots(int min, int max) {
		
		return null;
	}
}