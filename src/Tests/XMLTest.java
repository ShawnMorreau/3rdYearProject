/**
 * 
 */
package Tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Model.Message;
import Model.ModelContainer;
import Model.NetworkTopology;
import Model.Router;
import Routing.RandomRoutingAlgorithm;
import View.Connection;
import View.Node;
import View.ShapesContainer;
import XML.XMLState;
import XML.XMLStateSerializer;

/**
 * Tests for the save/restore of the topology to and from an XML file
 * 
 * @author Aaron Bungay
 */
public class XMLTest {

	private XMLStateSerializer serializer;
	private XMLState state;
	private ModelContainer modelContainer;
	private ShapesContainer shapesContainer;
	private File saveFile;
	
	/**
	 * Creates a basic topology and view shapes for serialization testing
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		NetworkTopology topology = new NetworkTopology();
		Router a = new Router("A");
		Router b = new Router("B");
		Message messageAB = new Message(0, a, b);
		a.storeMessage(messageAB);
		topology.addRouter(a);
		topology.addRouter(b);
		topology.setRouterNeighbour("A", "B");
		topology.setRouterNeighbour("B", "A");
		topology.incrementTotalPacketsTransmittedMetric();
		this.modelContainer = new ModelContainer(topology, new RandomRoutingAlgorithm(), 1, 1, 1);
		
		Node nodeA = new Node(100, 100);
		nodeA.setName("A");
		nodeA.setMessages(Collections.singletonList(messageAB));
		Node nodeB = new Node(200, 200);
		nodeB.setName("B");
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(nodeA);
		nodes.add(nodeB);
		Connection connectionAB = new Connection(nodeA, nodeB);
		this.shapesContainer = new ShapesContainer(nodes, Collections.singletonList(connectionAB));
		
		this.state = new XMLState();
		this.state.setModelContainer(modelContainer);
		this.state.setShapesContainer(shapesContainer);
		
		this.serializer = new XMLStateSerializer();
		this.saveFile = new File("XMLTEST-" + hashCode() + ".xml");
	}
	
	/**
	 * Deletes the temporary xml file which was used for serialization testing
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (saveFile != null) {
			saveFile.delete();
		}
	}
	
	/**
	 * Ensure the serializer creates a new file
	 */
	@Test
	public void testXMLSerialize() {
		serializer.serialize(saveFile, state);
		assertTrue(saveFile.exists());
		assertTrue(saveFile.length() > 0);
	}
	
	/**
	 * Ensure the deserialized container has the correct data stored in it (model, shapes, etc)
	 */
	@Test
	public void testXMLDeserialize() {
		serializer.serialize(saveFile, state);
		
		state = new XMLState();
		serializer.deserialize(saveFile, state);
		ModelContainer newModelContainer = state.getModelContainer();
		ShapesContainer newShapesContainer = state.getShapesContainer();
		
		NetworkTopology newTopology = newModelContainer.getTopology();
		assertEquals(1, newTopology.getTotalPacketsTransmittedMetric());
		assertTrue(newTopology.contains("A"));
		assertTrue(newTopology.contains("B"));
		assertFalse(newTopology.getRouter("A").getStoredMessages().isEmpty());
		assertTrue(newTopology.getNeighbours("A").get(0).getName().equals("B"));
		assertTrue(newTopology.getNeighbours("B").get(0).getName().equals("A"));
		
		assertTrue(newModelContainer.getChosenRoutingAlgorithm() instanceof RandomRoutingAlgorithm);
		assertEquals(1.0, newModelContainer.getMessageGenerationRate(), 0.0);
		assertEquals(1.0, newModelContainer.getMessageCounter(), 0.0);
		assertEquals(1.0, newModelContainer.getStepCounter(), 0.0);
		
		assertEquals(shapesContainer.getNodes(), newShapesContainer.getNodes());
		assertEquals(shapesContainer.getConnections(), newShapesContainer.getConnections());
		
	}
	
}
