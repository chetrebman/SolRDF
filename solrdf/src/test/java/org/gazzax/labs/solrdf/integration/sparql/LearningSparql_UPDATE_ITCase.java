/*
 * This Test case makes use of some examples from 
 * 
 * "Learning SPARQL - Querying and Updating with SPARQL 1.1" by Bob DuCharme
 * 
 * Publisher: O'Reilly
 * Author: Bob DuCharme
 * ISBN: 978-1-449-37143-2
 * http://www.learningsparql.com/
 * http://shop.oreilly.com/product/0636920030829.do
 * 
 * We warmly appreciate and thank the author and O'Reilly for such permission.
 * 
 */
package org.gazzax.labs.solrdf.integration.sparql;

import static org.gazzax.labs.solrdf.MisteryGuest.misteryGuest;

import java.util.Iterator;

import org.gazzax.labs.solrdf.MisteryGuest;
import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;

/**
 * SPARQL Update Integration tests using examples taken from LearningSPARQL book.
 *  
 * @author Andrea Gazzarini
 * @since 1.0
 * @see http://learningsparql.com
 */  
public class LearningSparql_UPDATE_ITCase extends LearningSparqlSupertypeLayer {
	
	/**
	 * Executes a given update command both on remote and local model.
	 * 
	 * @param data the object holding test data (i.e. commands, queries, datafiles).
	 * @throws Exception hopefully never otherwise the corresponding test fails.
	 */
	void executeUpdate(final MisteryGuest data) throws Exception {
		load(data);
		
		final String updateCommandString = readFile(data.query);
		UpdateExecutionFactory.createRemote(UpdateFactory.create(updateCommandString), SPARQL_ENDPOINT_URI).execute();

		commitChanges();

		UpdateAction.parseExecute(updateCommandString, memoryDataset.asDatasetGraph());
		
		final Iterator<Node> nodes = memoryDataset.asDatasetGraph().listGraphNodes();
		if (nodes != null) {
			while (nodes.hasNext()) {
				final Node graphNode = nodes.next();
				final String graphUri = graphNode.getURI();
				final Model inMemoryNamedModel = memoryDataset.getNamedModel(graphUri);
				assertIsomorphic(inMemoryNamedModel, DATASET.getModel(graphUri), graphUri);		
			}
		}
		
		assertIsomorphic(memoryDataset.getDefaultModel(), DATASET.getModel(), null);			
	}
	
	@Test
	public void insertDataKeyword() throws Exception {
		executeUpdate(misteryGuest("ex312.ru"));
	}
	
	@Test
	public void insertKeyword() throws Exception {
		executeUpdate(misteryGuest("ex313.ru", "ex012.ttl"));
	}	
	
	@Test
	public void insertAsConstructThatChangesData() throws Exception {
		executeUpdate(misteryGuest("ex316.ru", "ex012.ttl"));
	}
	
	@Test
	public void loadKeyword() throws Exception {
		executeUpdate(misteryGuest("ex546.ru"));
	}	
	
	@Test
	public void deleteDataKeyword() throws Exception {
		selectTest(misteryGuest("ex547.rq", "exxyz.ttl"));
		executeUpdate(misteryGuest("ex548.ru"));
		selectTest(misteryGuest("ex547.rq"));		
	}	
	
	@Test
	public void deleteKeyword_I() throws Exception {
		executeUpdate(misteryGuest("ex549.ru", "exxyz.ttl"));
	}		
	
	@Test
	public void deleteKeyword_II() throws Exception {
		executeUpdate(misteryGuest("ex550.ru", "exxyz.ttl"));
	}			
	
	@Test
	public void deleteKeyword_III() throws Exception {
		executeUpdate(misteryGuest("ex551.ru", "exxyz.ttl"));
	}
	
	@Test
	public void changingExistingData() throws Exception {
		selectTest(misteryGuest("ex311.rq", "ex012.ttl"));
		constructTest(misteryGuest("ex326.rq"));
		executeUpdate(misteryGuest("ex325.ru"));
		selectTest(misteryGuest("ex311.rq"));
	}
	
	@Test
	public void changingSkosInSkosXL() throws Exception {
		executeUpdate(misteryGuest("ex329.ru", "ex327.ttl"));
	}	
	
	@Test
	public void insertDataInNamedGraphs() throws Exception {
		executeUpdate(misteryGuest("ex330.ru"));
		executeUpdate(misteryGuest("ex331.ru"));
		selectTest(misteryGuest("ex332.rq"));
	}	

	@Test
	public void insertInNamedGraphs() throws Exception {
		executeUpdate(misteryGuest("ex330.ru"));
		executeUpdate(misteryGuest("ex543.ru"));
		selectTest(misteryGuest("ex332.rq"));
	}	

	@Test
	public void createGraph() throws Exception {
		executeUpdate(misteryGuest("ex340.ru"));
	}	
	
	@Test
	public void withKeyword() throws Exception {
		executeUpdate(misteryGuest("ex342.ru"));
	}	
	
	@Test
	public void copyKeyword() throws Exception {
		executeUpdate(misteryGuest("ex338.ru"));
		executeUpdate(misteryGuest("ex503.ru"));		
	}	

	@Test
	public void moveKeyword() throws Exception {
		executeUpdate(misteryGuest("ex338.ru"));
		executeUpdate(misteryGuest("ex505.ru"));	
	}		
}